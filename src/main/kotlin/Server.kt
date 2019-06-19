import controllers.accept.AcceptController
import controllers.auth.AuthController
import controllers.labwork.LabworkController
import controllers.madelabwork.MadeLabworkController
import controllers.reset.ResetController
import controllers.settings.SettingsController
import controllers.teach.TeachController
import di.DaggerApplicationComponent
import spark.Spark.port
import java.net.InetAddress
import java.text.SimpleDateFormat
import java.util.*

val daggerApplicationComponent = DaggerApplicationComponent
    .builder()
    .build()

fun main(args: Array<String>) {
    if (args.isEmpty() || args[0].toIntOrNull() == null) {
        println("Post is empty")
        return
    }

    initServer(args[0].toInt())

    val controllers = arrayListOf(
        AuthController(), ResetController(), AcceptController(), LabworkController(),
        MadeLabworkController(), TeachController(), SettingsController()
    )
    controllers.forEach {
        it.start()
    }
}

fun initServer(port: Int) {
    val sdf = SimpleDateFormat("hh:mm dd/MM/yyyy")
    val date = sdf.format(Date())
    val version = String::class.java.getResource("/version.txt").readText()
    val ip = InetAddress.getLocalHost()
    port(port)

    println("=======================================")
    println("Port: $port")
    println("IP address: ${ip.hostAddress}")
    println("Version: $version")
    println("Date: $date")
    println("=======================================\n")
}
