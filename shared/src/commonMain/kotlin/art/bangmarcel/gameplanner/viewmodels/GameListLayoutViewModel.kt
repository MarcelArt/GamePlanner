package art.bangmarcel.gameplanner.viewmodels

import art.bangmarcel.gameplanner.entitties.GameEntity
import art.bangmarcel.gameplanner.enums.ViewModelStatus
import art.bangmarcel.gameplanner.repositories.GameRepo
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlin.collections.emptyList

class GameListLayoutViewModel(private val gRepo: GameRepo): ScreenModel {
    private val _status = MutableStateFlow(ViewModelStatus.NONE)
    val status = _status.asStateFlow()

    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    val data: StateFlow<List<GameEntity>> = gRepo.read()
        .stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )
}