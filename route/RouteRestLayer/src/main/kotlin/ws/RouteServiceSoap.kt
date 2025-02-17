package com.example.ws

import jakarta.jws.WebMethod
import jakarta.jws.WebParam
import jakarta.jws.WebService

//todo исправить урлы - targetNamespace

@WebService(name = "RouteServiceSoap", targetNamespace = "http://controllers.ru.ifmo.se/")
interface RouteServiceSoap {

    @WebMethod
    fun calculateDistanceBetweenOldestAndNewest(): Double

    @WebMethod
    fun calculateDistanceToOldest(): Double
}