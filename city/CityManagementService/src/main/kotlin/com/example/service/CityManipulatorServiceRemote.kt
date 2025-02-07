package com.example.com.example.service

import com.example.com.example.entity.City
import jakarta.ejb.Remote

@Remote
interface CityManipulatorServiceRemote {

    fun getCities(sort: String?, filter: String?, page: Int?, size: Int?): ArrayList<City>

    fun addCity(city: City): City

    fun getCity(id: Long): City?

    fun updateCity(updatedCity: City): City?

    fun deleteCity(id: Long)

    fun deleteBySeaLevel(level: Float)

    fun getSeaLevelSum(): Double

    fun getByNamePrefix(prefix: String): List<City>

}