package com.example.controller

import jakarta.jws.WebMethod
import jakarta.jws.WebService


@WebService(
    name = "RouteSoapService",
    serviceName = "RouteSoapService",
    targetNamespace = "http://com.example.controller/")
interface RouteSoapService {

    @WebMethod
    fun calculateDistanceBetweenOldestAndNewest(): Double

    @WebMethod
    fun calculateDistanceToOldest(): Double
}