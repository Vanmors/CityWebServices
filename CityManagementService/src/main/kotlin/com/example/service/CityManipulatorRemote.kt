package com.example.com.example.service

import com.example.entity.City
import jakarta.ejb.Remote

@Remote
interface CityManipulatorRemote {

    fun applyFilter(cityList: List<City>, filter: String): List<City>

    fun applySort(cityList: List<City>, sort: String): List<City>

    fun applyPagination(cityList: List<City>, page: Int, size: Int): List<City>
}