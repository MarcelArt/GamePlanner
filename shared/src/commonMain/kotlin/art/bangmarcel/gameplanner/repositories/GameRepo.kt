package art.bangmarcel.gameplanner.repositories

import art.bangmarcel.gameplanner.dao.GameDao
import art.bangmarcel.gameplanner.entitties.GameEntity
import kotlinx.coroutines.flow.Flow

class GameRepo(private val dao: GameDao) {
    fun read(): Flow<List<GameEntity>> = dao.read()

    suspend fun create(game: GameEntity) {
        dao.create(game)
    }

    suspend fun update(game: GameEntity) {
        dao.update(game)
    }

    suspend fun getById(id: String): GameEntity? {
        return dao.getById(id)
    }
}