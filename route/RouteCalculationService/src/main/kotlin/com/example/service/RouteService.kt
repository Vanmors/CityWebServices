package com.example.service

import Coordinates
import RouteServiceRemote
import jakarta.enterprise.context.ApplicationScoped
import javax.naming.InitialContext
import javax.naming.NamingException

@ApplicationScoped
open class RouteService {
    open val routeServiceRemote =
        getFromEJBPool("ejb:/CityWebService-1.0-SNAPSHOT/RouteServiceImpl!RouteServiceRemote")
    // TODO (проверить путь!!)
    //getFromEJBPool("ejb:/city-management/CityManipulatorServiceImpl!com.example.com.example.service.CityManipulatorServiceRemote")

    open fun calculateDistanceBetweenOldestAndNewest(coord1: Coordinates, coord2: Coordinates): Double {
        return routeServiceRemote.calculateDistanceBetweenOldestAndNewest(coord1, coord2)
    }

    @Throws(NamingException::class)
    open fun getFromEJBPool(name: String): RouteServiceRemote {
        return InitialContext().lookup(name) as RouteServiceRemote
    }
}