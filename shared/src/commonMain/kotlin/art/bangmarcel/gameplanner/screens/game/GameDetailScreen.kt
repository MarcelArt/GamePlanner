package art.bangmarcel.gameplanner.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import art.bangmarcel.gameplanner.components.ImagePickerComponent
import art.bangmarcel.gameplanner.entitties.GameEntity
import art.bangmarcel.gameplanner.viewmodels.GameDetailViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import coil3.compose.AsyncImage
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.copyTo
import io.github.vinceglb.filekit.filesDir
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.path
import kotlinx.coroutines.launch

class GameDetailScreen(private val game: GameEntity) : Screen {
    @Composable
    override fun Content() {
        val scrollState = rememberScrollState()
        val viewModel = koinScreenModel<GameDetailViewModel>()
        val coroutineScope = rememberCoroutineScope()

        var currentGame by remember { mutableStateOf(game) }
        var isEditing by remember { mutableStateOf(false) }
        var editName by remember(isEditing) { mutableStateOf(currentGame.name) }
        var editPicture by remember(isEditing) { mutableStateOf<PlatformFile?>(null) }
        var errorMessage by remember { mutableStateOf("") }
        var isSaving by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 640.dp)
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(24.dp)
            ) {
                // Header / Game Title and Edit Button Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isEditing) "EDIT DETAILS" else currentGame.name.uppercase(),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    if (!isEditing) {
                        TextButton(
                            onClick = {
                                errorMessage = ""
                                isEditing = true
                            },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text("Edit", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                        }
                    }
                }

                if (isEditing) {
                    // Cover Image Upload
                    Text(
                        text = "GAME COVER IMAGE",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    ImagePickerComponent(
                        selectedFile = editPicture,
                        onFileSelected = { editPicture = it },
                        initialImageUrl = currentGame.picture,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Game Title Input
                    Text(
                        text = "GAME TITLE",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = editName,
                        label = { Text("Game Title") },
                        onValueChange = { editName = it },
                        singleLine = true,
                        shape = MaterialTheme.shapes.small,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

                    // Action buttons (Cancel / Save)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = { isEditing = false },
                            enabled = !isSaving
                        ) {
                            Text("Cancel")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                if (editName.isBlank()) {
                                    errorMessage = "Game title cannot be empty"
                                    return@Button
                                }
                                isSaving = true
                                errorMessage = ""
                                coroutineScope.launch {
                                    try {
                                        var updatedPicturePath = currentGame.picture
                                        val file = editPicture
                                        if (file != null) {
                                            val newFile = PlatformFile(FileKit.filesDir, file.name)
                                            file.copyTo(newFile)
                                            updatedPicturePath = newFile.path
                                        }
                                        val updatedGame = currentGame.copy(name = editName, picture = updatedPicturePath)
                                        viewModel.update(
                                            game = updatedGame,
                                            onError = { error ->
                                                errorMessage = error
                                                isSaving = false
                                            },
                                            onSuccess = {
                                                currentGame = updatedGame
                                                isSaving = false
                                                isEditing = false
                                            }
                                        )
                                    } catch (e: Exception) {
                                        errorMessage = e.message ?: "Failed to save picture"
                                        isSaving = false
                                    }
                                }
                            },
                            enabled = !isSaving,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            shape = MaterialTheme.shapes.small
                        ) {
                            if (isSaving) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text("Save")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                } else {
                    // Game Image Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .clip(MaterialTheme.shapes.medium)
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.medium),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (currentGame.picture.isNotEmpty()) {
                                AsyncImage(
                                    model = currentGame.picture,
                                    contentDescription = currentGame.name,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "\uD83C\uDFAE",
                                        style = MaterialTheme.typography.displayLarge
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "No Image Uploaded",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Subtitle / Section title
                Text(
                    text = "CRAFTING OVERVIEW",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Features Grid / Details placeholder cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FeatureCard(
                        modifier = Modifier.weight(1f),
                        title = "Items",
                        icon = "📦",
                        description = "Manage crafting materials and resource base database."
                    )
                    FeatureCard(
                        modifier = Modifier.weight(1f),
                        title = "Inventory",
                        icon = "🎒",
                        description = "Track current inventory and stock counts."
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FeatureCard(
                        modifier = Modifier.weight(1f),
                        title = "Recipes",
                        icon = "🍳",
                        description = "Define complex formulas and crafting chains."
                    )
                    FeatureCard(
                        modifier = Modifier.weight(1f),
                        title = "Plan",
                        icon = "🎯",
                        description = "Set goals and calculate raw materials needed."
                    )
                }
            }
        }
    }

    @Composable
    private fun FeatureCard(
        modifier: Modifier = Modifier,
        title: String,
        icon: String,
        description: String
    ) {
        Card(
            modifier = modifier
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.medium),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = icon, style = MaterialTheme.typography.headlineMedium)
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}