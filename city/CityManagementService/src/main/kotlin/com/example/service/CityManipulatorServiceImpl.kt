package com.example.com.example.service

import com.example.com.example.dao.CityDao
import com.example.com.example.entity.City
import jakarta.ejb.Stateless
import jakarta.ws.rs.BadRequestException
import java.time.LocalDate

@Stateless
open class CityManipulatorServiceImpl : CityManipulatorServiceRemote {

    private val cityDao: CityDao = CityDao()

    override fun getCities(sort: String?, filter: String?, page: Int?, size: Int?): ArrayList<City> {
        val effectiveSize = cityDao.findAll().size

        var result = ArrayList(cityDao.findAll())

        if (filter != null) {
            try {
                result = ArrayList(applyFilter(result, filter))
            } catch (e: BadRequestException) {
                throw e
            }
        }

        result = ArrayList(applyPagination(result, page!!, effectiveSize))

        if (sort != null) {
            result = ArrayList(applySort(result, sort))
        }
        return result
    }

    override fun addCity(city: City): City {
        city.creationDate = LocalDate.now().toString()
        return cityDao.save(city)
    }

    override fun getCity(id: Long): City? {
        return cityDao.findById(id)
    }

    override fun updateCity(updatedCity: City): City? {
        return cityDao.update(updatedCity)
    }

    override fun deleteCity(id: Long) {
        cityDao.delete(id)
    }

    override fun deleteBySeaLevel(level: Float) {
        cityDao.deleteBySeaLevel(level)
    }

    override fun getSeaLevelSum(): Double {
        return cityDao.getSeaLevelSum()
    }

    override fun getByNamePrefix(prefix: String): List<City> {
        return cityDao.findByNamePrefix(prefix)
    }

    companion object {
        private fun applyFilter(cityList: List<City>, filter: String): List<City> {
            val (field, operation, value) = parseFilter(filter)
            return when (field) {
                "name" -> {
                    if (operation == "=") {
                        cityList.filter { it.name == value }
                    } else {
                        throw BadRequestException("Only '=' operator is supported for 'name' field")
                    }
                }

                "population" -> cityList.filterByNumericField(
                    { it.population?.toLong() },
                    operation,
                    value.toLongOrNull()
                )

                "area" -> cityList.filterByNumericField({ it.area }, operation, value.toLongOrNull())
                "capital" -> {
                    if (operation == "=") {
                        cityList.filterByBooleanField({ it.capital }, value.toBooleanStrictOrNull())
                    } else {
                        throw BadRequestException("Only '=' operator is supported for 'capital' field")
                    }
                }

                "metersAboveSeaLevel" -> cityList.filterByNumericField(
                    { it.metersAboveSeaLevel.toDouble() },
                    operation,
                    value.toDoubleOrNull()
                )

                "id" -> cityList.filterByNumericField({ it.id }, operation, value.toLongOrNull())
                else -> cityList
            }
        }

        // Фильтрация по числовому полю
        private fun <T : Comparable<T>> List<City>.filterByNumericField(
            fieldSelector: (City) -> T?,
            operation: String,
            value: T?
        ): List<City> {
            if (value == null) return this
            return this.filter { city ->
                val fieldValue = fieldSelector(city)
                when (operation) {
                    ">" -> fieldValue != null && fieldValue > value
                    "<" -> fieldValue != null && fieldValue < value
                    "=" -> fieldValue != null && fieldValue == value
                    else -> false
                }
            }
        }

        // Фильтрация по булевому полю
        private fun List<City>.filterByBooleanField(fieldSelector: (City) -> Boolean, value: Boolean?): List<City> {
            return if (value == null) this else this.filter { fieldSelector(it) == value }
        }

        private fun parseFilter(filter: String): Triple<String, String, String> {
            val regex = """([a-zA-Z]+)([><=]+)(\d+)""".toRegex()
            val match = regex.find(filter)
            val (field, operation, value) = match?.destructured ?: throw BadRequestException("Invalid filter format")
            return Triple(field, operation, value)
        }

        private fun applySort(cityList: List<City>, sort: String): List<City> {
            return when (sort) {
                "name" -> cityList.sortedBy { it.name }
                "population" -> cityList.sortedBy { it.population }
                "area" -> cityList.sortedBy { it.area }
                "capital" -> cityList.sortedBy { it.capital }
                "metersAboveSeaLevel" -> cityList.sortedBy { it.metersAboveSeaLevel }
                "id" -> cityList.sortedBy { it.id }
                else -> cityList
            }
        }

        private fun applyPagination(cityList: List<City>, page: Int, size: Int): List<City> {
            val fromIndex = (page - 1) * size
            val toIndex = minOf(fromIndex + size, cityList.size)
            //return if (fromIndex < cityList.size) cityList.subList(fromIndex, toIndex) else emptyList()
            return if (fromIndex < cityList.size) ArrayList(cityList.subList(fromIndex, toIndex)) else arrayListOf()
        }
    }
}