package com.example

import com.example.entity.Coordinates
import jakarta.ejb.Remote

@Remote
interface RouteServiceRemote {

    fun calculateDistanceBetweenOldestAndNewest(coord1: Coordinates, coord2: Coordinates): Double

}