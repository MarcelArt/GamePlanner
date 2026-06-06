package art.bangmarcel.gameplanner.viewmodels

import cafe.adriel.voyager.core.model.ScreenModel
import androidx.lifecycle.viewModelScope
import art.bangmarcel.gameplanner.entitties.GameEntity
import art.bangmarcel.gameplanner.repositories.GameRepo
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GameListViewModel(private val repo: GameRepo) : ScreenModel {
    val gameState: StateFlow<List<GameEntity>> = repo.read()
        .stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    fun create(name: String, picture: String) {
        screenModelScope.launch {
            repo.create(GameEntity(name = name, picture = picture))
        }
    }
}