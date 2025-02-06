package com.example.config

import com.google.common.net.HostAndPort
import com.orbitz.consul.AgentClient
import com.orbitz.consul.Consul
import com.orbitz.consul.model.agent.ImmutableRegistration
import com.orbitz.consul.model.agent.Registration
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import jakarta.ejb.Singleton
import jakarta.ejb.Startup

@Startup
@Singleton
open class ConsulConfig {

    private val serviceId = "city-management-service"
    private lateinit var client: Consul
    private lateinit var agentClient: AgentClient

    @PostConstruct
    open fun init() {
        client = Consul.builder()
            //.withHostAndPort(HostAndPort.fromParts("consul", 8500))
            .withHostAndPort(HostAndPort.fromParts("localhost", 8500))
            .build()
        agentClient = client.agentClient()

        val service: Registration = ImmutableRegistration.builder()
            .id(serviceId)
            .name(serviceId)
            .port(8080)
            //.address("city-management-service")
            .address("localhost")
            .tags(listOf("city-management-service"))
            .meta(mapOf("version" to "1.0"))
            .build()

        agentClient.register(service)
    }

    @PreDestroy
    open fun deregisterService() {
        agentClient.deregister(serviceId)
    }
}