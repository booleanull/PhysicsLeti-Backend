package managers.database.dao.models

import managers.database.base.HibEntity
import managers.user.models.User
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "table_users", schema = "public")
class HibUser : Serializable, HibEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int = 0
    var token: String = ""
    var approve: Boolean = false
    var restored: Boolean = false
    var email: String = ""
    var password: String = ""
    var firstName: String = ""
    var lastName: String = ""
    var type: Int = 0
    var groupNumber: Int? = null
    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    var labworks: MutableList<HibMadeLabwork> = mutableListOf()

    fun toUser() = User(
        id,
        token,
        approve,
        restored,
        email,
        password,
        firstName,
        lastName,
        type,
        if (groupNumber != 0) groupNumber else null,
        labworks.map { it.toMadeLabwork() })
}