package com.example.entity

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "seaLevelSum")
@XmlAccessorType(XmlAccessType.FIELD)
data class SeaLevelSumWrapper(
    @field:XmlElement(name = "value")
    val value: Double = 0.0,
)