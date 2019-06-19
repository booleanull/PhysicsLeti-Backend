package managers.database.dao.models

import managers.database.base.HibEntity
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "table_teach", schema = "public")
class HibTeach : Serializable, HibEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int = 0
    var teacher: Int = 0
    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    var groupNumber = mutableSetOf<HibTeachNumber>()
}