package com.example.controller

import com.example.entity.City
import com.example.entity.CityListWrapper
import com.example.entity.Coordinates
import com.example.service.RouteService
import jakarta.inject.Inject
import jakarta.jws.WebMethod
import jakarta.jws.WebService
import jakarta.xml.soap.SOAPFactory
import jakarta.xml.ws.WebServiceException
import jakarta.xml.ws.soap.SOAPFaultException

@WebService
class RouteSoapService {

    @Inject
    private lateinit var routeService: RouteService

    private val citiesApiUrl = "http://city-management-service:8080/CityWebService-1.0-SNAPSHOT/api/cities"

    @WebMethod
    fun calculateDistanceBetweenOldestAndNewest(): String {
        return try {
            val cities = fetchCities()
            val (oldestCity, newestCity) = findOldestAndNewestCities(cities)

            if (oldestCity?.coordinates == null || newestCity?.coordinates == null) {
                throw WebServiceException("Cannot calculate distance: insufficient city data")
            }

            val distance =
                routeService.calculateDistanceBetweenOldestAndNewest(oldestCity.coordinates!!, newestCity.coordinates!!)
            "<distance>$distance</distance>"
        } catch (e: Exception) {
            throw createSOAPFaultException("Server error: ${e.message}")
        }

    }

    @WebMethod
    fun calculateDistanceToOldest(): String {
        return try {
            val cities = fetchCities()
            val (oldestCity, _) = findOldestAndNewestCities(cities)

            if (oldestCity?.coordinates == null) {
                throw WebServiceException("Cannot calculate distance: insufficient city data")
            }

            val distance =
                routeService.calculateDistanceBetweenOldestAndNewest(oldestCity.coordinates!!, Coordinates(0.0, 0))
            "<distance>$distance</distance>"
        } catch (e: Exception) {
            throw createSOAPFaultException("Server error: ${e.message}")
        }
    }

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
        val fault = soapFactory.createFault(message, javax.xml.namespace.QName("http://example.com", "RouteServiceError"))
        return SOAPFaultException(fault)
    }
}
