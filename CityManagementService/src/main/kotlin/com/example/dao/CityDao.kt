package com.example.com.example.dao

import com.example.entity.City
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.Persistence


open class CityDao {

    private val emf: EntityManagerFactory = Persistence.createEntityManagerFactory("examplePU")


    open fun save(city: City): City {
        val em = emf.createEntityManager()
        val transaction = em.transaction
        transaction.begin()

        val sql = """
        INSERT INTO city (name, x, y, creationdate, area, population, metersabovesealevel, capital, climate, standardofliving, version, governor_id)
        VALUES (:name, :x, :y, :creationdate, :area, :population, :metersabovesealevel, :capital, :climate, :standardofliving, :version, :governor_id)
    """

        // Подготовка и выполнение нативного SQL-запроса
        val query = em.createNativeQuery(sql)
        query.setParameter("name", city.name)
        //query.setParameter("coordinates", city.coordinates?.let { "POINT(${it.x}, ${it.y})" })  // Преобразование координат в строку (если используется тип POINT в PostgreSQL)
        query.setParameter("x", city.coordinates?.x)
        query.setParameter("y", city.coordinates?.y)
        query.setParameter("creationdate", city.creationDate)
        query.setParameter("area", city.area)
        query.setParameter("population", city.population)
        query.setParameter("metersabovesealevel", city.metersAboveSeaLevel)
        query.setParameter("capital", city.capital)
        query.setParameter("climate", city.climate?.name)  // Сохраняем значение перечисления как строку
        query.setParameter("standardofliving", city.standardOfLiving?.name)  // Сохраняем значение перечисления как строку
        query.setParameter("version", city.version)  // Сохраняем значение перечисления как строку
        query.setParameter("governor_id", city.governor?.id)  // ID губернатора (если есть)

        query.executeUpdate()  // Выполнение запроса
        transaction.commit()

        // Возвращаем объект города после сохранения
        em.close()

        // Вернем сохраненный город, однако ID будет присвоен уже в базе данных, и JPA автоматически обновит объект.
        return city
    }

    fun update(city: City): City {
        val em = emf.createEntityManager()
        em.transaction.begin()

        val query = """
        UPDATE city
        SET name = :name, x = :x, y = :y, area = :area, 
            population = :population, metersabovesealevel = :metersabovesealevel, 
            capital = :capital, climate = :climate, standardofliving = :standardofliving, version = :version, 
            governor_id = :governor_id
        WHERE id = :id
    """

        em.createNativeQuery(query)
            .setParameter("name", city.name)
            .setParameter("x", city.coordinates?.x)
            .setParameter("y", city.coordinates?.y)
            .setParameter("area", city.area)
            .setParameter("population", city.population)
            .setParameter("metersabovesealevel", city.metersAboveSeaLevel)
            .setParameter("capital", city.capital)
            .setParameter("climate", city.climate?.name)  // Используем строковое значение из enum
            .setParameter("standardofliving", city.standardOfLiving?.name)  // Также строковое значение из enum
            .setParameter("version", city.version)
            .setParameter("governor_id", city.governor?.id)
            .setParameter("id", city.id)
            .executeUpdate()

        em.transaction.commit()
        em.close()

        return city
    }


    /*
    open fun save(city: City): City {
        val em = emf.createEntityManager()
        em.transaction.begin()
        //em.persist(city)

        val existingCity = if (city.id != null) {
            em.find(City::class.java, city.id) ?: city // Если не найден — это новый объект
        } else {
            city
        }


        //val newCity = em.merge(city)
        em.merge(city)
        em.transaction.commit()
        em.close()
        return city
    }

     */

    open fun findAll(): List<City> {
        val em = emf.createEntityManager()
        val cities = em.createQuery("SELECT c FROM City c", City::class.java).resultList
        em.close()
        return cities
    }

    fun findById(id: Long): City? {
        val em = emf.createEntityManager()
        val city = em.find(City::class.java, id)
        em.close()
        return city
    }

    fun delete(id: Long) {
        val em = emf.createEntityManager()
        em.transaction.begin()
        val city = em.find(City::class.java, id)
        if (city != null) {
            em.remove(city)  // Удаляет город по ID
        }
        em.transaction.commit()
        em.close()
    }

    fun deleteBySeaLevel(level: Float) {
        val em = emf.createEntityManager()
        em.transaction.begin()
        val citiesToDelete = em.createQuery("SELECT c FROM City c WHERE c.metersAboveSeaLevel = :level", City::class.java)
            .setParameter("level", level)
            .resultList
        citiesToDelete.forEach { em.remove(it) }  // Удаляет все города с данным уровнем моря
        em.transaction.commit()
        em.close()
    }

    fun getSeaLevelSum(): Double {
        val em = emf.createEntityManager()
        val query = em.createQuery("SELECT SUM(c.metersAboveSeaLevel) FROM City c", Double::class.java)
        val sum = query.singleResult
        em.close()
        return sum
    }

    fun findByNamePrefix(prefix: String): List<City> {
        val em = emf.createEntityManager()
        val query = em.createQuery("SELECT c FROM City c WHERE c.name LIKE :prefix", City::class.java)
            .setParameter("prefix", "$prefix%")
        val cities = query.resultList
        em.close()
        return cities
    }

}