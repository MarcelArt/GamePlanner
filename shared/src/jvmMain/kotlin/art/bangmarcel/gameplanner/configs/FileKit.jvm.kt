package art.bangmarcel.gameplanner.configs

import io.github.vinceglb.filekit.FileKit
import java.io.File

actual fun newFileKit() {
    val appDir = File(System.getProperty("user.home"), ".craftingplanner")
    FileKit.init(
        appId = "GamePlanner",
        filesDir = File(appDir, "database"),
        cacheDir = File(appDir, "cache"),
    )
    println("JVM FileKit initialized")
}