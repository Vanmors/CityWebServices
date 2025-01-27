package com.example.service.impl

import com.example.entity.City
import com.example.entity.CityListWrapper
import com.example.entity.Coordinates
import com.example.service.RouteServiceRemote
import jakarta.ejb.Stateless
import jakarta.ws.rs.client.ClientBuilder
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.pow
import kotlin.math.sqrt

@Stateless
class RouteService : RouteServiceRemote {

    private val citiesApiUrl = "https://city-management-service:8181/city-management-1.0-SNAPSHOT/api/cities"

    override fun calculateDistanceToOldest(): Response {
        try {
            val response = queryToCitiesApi()
            if (response.first != null) {
                return response.first!!
            }
            val cities = response.second!!

            val (oldestCity, _) = findOldestAndNewestCities(cities)

            if (oldestCity?.coordinates == null) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("<error>Cannot calculate distance: insufficient city data</error>").build()
            }

            val distance = calculateDistanceByCoordinates(oldestCity.coordinates!!, Coordinates(.0, 0))

            return Response.ok("<distance>$distance</distance>").build()

        } catch (e: Exception) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("<error>Server error: ${e.message}</error>")
                .build()
        }
    }

    override fun calculateDistanceBetweenOldestAndNewest(): Response {
        try {
            val response = queryToCitiesApi()
            if (response.first != null) {
                return response.first!!
            }
            val cities = response.second!!

            val (oldestCity, newestCity) = findOldestAndNewestCities(cities)

            if (oldestCity == null || newestCity == null || oldestCity.coordinates == null || newestCity.coordinates == null) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("<error>Cannot calculate distance: insufficient city data</error>").build()
            }

            val distance = calculateDistanceByCoordinates(oldestCity.coordinates!!, newestCity.coordinates!!)

            return Response.ok("<distance>$distance</distance>").build()

        } catch (e: Exception) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("<error>Server error: ${e.message}</error>")
                .build()
        }
    }


    private fun queryToCitiesApi(): Pair<Response?, List<City>?> {
        val client = ClientBuilder.newClient()

        val citiesResponse = client.target(citiesApiUrl)
            .request(MediaType.APPLICATION_XML)
            .get(Response::class.java)

        if (citiesResponse.status != 200) {
            return Pair(
                Response.status(citiesResponse.status)
                    .entity(citiesResponse).build(), null
                //.entity("<error>Failed to fetch cities</error>").build(), null
            )
        }

        val cities = citiesResponse.readEntity(CityListWrapper::class.java)

        if (cities.cities.isEmpty()) {
            return Pair(
                Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("<error>No cities found</error>")
                    .build(), null
            )
        }
        client.close()
        val mutableCities = cities.cities.toMutableList()
        return Pair(null, mutableCities)
    }

    private fun calculateDistanceByCoordinates(coord1: Coordinates, coord2: Coordinates): Double {
        val deltaX = coord1.x!! - coord2.x!!
        val deltaY = (coord1.y!! - coord2.y!!).toDouble()
        return sqrt(deltaX.pow(2) + deltaY.pow(2))
    }

    private fun findOldestAndNewestCities(cities: List<City>): Pair<City?, City?> {
        // Форматтеры для разбора дат с и без времени
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        // Преобразуем строки в LocalDateTime
        fun parseCreationDate(dateStr: String?): LocalDateTime? {
            return try {
                if (dateStr == null) null
                else if (dateStr.contains(" ")) LocalDateTime.parse(dateStr, dateTimeFormatter)
                else LocalDate.parse(dateStr, dateFormatter).atStartOfDay()
            } catch (e: Exception) {
                null // Если формат неверный, вернем null
            }
        }

        // Найти города с минимальной и максимальной датой
        val oldestCity = cities
            .filter { parseCreationDate(it.creationDate) != null } // Оставляем только города с валидными датами
            .minByOrNull { parseCreationDate(it.creationDate)!! }

        val newestCity = cities
            .filter { parseCreationDate(it.creationDate) != null }
            .maxByOrNull { parseCreationDate(it.creationDate)!! }

        return Pair(oldestCity, newestCity)
    }
}