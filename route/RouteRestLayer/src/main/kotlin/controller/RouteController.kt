package com.example.controller

import com.example.client.RouteSoapClient
import com.example.service.RouteService
import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.client.ClientBuilder
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Path("/route/calculate")
@Produces(MediaType.APPLICATION_XML)
@Consumes(MediaType.APPLICATION_XML)
open class RouteController {
    @Inject
    private lateinit var routeService: RouteService

    @GET
    @Path("/between-oldest-and-newest")
    @Produces(MediaType.APPLICATION_XML)
    open fun calculateDistanceBetweenOldestAndNewest(): Response {
        try {
            val distance = routeService.handleCalculateDistanceBetweenOldestAndNewest()
            return Response.ok("<distance>$distance</distance>").build()
        } catch (e: Exception) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("<error>Server error: ${e.message}</error>")
                .build()
        }
    }

    @GET
    @Path("/to-oldest")
    @Produces(MediaType.APPLICATION_XML)
    open fun calculateDistanceToOldest(): Response {
        try {
            val distance = routeService.handleCalculateDistanceToOldest()
            return Response.ok("<distance>$distance</distance>").build()
        } catch (e: Exception) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("<error>Server error: ${e.message}</error>")
                .build()
        }
    }
}