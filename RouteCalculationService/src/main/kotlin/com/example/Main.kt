package com.example

import jakarta.ws.rs.ApplicationPath
import jakarta.ws.rs.core.Application

@ApplicationPath("/api")
open class JaxRsApplication : Application()