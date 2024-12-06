package com.example.controller

import com.example.entity.CityListWrapper
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import com.example.service.CityManipulator
import com.example.service.CityValidator
import java.time.LocalDate
import com.example.entity.City
import com.example.entity.SeaLevelSumWrapper
import java.util.concurrent.CopyOnWriteArrayList

@Path("/cities")
@Produces(MediaType.APPLICATION_XML)
@Consumes(MediaType.APPLICATION_XML)
open class CityResource {
    companion object {
        val cities = CopyOnWriteArrayList<City>()
    }
    @GET
    open fun getCities(
        @QueryParam("sort") sort: String?,
        @QueryParam("filter") filter: String?,
        @QueryParam("page") @DefaultValue("1") page: Int?,
        @QueryParam("size") size: Int?
    ): Response {
        try {
            val effectiveSize = size ?: cities.size

            var result = cities.toList()

            if (filter != null) {
                try {
                    result = CityManipulator.applyFilter(result, filter)
                } catch (e: BadRequestException) {
                    return Response.status(Response.Status.BAD_REQUEST).build()
                }
            }

            result = CityManipulator.applyPagination(result, page!!, effectiveSize)

            if (sort != null) {
                result = CityManipulator.applySort(result, sort)
            }

            return Response.ok(CityListWrapper(result)).build()
        } catch (e: Exception) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("<error>Server error: ${e.message}</error>")
                .build()
        }
    }

    @POST
    open fun addCity(city: City): Response {
        try {
            val validationErrors = CityValidator.validateCity(city)
            if (validationErrors.isNotEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("<error>${validationErrors.joinToString(", ")}</error>")
                    .build()
            }

            city.id = (cities.size + 1).toLong()
            city.creationDate = LocalDate.now().toString()
            cities.add(city)
            return Response.status(Response.Status.CREATED).entity(city).build()
        } catch (e: Exception) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("<error>Server error: ${e.message}</error>")
                .build()
        }
    }

    @GET
    @Path("/{id}")
    open fun getCity(@PathParam("id") id: Long): Response {
        return try {
            val city = cities.find { it.id == id }
            if (city != null) {
                Response.ok(city).build()
            } else {
                Response.status(Response.Status.NOT_FOUND).build()
            }
        } catch (e: Exception) {
            Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("<error>Server error: ${e.message}</error>")
                .build()
        }
    }

    @PUT
    @Path("/{id}")
    open fun updateCity(@PathParam("id") id: Long, updatedCity: City): Response {
        try {
            val validationErrors = CityValidator.validateCity(updatedCity)
            if (validationErrors.isNotEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("<error>${validationErrors.joinToString(", ")}</error>")
                    .build()
            }

            val city = cities.find { it.id == id }
            return if (city != null) {
                city.apply {
                    name = updatedCity.name
                    coordinates = updatedCity.coordinates
                    area = updatedCity.area
                    population = updatedCity.population
                    metersAboveSeaLevel = updatedCity.metersAboveSeaLevel
                    capital = updatedCity.capital
                    climate = updatedCity.climate
                    standardOfLiving = updatedCity.standardOfLiving
                    governor = updatedCity.governor
                }
                Response.ok(city).build()
            } else {
                Response.status(Response.Status.NOT_FOUND).build()
            }
        } catch (e: Exception) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("<error>Server error: ${e.message}</error>")
                .build()
        }
    }

    @DELETE
    @Path("/{id}")
    open fun deleteCity(@PathParam("id") id: Long): Response {
        return try {
            val city = cities.find { it.id == id }
            if (city != null) {
                cities.remove(city)
                Response.noContent().build()
            } else {
                Response.status(Response.Status.NOT_FOUND).build()
            }
        } catch (e: Exception) {
            Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("<error>Server error: ${e.message}</error>")
                .build()
        }
    }

    @DELETE
    //@Path("/bySeaLevel/{level}")
    @Path("/by-sea-level/{level}")
    open fun deleteBySeaLevel(@PathParam("level") level: Float): Response {
        return try {
            cities.removeIf { it.metersAboveSeaLevel == level }
            Response.status(Response.Status.NO_CONTENT).build()
        } catch (e: Exception) {
            Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("<error>Server error: ${e.message}</error>")
                .build()
        }
    }

    @GET
    //@Path("/seaLevelSum")
    @Path("/sea-level-sum")
    open fun getSeaLevelSum(): Response {
        return try {
            val sum = cities.sumOf { it.metersAboveSeaLevel.toDouble() }
            Response.ok(SeaLevelSumWrapper(sum)).build()
        } catch (e: Exception) {
            Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("<error>Server error: ${e.message}</error>")
                .build()
        }
    }

    @GET
    //@Path("/nameStartsWith/{prefix}")
    @Path("/name-starts-with/{prefix}")
    open fun getByNamePrefix(@PathParam("prefix") prefix: String): Response {
        return try {
            val filteredCities = cities.filter { it.name?.startsWith(prefix) == true }
            Response.ok(CityListWrapper(filteredCities)).build()
        } catch (e: Exception) {
            Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("<error>Server error: ${e.message}</error>")
                .build()
        }
    }
}
