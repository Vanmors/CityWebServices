package com.example.entity

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "cities")
@XmlAccessorType(XmlAccessType.FIELD)
data class CityListWrapper(
    //todo мб city с большой буквы?
    @field:XmlElement(name = "city")
    var cities: List<City> = listOf()
)