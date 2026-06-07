package art.bangmarcel.gameplanner.viewmodels

import androidx.compose.material3.Text
import art.bangmarcel.gameplanner.entitties.GameEntity
import art.bangmarcel.gameplanner.repositories.GameRepo
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.copyTo
import io.github.vinceglb.filekit.filesDir
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.path
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreateGameViewModel(private val repo: GameRepo) : ScreenModel {
    private val _isLoadingPicture = MutableStateFlow(false)
    val isLoadingPicture = _isLoadingPicture.asStateFlow()

    private val _srcPicture = MutableStateFlow("")
    val srcPicture = _srcPicture.asStateFlow()

    private val _destPicture = MutableStateFlow("")
    val destPicture = _destPicture.asStateFlow()

    fun copyPicture(file: PlatformFile) {
        screenModelScope.launch {
            _isLoadingPicture.value = true
            val newFile = PlatformFile(FileKit.filesDir, file.name)
            file.copyTo(newFile)
            _isLoadingPicture.value = false
        }
    }

    fun createGame(name: String, picture: PlatformFile?, onSuccess: () -> Unit) {
        screenModelScope.launch {
            var picturePath: String = ""
            if (picture != null) {
                val newFile = PlatformFile(FileKit.filesDir, picture.name)
                picture.copyTo(newFile)
                picturePath = newFile.path
            }

            repo.create(GameEntity(name = name, picture = picturePath))
            onSuccess()
        }
    }

    fun readPicture(file: PlatformFile?) {
        if (file == null) {
            _srcPicture.value = ""
            _destPicture.value = ""
            return
        }
        _srcPicture.value = file.path
        val newFile = PlatformFile(FileKit.filesDir, file.name)
        _destPicture.value = newFile.path
        println("${srcPicture.value} to ${destPicture.value}")
    }
}