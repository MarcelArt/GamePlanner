package art.bangmarcel.gameplanner.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import art.bangmarcel.gameplanner.entitties.GameEntity
import art.bangmarcel.gameplanner.screens.game.GameLayoutScreen
import art.bangmarcel.gameplanner.viewmodels.GameListLayoutViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import gameplanner.shared.generated.resources.Res
import gameplanner.shared.generated.resources.add_24px
import org.jetbrains.compose.resources.painterResource

class GameListLayoutScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<GameListLayoutViewModel>()
        val games by viewModel.data.collectAsStateWithLifecycle()
        val parentNavigator = LocalNavigator.currentOrThrow

        var searchQuery by remember { mutableStateOf("") }
        val filteredGames = remember(games, searchQuery) {
            games.filter { it.name.contains(searchQuery, ignoreCase = true) }
        }

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val isWideScreen = maxWidth >= 600.dp

            if (isWideScreen) {
                // Desktop Split-Pane Layout
                var selectedGameId by remember { mutableStateOf<String?>(null) }
                var previousGames by remember { mutableStateOf<List<GameEntity>>(emptyList()) }

                LaunchedEffect(games) {
                    if (selectedGameId == "CREATE_NEW" && games.size > previousGames.size) {
                        val newGame = games.firstOrNull { g -> previousGames.none { it.id == g.id } }
                        if (newGame != null) {
                            selectedGameId = newGame.id
                        }
                    }
                    previousGames = games
                }

                Row(modifier = Modifier.fillMaxSize()) {
                    // Sidebar
                    Column(
                        modifier = Modifier
                            .width(280.dp)
                            .fillMaxHeight()
                            .background(MaterialTheme.colorScheme.surfaceContainerLow)
                    ) {
                        // Title
                        Text(
                            text = "GAME TRACKERS",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.padding(
                                start = 16.dp,
                                end = 16.dp,
                                top = 20.dp,
                                bottom = 4.dp
                            )
                        )

                        // Search box
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search games...") },
                            singleLine = true,
                            textStyle = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            shape = MaterialTheme.shapes.small,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                            )
                        )

                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                        // Games List
                        if (filteredGames.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (searchQuery.isEmpty()) "No games yet" else "No matching games",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            ) {
                                items(filteredGames) { game ->
                                    SidebarGameItem(
                                        game = game,
                                        isSelected = game.id == selectedGameId,
                                        onClick = { selectedGameId = game.id }
                                    )
                                }
                            }
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                        // Add Game Button at bottom of sidebar
                        Button(
                            onClick = { selectedGameId = "CREATE_NEW" },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            shape = MaterialTheme.shapes.small
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.add_24px),
                                contentDescription = "Add Game",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Add Game", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                        }
                    }

                    // Vertical Divider
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                            .background(MaterialTheme.colorScheme.outlineVariant)
                    )

                    // Right detail pane (nested navigator)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        Navigator(NotSelectedGameScreen()) { nestedNavigator ->
                            LaunchedEffect(selectedGameId, games) {
                                when (selectedGameId) {
                                    null -> nestedNavigator.replaceAll(NotSelectedGameScreen())
                                    "CREATE_NEW" -> {
                                        if (nestedNavigator.lastItem !is CreateGameScreen) {
                                            nestedNavigator.push(CreateGameScreen())
                                        }
                                    }
                                    else -> {
                                        val currentSelected = games.find { it.id == selectedGameId }
                                        if (currentSelected != null) {
                                            nestedNavigator.replaceAll(GameLayoutScreen(currentSelected))
                                        } else {
                                            nestedNavigator.replaceAll(NotSelectedGameScreen())
                                        }
                                    }
                                }
                            }

                            // Synchronize backstack pop state to update parent selectedGameId
                            val currentScreen = nestedNavigator.lastItem
                            LaunchedEffect(currentScreen) {
                                if (selectedGameId == "CREATE_NEW" && currentScreen !is CreateGameScreen) {
                                    selectedGameId = (currentScreen as? GameLayoutScreen)?.game?.id
                                }
                            }

                            CurrentScreen()
                        }
                    }
                }
            } else {
                // Mobile Stacked List Layout
                Scaffold(
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { parentNavigator.push(CreateGameScreen()) },
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.add_24px),
                                contentDescription = "Add game"
                            )
                        }
                    }
                ) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(paddingValues)
                    ) {
                        // Title
                        Text(
                            text = "GAMES",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(
                                start = 16.dp,
                                end = 16.dp,
                                top = 20.dp,
                                bottom = 8.dp
                            )
                        )

                        // Search box
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search games...") },
                            singleLine = true,
                            textStyle = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            shape = MaterialTheme.shapes.small,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                            )
                        )

                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                        // Games List
                        if (filteredGames.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (searchQuery.isEmpty()) "No games yet. Tap '+' to create one." else "No matching games",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(filteredGames) { game ->
                                    SidebarGameItem(
                                        game = game,
                                        isSelected = false,
                                        onClick = { parentNavigator.push(GameLayoutScreen(game)) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun SidebarGameItem(
        game: GameEntity,
        isSelected: Boolean,
        onClick: () -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable(onClick = onClick)
                .background(
                    if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)
                    else Color.Transparent
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Selected indicator line on the left
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(4.dp)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else Color.Transparent
                    )
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Game Image / Placeholder
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                contentAlignment = Alignment.Center
            ) {
                if (game.picture.isNotEmpty()) {
                    AsyncImage(
                        model = game.picture,
                        contentDescription = game.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = "\uD83C\uDFAE",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = game.name,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                ),
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp)
            )
        }
    }
}
