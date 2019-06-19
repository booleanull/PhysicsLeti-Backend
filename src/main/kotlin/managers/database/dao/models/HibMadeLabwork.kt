package managers.database.dao.models

import managers.database.base.HibEntity
import managers.labwork.models.MadeLabwork
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "table_made_labwork", schema = "public")
class HibMadeLabwork : Serializable, HibEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int = 0
    var labId: Int = 0
    @Column(length = 10000000)
    var protocol: String = ""
    @Column(length = 100000)
    var answer: String = ""
    var mark: String = ""

    fun toMadeLabwork() = MadeLabwork(id, labId, protocol, answer, mark)
}