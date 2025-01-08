package com.example.com.example.service.impl

import com.example.com.example.service.CityValidatorRemote
import com.example.entity.City
import jakarta.ejb.Stateless

@Stateless
open class CityValidator : CityValidatorRemote {

    override fun validateCity(city: City): List<String> {
        val errors = mutableListOf<String>()

        // Валидация name
        if (city.name.isNullOrBlank()) {
            errors.add("Name cannot be null or empty")
        }

        // Валидация coordinates
        if (city.coordinates == null) {
            errors.add("Coordinates cannot be null")
        } else {
            if (city.coordinates?.x == null) {
                errors.add("Coordinate x cannot be null")
            }
            if (city.coordinates?.y == null || city.coordinates?.y!! <= -197) {
                errors.add("Coordinate y cannot be null and must be greater than -197")
            }
        }

        // Валидация area
        if (city.area == null || city.area!! <= 0) {
            errors.add("Area must be greater than 0 and cannot be null")
        }

        // Валидация population
        if (city.population == null || city.population!! <= 0) {
            errors.add("Population must be greater than 0 and cannot be null")
        }

        // Валидация standardOfLiving
        if (city.standardOfLiving == null) {
            errors.add("Standard of Living cannot be null")
        }

        return errors
    }
}