import service.CityManipulator
import service.CityValidator
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import java.time.LocalDate

@Path("/cities")
@Produces(MediaType.APPLICATION_XML)
@Consumes(MediaType.APPLICATION_XML)
class CityResource {

    private val cities = mutableListOf<City>()

    @GET
    fun getCities(
        @QueryParam("sort") sort: String?,
        @QueryParam("filter") filter: String?,
        @QueryParam("page") @DefaultValue("1") page: Int?,
        @QueryParam("size") @DefaultValue("1") size: Int?
    ): Response {
        try {
            var result = cities.toList()

            if (filter != null) {
                try {
                    result = CityManipulator.applyFilter(result, filter)
                } catch (e: BadRequestException) {
                    return Response.status(Response.Status.BAD_REQUEST).build()
                }
            }

            result = CityManipulator.applyPagination(result, page!!, size!!)

            if (sort != null) {
                result = CityManipulator.applySort(result, sort)
            }

            return Response.ok(result).build()
        } catch (e: Exception) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("<error>Server error: ${e.message}</error>")
                .build()
        }
    }

    @POST
    fun addCity(city: City): Response {
        try {
            val validationErrors = CityValidator.validateCity(city)
            if (validationErrors.isNotEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("<error>${validationErrors.joinToString(", ")}</error>")
                    .build()
            }

            city.id = (cities.size + 1).toLong()
            city.creationDate = LocalDate.now()
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
    fun getCity(@PathParam("id") id: Long): Response {
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
    fun updateCity(@PathParam("id") id: Long, updatedCity: City): Response {
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
    fun deleteCity(@PathParam("id") id: Long): Response {
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
    @Path("/bySeaLevel/{level}")
    fun deleteBySeaLevel(@PathParam("level") level: Float): Response {
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
    @Path("/seaLevelSum")
    fun getSeaLevelSum(): Response {
        return try {
            val sum = cities.sumOf { it.metersAboveSeaLevel.toDouble() }
            Response.ok(sum).build()
        } catch (e: Exception) {
            Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("<error>Server error: ${e.message}</error>")
                .build()
        }
    }

    @GET
    @Path("/nameStartsWith/{prefix}")
    fun getByNamePrefix(@PathParam("prefix") prefix: String): Response {
        return try {
            val filteredCities = cities.filter { it.name?.startsWith(prefix) == true }
            Response.ok(filteredCities).build()
        } catch (e: Exception) {
            Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("<error>Server error: ${e.message}</error>")
                .build()
        }
    }
}
