package com.example.controller

import com.example.entity.City
import com.example.entity.Coordinates
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.client.ClientBuilder
import kotlin.math.sqrt
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import kotlin.math.pow

@Path("/route/calculate")
@Produces(MediaType.APPLICATION_XML)
open class RouteController {
    private val citiesApiUrl = "http://localhost:8080/cities"

    @GET
    @Path("/between-oldest-and-newest")
    open fun calculateDistanceBetweenOldestAndNewest(): Response {
        try {
            val response = queryToCitiesApi()
            if (response.first != null) {
                return response.first!!
            }
            val cities = response.second!!

            val oldestCity = cities.filter { it.creationDate != null }.minByOrNull { it.creationDate!! }
            val newestCity = cities.filter { it.creationDate != null }.maxByOrNull { it.creationDate!! }

            if (oldestCity == null || newestCity == null || oldestCity.coordinates == null || newestCity.coordinates == null) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("<error>Cannot calculate distance: insufficient city data</error>").build()
            }

            val distance = calculateDistanceByCoordinates(oldestCity.coordinates!!, newestCity.coordinates!!)

            return Response.ok("<distance>$distance</distance>").build()

        } catch (e: Exception) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("<error>Server error: ${e.message}</error>")
                .build()
        }
    }

    @GET
    @Path("/to-oldest")
    open fun calculateDistanceToOldest(): Response {

        try {
            val response = queryToCitiesApi()
            if (response.first != null) {
                return response.first!!
            }
            val cities = response.second!!

            val oldestCity = cities.filter { it.creationDate != null }.minByOrNull { it.creationDate!! }

            if (oldestCity?.coordinates == null) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("<error>Cannot calculate distance: insufficient city data</error>").build()
            }

            val distance = calculateDistanceByCoordinates(oldestCity.coordinates!!, Coordinates(.0, 0))

            return Response.ok("<distance>$distance</distance>").build()

        } catch (e: Exception) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("<error>Server error: ${e.message}</error>")
                .build()
        }
    }

    private fun queryToCitiesApi(): Pair<Response?, List<City>?> {
        val client = ClientBuilder.newClient()

        val citiesResponse = client.target(citiesApiUrl)
            .request(MediaType.APPLICATION_XML)
            .get(Response::class.java)

        if (citiesResponse.status != 200) {
            return Pair(
                Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("<error>Failed to fetch cities</error>").build(), null
            )
        }

        val cities = citiesResponse.readEntity(Array<City>::class.java).toList()

        if (cities.isEmpty()) {
            return Pair(
                Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("<error>No cities found</error>")
                    .build(), null
            )
        }
        client.close()
        return Pair(null, cities)
    }

    private fun calculateDistanceByCoordinates(coord1: Coordinates, coord2: Coordinates): Double {
        val deltaX = coord1.x!! - coord2.x!!
        val deltaY = (coord1.y!! - coord2.y!!).toDouble()
        return sqrt(deltaX.pow(2) + deltaY.pow(2))
    }

}