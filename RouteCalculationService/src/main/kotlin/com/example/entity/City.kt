package com.example.entity

import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement
import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType


@XmlRootElement(name = "City")
@XmlAccessorType(XmlAccessType.FIELD)
data class City(
    @field:XmlElement
    var id: Long? = null,
    @field:XmlElement
    var name: String? = null,
    @field:XmlElement
    var coordinates: Coordinates? = null,
    @field:XmlElement
    var creationDate: String? = null,
    @field:XmlElement
    var area: Long? = null,
    @field:XmlElement
    var population: Int? = null,
    @field:XmlElement
    var metersAboveSeaLevel: Float = 0f,
    @field:XmlElement
    var capital: Boolean = false,
    @field:XmlElement
    var climate: Climate? = null,
    @field:XmlElement
    var standardOfLiving: StandardOfLiving? = null,
    @field:XmlElement
    var governor: Human? = null
)

@XmlAccessorType(XmlAccessType.FIELD)
data class Coordinates(
    @field:XmlElement
    var x: Double? = null,
    @field:XmlElement
    var y: Int? = null
)

@XmlAccessorType(XmlAccessType.FIELD)
data class Human(
    @field:XmlElement
    var birthday: String? = null
)

enum class Climate {
    MONSOON, HUMIDCONTINENTAL, OCEANIC, POLAR_ICECAP
}

enum class StandardOfLiving {
    ULTRA_HIGH, VERY_HIGH, LOW, ULTRA_LOW, NIGHTMARE
}