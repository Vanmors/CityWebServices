
import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement
import java.io.Serializable

@XmlRootElement(name = "cities")
@XmlAccessorType(XmlAccessType.FIELD)
data class CityListWrapper(
    @field:XmlElement(name = "city")
    var cities: List<City> = mutableListOf()
) : Serializable {
    companion object {
        @Transient
        const val serialVersionUID: Long = 101L
    }
}