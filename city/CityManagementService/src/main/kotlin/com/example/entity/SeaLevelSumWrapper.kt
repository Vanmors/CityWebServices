package com.example.com.example.entity

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement
import java.io.Serializable

@XmlRootElement(name = "seaLevelSum")
@XmlAccessorType(XmlAccessType.FIELD)
data class SeaLevelSumWrapper(
    @field:XmlElement(name = "value")
    val value: Double = 0.0,
) : Serializable {
    companion object {
        @Transient
        const val serialVersionUID: Long = 34L
    }
}