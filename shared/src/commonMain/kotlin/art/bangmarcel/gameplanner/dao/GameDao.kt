package art.bangmarcel.gameplanner.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import art.bangmarcel.gameplanner.entitties.GameEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(game: GameEntity)

    @Query("select * from games")
    fun read(): Flow<List<GameEntity>>

    @Update
    suspend fun update(game: GameEntity)

    @Query("select * from games where id = :id")
    suspend fun getById(id: String): GameEntity?
}