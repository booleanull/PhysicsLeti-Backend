package managers.database.dao.models

import managers.database.base.HibEntity
import managers.labwork.models.Labwork
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "table_labwork", schema = "public")
class HibLabwork : Serializable, HibEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int = 0
    var title: String = ""
    @Column(length = 10000000)
    var description: String = ""
    var theme: String = ""
    @Column(length = 100000)
    var protocol: String = ""
    var link: String = ""

    fun toLabwork() = Labwork(id, title, description, theme, protocol, link)
}