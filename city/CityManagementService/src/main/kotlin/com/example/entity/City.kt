package com.example.com.example.entity

import jakarta.persistence.*
import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement
import java.io.Serializable
import kotlin.jvm.Transient

@Entity
@Table(name = "city")
@XmlRootElement(name = "City")
@XmlAccessorType(XmlAccessType.FIELD)
data class City(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @field:XmlElement
    var id: Long? = null,

    @Version
    @field:XmlElement
    var version: Int = 0,

    @field:XmlElement
    var name: String? = null,
    @Embedded
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
    @Enumerated(EnumType.STRING)
    @field:XmlElement
    var climate: Climate? = null,
    @Enumerated(EnumType.STRING)
    @field:XmlElement
    var standardOfLiving: StandardOfLiving? = null,
    @field:XmlElement
    @ManyToOne
    @JoinColumn(name = "governor_id")
    var governor: Human? = null
):  Serializable {
    companion object {
        @Transient
        const val serialVersionUID: Long = 14L
    }
}

@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
data class Coordinates(
    @field:XmlElement
    var x: Double? = null,
    @field:XmlElement
    var y: Int? = null
) : Serializable {
    companion object {
        @Transient
        const val serialVersionUID: Long = 15L
    }
}

@Entity
@XmlAccessorType(XmlAccessType.FIELD)
data class Human(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @field:XmlElement
    var birthday: String? = null
) : Serializable {
    companion object {
        @Transient
        const val serialVersionUID: Long = 16L
    }
}

enum class Climate : Serializable {
    MONSOON, HUMIDCONTINENTAL, OCEANIC, POLAR_ICECAP
}

enum class StandardOfLiving : Serializable {
    ULTRA_HIGH, VERY_HIGH, LOW, ULTRA_LOW, NIGHTMARE
}

