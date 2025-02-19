package com.example

import com.example.controller.RouteSoapServiceImpl
import jakarta.ws.rs.core.Application
import jakarta.xml.ws.Endpoint

open class JaxRsApplication {
    init {
        Endpoint.publish("http://localhost:8080/", RouteSoapServiceImpl())
    }
}