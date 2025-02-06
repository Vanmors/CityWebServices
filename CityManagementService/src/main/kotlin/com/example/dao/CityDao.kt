package com.example.com.example.dao

import com.example.entity.City
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.Persistence


class CityDao {

    private val emf: EntityManagerFactory = Persistence.createEntityManagerFactory("examplePU")

    fun save(city: City): City {
        val em = emf.createEntityManager()
        em.transaction.begin()
        em.persist(city)
        em.transaction.commit()
        em.close()
        return city
    }

    fun findAll(): List<City> {
        val em = emf.createEntityManager()
        val cities = em.createQuery("SELECT c FROM City c", City::class.java).resultList
        em.close()
        return cities
    }
}