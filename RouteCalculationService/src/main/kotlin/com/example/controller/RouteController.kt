package com.example.controller

import com.example.service.RouteServiceRemote
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response

@Path("/route/calculate")
@Produces(MediaType.APPLICATION_XML)
open class RouteController @Inject constructor(
    private val routeServiceRemote: RouteServiceRemote
) {

    @GET
    @Path("/between-oldest-and-newest")
    open fun calculateDistanceBetweenOldestAndNewest(): Response {
        return routeServiceRemote.calculateDistanceBetweenOldestAndNewest()
    }

    @GET
    @Path("/to-oldest")
    open fun calculateDistanceToOldest(): Response {
        return routeServiceRemote.calculateDistanceToOldest()
    }

}