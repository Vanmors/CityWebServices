package com.example.service

import jakarta.ejb.Remote
import jakarta.ws.rs.core.Response

@Remote
interface RouteServiceRemote {

    fun calculateDistanceToOldest(): Response

    fun calculateDistanceBetweenOldestAndNewest(): Response


}