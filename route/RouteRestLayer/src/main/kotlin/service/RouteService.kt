package com.example.service

import com.example.client.RouteSoapClient
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

@ApplicationScoped
open class RouteService @Inject constructor() {
    @Inject
    private lateinit var client: RouteSoapClient

    open fun handleCalculateDistanceBetweenOldestAndNewest(): Double {
        return client.sendQueryOnCalculateDistanceBetweenOldestAndNewest()
    }

    open fun handleCalculateDistanceToOldest(): Double {
        return client.sendQueryOnCalculateDistanceToOldest()
    }
}