package managers.database.dao.models

import managers.database.base.HibEntity
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "table_teach_number", schema = "public")
class HibTeachNumber : Serializable, HibEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int = 0
    var number: Int = 0
}
