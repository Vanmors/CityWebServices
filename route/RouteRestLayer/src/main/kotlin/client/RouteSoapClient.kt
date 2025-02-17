package com.example.client

import com.example.ws.RouteServiceSoap
import jakarta.inject.Inject
import jakarta.xml.ws.Service
import jakarta.xml.ws.WebServiceClient
import java.net.MalformedURLException
import java.net.URL
import javax.xml.namespace.QName


@WebServiceClient
open class RouteSoapClient @Inject constructor() {

    //todo ИСПРАВИТЬ
    private val wsdlUrl = "http://10.5.0.7:8081/web-module-1.0-SNAPSHOT/MovieServiceSoapImpl?wsdl"
    private val nsUri = "http://controllers.ru.ifmo.se/"
    private val localPart = "MovieServiceSoapImplService"

    open fun sendQueryOnCalculateDistanceBetweenOldestAndNewest(): Double {
        return getPort().calculateDistanceBetweenOldestAndNewest()
    }

    open fun sendQueryOnCalculateDistanceToOldest(): Double {
        return getPort().calculateDistanceToOldest()
    }

    open fun getPort(): RouteServiceSoap {
        var url: URL? = null
        try {
            url = URL(wsdlUrl)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }

        val qname = QName(nsUri, localPart)

        val service = Service.create(url, qname)

        return service.getPort(RouteServiceSoap::class.java)
    }
}