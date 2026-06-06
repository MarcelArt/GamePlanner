package art.bangmarcel.gameplanner.database

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val platformDataDir = File(System.getProperty("user.home"), ".craftingplanner")
    if (!platformDataDir.exists()) platformDataDir.mkdirs()

    val dbFile = File(platformDataDir, "sqlite.db")
    return Room.databaseBuilder<AppDatabase>(name = dbFile.absolutePath)
}