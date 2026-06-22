package art.bangmarcel.gameplanner.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import art.bangmarcel.gameplanner.entitties.GameEntity
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import gameplanner.shared.generated.resources.Res
import gameplanner.shared.generated.resources.arrow_back_ios_new_24px
import org.jetbrains.compose.resources.painterResource

class GameLayoutScreen(val game: GameEntity) : Screen {
    @Composable
    override fun Content() {
        val parentNavigator = LocalNavigator.currentOrThrow

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val isWideScreen = maxWidth >= 600.dp

            if (isWideScreen) {
                // Desktop Split-pane layout
                Row(modifier = Modifier.fillMaxSize()) {
                    // Sub-sidebar
                    Column(
                        modifier = Modifier
                            .width(220.dp)
                            .fillMaxHeight()
                            .background(MaterialTheme.colorScheme.surfaceContainerLow)
                    ) {
                        // Game name header
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 20.dp)
                        ) {
                            Text(
                                text = game.name,
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                        Spacer(modifier = Modifier.height(8.dp))

                        val items = listOf(
                            "Game Details" to "📝",
                            "Items" to "📦",
                            "Inventory" to "🎒",
                            "Recipe" to "🍳",
                            "Plan" to "🎯"
                        )

                        var selectedItem by remember { mutableStateOf("Game Details") }

                        items.forEach { (title, icon) ->
                            SubSidebarItem(
                                title = title,
                                icon = icon,
                                isSelected = title == selectedItem,
                                onClick = { selectedItem = title }
                            )
                        }
                    }

                    // Vertical Divider
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                            .background(MaterialTheme.colorScheme.outlineVariant)
                    )

                    // Detail area (nested navigator)
                    Box(modifier = Modifier.weight(1f)) {
                        key(game.id) {
                            Navigator(GameDetailScreen(game))
                        }
                    }
                }
            } else {
                // Mobile stacked layout
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    // Custom App Bar for Mobile
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(MaterialTheme.colorScheme.surfaceContainerLow)
                            .padding(horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { parentNavigator.pop() }) {
                            Icon(
                                painter = painterResource(Res.drawable.arrow_back_ios_new_24px),
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = game.name,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                    // Horizontal Scrollable Tabs on Mobile
                    val tabs = listOf(
                        "Game Details" to "📝",
                        "Items" to "📦",
                        "Inventory" to "🎒",
                        "Recipe" to "🍳",
                        "Plan" to "🎯"
                    )
                    var selectedTab by remember { mutableStateOf("Game Details") }

                    ScrollableTabRow(
                        selectedTabIndex = tabs.indexOfFirst { it.first == selectedTab },
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        contentColor = MaterialTheme.colorScheme.primary,
                        edgePadding = 16.dp,
                        divider = { HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant) }
                    ) {
                        tabs.forEach { (title, icon) ->
                            Tab(
                                selected = selectedTab == title,
                                onClick = { selectedTab = title },
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = icon, modifier = Modifier.padding(end = 4.dp))
                                        Text(text = title)
                                    }
                                }
                            )
                        }
                    }

                    // Content Area (nested navigator)
                    Box(modifier = Modifier.weight(1f)) {
                        key(game.id) {
                            Navigator(GameDetailScreen(game))
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun SubSidebarItem(
        title: String,
        icon: String,
        isSelected: Boolean,
        onClick: () -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clickable(onClick = onClick)
                .background(
                    if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)
                    else Color.Transparent
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Active Indicator Bar on the left edge
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

            Text(
                text = icon,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(end = 8.dp)
            )

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                ),
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}