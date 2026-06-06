package art.bangmarcel.gameplanner.entitties

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.uuid.Uuid

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey val id: String = Uuid.random().toString(),
    val name: String,
    val picture: String,
)
