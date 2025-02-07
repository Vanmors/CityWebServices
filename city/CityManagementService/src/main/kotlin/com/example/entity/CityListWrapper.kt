package com.example.com.example.entity

import jakarta.xml.bind.annotation.*
import java.io.Serializable

@XmlRootElement(name = "cities")
@XmlAccessorType(XmlAccessType.FIELD)
data class CityListWrapper(
    //todo мб city с большой буквы?
    @field:XmlElement(name = "city")
    var cities: List<City> = listOf()
) : Serializable {
    companion object {
        @Transient
        const val serialVersionUID: Long = 24L
    }
}