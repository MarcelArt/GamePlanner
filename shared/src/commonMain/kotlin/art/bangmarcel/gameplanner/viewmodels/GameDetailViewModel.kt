package art.bangmarcel.gameplanner.viewmodels

import art.bangmarcel.gameplanner.entitties.GameEntity
import art.bangmarcel.gameplanner.enums.ViewModelStatus
import art.bangmarcel.gameplanner.repositories.GameRepo
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameDetailViewModel(private val gRepo: GameRepo): ScreenModel {
    private val _status = MutableStateFlow(ViewModelStatus.NONE)
    val status = _status.asStateFlow()

    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    fun update(game: GameEntity, onError: (String) -> Unit, onSuccess: () -> Unit) {
        _status.value = ViewModelStatus.PENDING
        screenModelScope.launch {
            try {
                gRepo.update(game)
                _status.value = ViewModelStatus.SUCCESS
                onSuccess()
            }
            catch (e: Exception) {
                _error.value = e.message ?: "unexpected error"
                _status.value = ViewModelStatus.ERROR
                onError(_error.value)
            }
        }
    }
}