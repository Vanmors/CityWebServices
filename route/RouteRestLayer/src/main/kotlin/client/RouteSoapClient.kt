package com.example.client

import com.example.ws.RouteSoapService
import jakarta.inject.Inject
import jakarta.xml.ws.Service
import jakarta.xml.ws.WebServiceClient
import java.net.MalformedURLException
import java.net.URL
import javax.xml.namespace.QName


@WebServiceClient
open class RouteSoapClient @Inject constructor() {

    //todo ИСПРАВИТЬ
    private val wsdlUrl = "http://10.5.0.7:8080/route-calculation-1.0-SNAPSHOT/RouteServiceSoapImpl?wsdl"
    private val nsUri = "http://com.example.controller/"
    private val localPart = "RouteServiceSoapImplService"

    open fun sendQueryOnCalculateDistanceBetweenOldestAndNewest(): Double {
        return getPort().calculateDistanceBetweenOldestAndNewest()
    }

    open fun sendQueryOnCalculateDistanceToOldest(): Double {
        return getPort().calculateDistanceToOldest()
    }

    open fun getPort(): RouteSoapService {
        var url: URL? = null
        try {
            url = URL(wsdlUrl)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }

        val qname = QName(nsUri, localPart)

        val service = Service.create(url, qname)

        return service.getPort(RouteSoapService::class.java)
    }
}