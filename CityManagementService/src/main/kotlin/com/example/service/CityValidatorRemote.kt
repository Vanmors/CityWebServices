package com.example.com.example.service

import com.example.entity.City
import jakarta.ejb.Remote

@Remote
interface CityValidatorRemote {
    fun validateCity(city: City): List<String>
}