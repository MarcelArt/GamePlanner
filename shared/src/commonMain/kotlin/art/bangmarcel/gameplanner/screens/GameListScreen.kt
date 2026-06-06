package art.bangmarcel.gameplanner.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import art.bangmarcel.gameplanner.repositories.GameRepo
import art.bangmarcel.gameplanner.viewmodels.GameListViewModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen

class GameListScreen(private val repo: GameRepo) : Screen {
    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel { GameListViewModel(repo) }
        val games by viewModel.gameState.collectAsStateWithLifecycle()

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(text = "GAMES", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { viewModel.create("Minecraft", "") }) {
                Text("Create New Game")
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(games) { game ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = game.name,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}