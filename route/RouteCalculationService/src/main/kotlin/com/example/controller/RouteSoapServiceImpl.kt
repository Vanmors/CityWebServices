package com.example.controller

import com.example.entity.*
import com.example.service.RouteService
import jakarta.inject.Inject
import jakarta.jws.WebMethod
import jakarta.jws.WebService
import jakarta.xml.soap.SOAPFactory
import jakarta.xml.ws.WebServiceException
import jakarta.xml.ws.soap.SOAPFaultException

@WebService(endpointInterface = "com.example.controller.RouteSoapService")
open class RouteSoapServiceImpl : RouteSoapService {

    init {
        println("### RouteSoapServiceImpl загружен ###")
    }

    constructor()

    @Inject
    private lateinit var routeService: RouteService

    private val citiesApiUrl = "http://city-management-service:8080/CityWebService-1.0-SNAPSHOT/api/cities"

    @WebMethod
    override fun calculateDistanceBetweenOldestAndNewest(): Double {
        return try {
            val cities = fetchCities()
            val (oldestCity, newestCity) = findOldestAndNewestCities(cities)

            if (oldestCity?.coordinates == null || newestCity?.coordinates == null) {
                throw WebServiceException("Cannot calculate distance: insufficient city data")
            }

            val distance =
                routeService.calculateDistanceBetweenOldestAndNewest(oldestCity.coordinates!!, newestCity.coordinates!!)
            distance
        } catch (e: Exception) {
            throw createSOAPFaultException("Server error: ${e.message}")
        }

    }

    @WebMethod
    override fun calculateDistanceToOldest(): Double {
        return try {
            val cities = fetchCities()
            val (oldestCity, _) = findOldestAndNewestCities(cities)

            if (oldestCity?.coordinates == null) {
                throw WebServiceException("Cannot calculate distance: insufficient city data")
            }

            val distance =
                routeService.calculateDistanceBetweenOldestAndNewest(oldestCity.coordinates!!, Coordinates(0.0, 0))
            distance
        } catch (e: Exception) {
            throw createSOAPFaultException("Server error: ${e.message}")
        }
    }

    /*
    private fun fetchCities(): List<City> {
        val client = jakarta.ws.rs.client.ClientBuilder.newClient()
        val response = client.target(citiesApiUrl)
            .request(jakarta.ws.rs.core.MediaType.APPLICATION_XML)
            .get(jakarta.ws.rs.core.Response::class.java)

        if (response.status != 200) {
            throw WebServiceException("Failed to fetch cities. HTTP status: ${response.status}")
        }

        val cityListWrapper = response.readEntity(CityListWrapper::class.java)
        client.close()

        if (cityListWrapper.cities.isEmpty()) {
            throw WebServiceException("No cities found")
        }

        return cityListWrapper.cities
    }

     */

    private fun fetchCities(): List<City> {
        val cityListWrapper = CityListWrapper(
            cities = listOf(
                City(
                    id = 1,
                    name = "New York",
                    coordinates = Coordinates(40.7128, -74),
                    creationDate = "1624-01-01",
                    area = 783L,
                    population = 8419600,
                    metersAboveSeaLevel = 10.0f,
                    capital = false,
                    climate = Climate.HUMIDCONTINENTAL,
                    standardOfLiving = StandardOfLiving.VERY_HIGH,
                    governor = Human(birthday = "1970-05-15")
                ),
                City(
                    id = 2,
                    name = "Paris",
                    coordinates = Coordinates(48.8566, 2),
                    creationDate = "300-01-01",
                    area = 105L,
                    population = 2148000,
                    metersAboveSeaLevel = 35.0f,
                    capital = true,
                    climate = Climate.OCEANIC,
                    standardOfLiving = StandardOfLiving.ULTRA_HIGH,
                    governor = Human(birthday = "1965-09-23")
                ),
                City(
                    id = 3,
                    name = "Tokyo",
                    coordinates = Coordinates(35.6895, 139),
                    creationDate = "1603-01-01",
                    area = 2194L,
                    population = 13929000,
                    metersAboveSeaLevel = 40.0f,
                    capital = true,
                    climate = Climate.MONSOON,
                    standardOfLiving = StandardOfLiving.ULTRA_HIGH,
                    governor = Human(birthday = "1980-12-11")
                )
            )
        )

        return cityListWrapper.cities
    }


    private fun findOldestAndNewestCities(cities: List<City>): Pair<City?, City?> {
        val dateFormatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dateTimeFormatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        fun parseDate(dateStr: String?): java.time.LocalDateTime? {
            return try {
                if (dateStr == null) null
                else if (dateStr.contains(" ")) java.time.LocalDateTime.parse(dateStr, dateTimeFormatter)
                else java.time.LocalDate.parse(dateStr, dateFormatter).atStartOfDay()
            } catch (e: Exception) {
                null
            }
        }

        val oldestCity = cities.filter { parseDate(it.creationDate) != null }
            .minByOrNull { parseDate(it.creationDate)!! }

        val newestCity = cities.filter { parseDate(it.creationDate) != null }
            .maxByOrNull { parseDate(it.creationDate)!! }

        return Pair(oldestCity, newestCity)
    }

    private fun createSOAPFaultException(message: String): SOAPFaultException {
        val soapFactory = SOAPFactory.newInstance()
        val fault = soapFactory.createFault(message, javax.xml.namespace.QName("http://com.example.controller/", "RouteSoapService"))
        return SOAPFaultException(fault)
    }
}
