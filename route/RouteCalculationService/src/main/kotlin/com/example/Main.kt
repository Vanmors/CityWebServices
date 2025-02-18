package com.example

import com.example.controller.RouteSoapService
import jakarta.ws.rs.ApplicationPath
import jakarta.ws.rs.core.Application
import jakarta.xml.ws.Endpoint

@ApplicationPath("/api")
open class JaxRsApplication : Application() {

//    init {
//        Endpoint.publish("http/????", RouteSoapService::class) // todo(is it really need?)
//    }

}