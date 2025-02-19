package com.example

import com.example.entity.Coordinates
import jakarta.ejb.Stateless
import kotlin.math.pow
import kotlin.math.sqrt

@Stateless
open class RouteServiceRemoteImpl : RouteServiceRemote {

    override fun calculateDistanceBetweenOldestAndNewest(coord1: Coordinates, coord2: Coordinates): Double {
        val deltaX = coord1.x!! - coord2.x!!
        val deltaY = (coord1.y!! - coord2.y!!).toDouble()
        return sqrt(deltaX.pow(2) + deltaY.pow(2))
    }

}