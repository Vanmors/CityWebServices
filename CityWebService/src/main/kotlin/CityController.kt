import com.example.entity.City
import com.example.entity.CityListWrapper
import com.example.entity.SeaLevelSumWrapper
import com.example.service.CityValidator
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response

@Path("/cities")
@Produces(MediaType.APPLICATION_XML)
@Consumes(MediaType.APPLICATION_XML)
open class CityResource {

    @Inject
    lateinit var cityManipulationService: CityManipulationService

    @GET
    open fun getCities(
        @QueryParam("sort") sort: String?,
        @QueryParam("filter") filter: String?,
        @QueryParam("page") @DefaultValue("1") page: Int?,
        @QueryParam("size") size: Int?
    ): Response {
        return try {
            Response.ok(CityListWrapper(cityManipulationService.getCities(sort, filter, page, size)))
                .build()
        } catch (e: BadRequestException) {
            Response.status(Response.Status.BAD_REQUEST).build()
        } catch (e: Exception) {
            Response.status(Response.Status.INTERNAL_SERVER_ERROR)
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

            val newCity = cityManipulationService.addCity(city)
            return Response.status(Response.Status.CREATED).entity(newCity).build()
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
            val city = cityManipulationService.getCity(id)
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

            val city = cityManipulationService.getCity(id)
            return if (city != null) {
                cityManipulationService.updateCity(updatedCity)
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
            val city = cityManipulationService.getCity(id)
            if (city != null) {
                cityManipulationService.deleteCity(id)
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
    @Path("/by-sea-level/{level}")
    open fun deleteBySeaLevel(@PathParam("level") level: Float): Response {
        return try {
            cityManipulationService.deleteBySeaLevel(level)
            Response.status(Response.Status.NO_CONTENT).build()
        } catch (e: Exception) {
            Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("<error>Server error: ${e.message}</error>")
                .build()
        }
    }

    @GET
    @Path("/sea-level-sum")
    open fun getSeaLevelSum(): Response {
        return try {
            val sum = cityManipulationService.getSeaLevelSum()
            Response.ok(SeaLevelSumWrapper(sum)).build()
        } catch (e: Exception) {
            Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("<error>Server error: ${e.message}</error>")
                .build()
        }
    }

    @GET
    @Path("/name-starts-with/{prefix}")
    open fun getByNamePrefix(@PathParam("prefix") prefix: String): Response {
        return try {
            val filteredCities = cityManipulationService.getByNamePrefix(prefix)
            Response.ok(CityListWrapper(filteredCities)).build()
        } catch (e: Exception) {
            Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("<error>Server error: ${e.message}</error>")
                .build()
        }
    }

}
