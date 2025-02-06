import com.example.com.example.service.CityManipulatorServiceRemote
import com.example.entity.City
import jakarta.enterprise.context.ApplicationScoped
import javax.naming.InitialContext
import javax.naming.NamingException

@ApplicationScoped
class CityManipulationService {

    private val cityManipulatorServiceRemote = getFromEJBPool("ejb:/city-management/CityManipulatorServiceImpl!com.example.service.CityManipulatorServiceRemote")

    fun getCities(sort: String?, filter: String?, page: Int?, size: Int?): List<City> {
        return cityManipulatorServiceRemote.getCities(sort, filter, page, size)
    }

    fun addCity(city: City): City {
        return cityManipulatorServiceRemote.addCity(city)
    }

    fun getCity(id: Long): City? {
        return cityManipulatorServiceRemote.getCity(id)
    }

    fun updateCity(updatedCity: City): City? {
        return cityManipulatorServiceRemote.updateCity(updatedCity)
    }

    fun deleteCity(id: Long) {
        return cityManipulatorServiceRemote.deleteCity(id)
    }

    fun deleteBySeaLevel(level: Float) {
        return cityManipulatorServiceRemote.deleteBySeaLevel(level)
    }

    fun getSeaLevelSum(): Double {
        return cityManipulatorServiceRemote.getSeaLevelSum()
    }

    fun getByNamePrefix(prefix: String): List<City> {
        return cityManipulatorServiceRemote.getByNamePrefix(prefix)
    }

    @Throws(NamingException::class)
    private fun getFromEJBPool(name: String): CityManipulatorServiceRemote {
        return InitialContext().lookup(name) as CityManipulatorServiceRemote
    }
}