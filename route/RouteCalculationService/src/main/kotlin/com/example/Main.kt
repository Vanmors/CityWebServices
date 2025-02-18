package com.example

import com.example.controller.RouteSoapService
import com.example.controller.RouteSoapServiceImpl
import jakarta.ws.rs.ApplicationPath
import jakarta.ws.rs.core.Application
import jakarta.xml.ws.Endpoint

@ApplicationPath("")
open class JaxRsApplication : Application() {
    init {
        Endpoint.publish("http://localhost:8080/", RouteSoapServiceImpl::class) // todo(is it really needed?)
    }

}