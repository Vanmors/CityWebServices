
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import java.time.LocalDate

@Path("/cities")
@Produces(MediaType.APPLICATION_XML)
@Consumes(MediaType.APPLICATION_XML)
class CityResource {

    private val cities = mutableListOf<City>()

    @POST
    fun addCity(city: City): Response {
        city.id = (cities.size + 1).toLong()  // Генерация ID
        city.creationDate = LocalDate.now()  // Автоматическая установка даты создания
        cities.add(city)
        return Response.status(Response.Status.CREATED).entity(city).build()
    }

    @GET
    @Path("/{id}")
    fun getCity(@PathParam("id") id: Long): Response {
        val city = cities.find { it.id == id }
        return if (city != null) {
            Response.ok(city).build()
        } else {
            Response.status(Response.Status.NOT_FOUND).build()
        }
    }

    @PUT
    @Path("/{id}")
    fun updateCity(@PathParam("id") id: Long, updatedCity: City): Response {
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
    }

    @DELETE
    @Path("/{id}")
    fun deleteCity(@PathParam("id") id: Long): Response {
        val city = cities.find { it.id == id }
        return if (city != null) {
            cities.remove(city)
            Response.noContent().build()
        } else {
            Response.status(Response.Status.NOT_FOUND).build()
        }
    }

    @GET
    fun getCities(
        @QueryParam("sort") sort: String?,
        @QueryParam("page") page: Int?,
        @QueryParam("size") size: Int?
    ): Response {
        // Логика сортировки и пагинации
        val sortedCities = cities // Примените фильтры и сортировку здесь
        return Response.ok(sortedCities).build()
    }

    @DELETE
    @Path("/bySeaLevel/{level}")
    fun deleteBySeaLevel(@PathParam("level") level: Float): Response {
        cities.removeIf { it.metersAboveSeaLevel == level }
        return Response.noContent().build()
    }

    @GET
    @Path("/nameStartsWith/{prefix}")
    fun getByNamePrefix(@PathParam("prefix") prefix: String): Response {
        val filteredCities = cities.filter { it.name?.startsWith(prefix) == true }
        return Response.ok(filteredCities).build()
    }

    @GET
    @Path("/seaLevelSum")
    fun getSeaLevelSum(): Response {
        val sum = cities.sumOf { it.metersAboveSeaLevel.toDouble() }
        return Response.ok(sum).build()
    }
}
