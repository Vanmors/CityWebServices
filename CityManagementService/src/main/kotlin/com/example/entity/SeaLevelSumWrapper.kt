package com.example.entity

import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement
data class SeaLevelSumWrapper(
    @get:XmlElement
    val value: Double
)