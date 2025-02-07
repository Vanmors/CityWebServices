import com.example.com.example.service.CityManipulatorServiceRemote
import com.example.com.example.entity.City
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import javax.naming.InitialContext
import javax.naming.NamingException

@ApplicationScoped
open class CityManipulationService @Inject constructor() {

    open val cityManipulatorServiceRemote = getFromEJBPool("ejb:/CityWebService-1.0-SNAPSHOT/CityManipulatorServiceImpl!com.example.com.example.service.CityManipulatorServiceRemote")
        //getFromEJBPool("ejb:/city-management/CityManipulatorServiceImpl!com.example.com.example.service.CityManipulatorServiceRemote")
    open fun getCities(sort: String?, filter: String?, page: Int?, size: Int?): ArrayList<City> {
        return cityManipulatorServiceRemote.getCities(sort, filter, page, size)
    }

    open fun addCity(city: City): City {
        return cityManipulatorServiceRemote.addCity(city)
    }

    open fun getCity(id: Long): City? {
        return cityManipulatorServiceRemote.getCity(id)
    }

    open fun updateCity(updatedCity: City): City? {
        return cityManipulatorServiceRemote.updateCity(updatedCity)
    }

    open fun deleteCity(id: Long) {
        return cityManipulatorServiceRemote.deleteCity(id)
    }

    open fun deleteBySeaLevel(level: Float) {
        return cityManipulatorServiceRemote.deleteBySeaLevel(level)
    }

    open fun getSeaLevelSum(): Double {
        return cityManipulatorServiceRemote.getSeaLevelSum()
    }

    open fun getByNamePrefix(prefix: String): List<City> {
        return cityManipulatorServiceRemote.getByNamePrefix(prefix)
    }

    @Throws(NamingException::class)
    open fun getFromEJBPool(name: String): CityManipulatorServiceRemote {
        return InitialContext().lookup(name) as CityManipulatorServiceRemote
    }
}