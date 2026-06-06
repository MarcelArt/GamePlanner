package art.bangmarcel.gameplanner.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Label
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.unit.dp
import art.bangmarcel.gameplanner.Platform
import art.bangmarcel.gameplanner.repositories.GameRepo
import art.bangmarcel.gameplanner.viewmodels.CreateGameViewModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import gameplanner.shared.generated.resources.Res
import gameplanner.shared.generated.resources.arrow_back_ios_new_24px
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.absolutePath
import io.github.vinceglb.filekit.copyTo
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.filesDir
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.readBytes
import org.jetbrains.compose.resources.painterResource

class CreateGameScreen(private val repo: GameRepo): Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var name by remember { mutableStateOf("") }
        val coroutineScope = rememberCoroutineScope()
        var statusMessage by remember { mutableStateOf("No file selected") }
        val viewModel = rememberScreenModel { CreateGameViewModel(repo) }
        val destPicture by viewModel.destPicture.collectAsState()
        val srcPicture by viewModel.srcPicture.collectAsState()

        val launcher = rememberFilePickerLauncher(type = FileKitType.Image) { file ->
            viewModel.readPicture(file)
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Create Game") },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                painterResource(Res.drawable.arrow_back_ios_new_24px),
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            },
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Button(onClick = {launcher.launch()}) {
                    Text("Open Gallery")
                }
                Text("$srcPicture to $destPicture")
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = name,
                    label = { Text("Game Title") },
                    onValueChange = { name = it }
                )
            }
        }

    }

}