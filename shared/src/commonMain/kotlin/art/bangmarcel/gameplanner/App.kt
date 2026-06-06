package art.bangmarcel.gameplanner

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import art.bangmarcel.gameplanner.configs.initFileKit
import art.bangmarcel.gameplanner.database.createDatabase
import art.bangmarcel.gameplanner.repositories.GameRepo
import art.bangmarcel.gameplanner.screens.GameListScreen
import art.bangmarcel.gameplanner.theme.GamePlannerTheme
import art.bangmarcel.gameplanner.viewmodels.GameListViewModel
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.resources.painterResource

import gameplanner.shared.generated.resources.Res
import gameplanner.shared.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    initFileKit()
    GamePlannerTheme {
        val db = remember { createDatabase() }
        val gRepo = remember { GameRepo(db.gameDao()) }

        Navigator(GameListScreen(gRepo))
    }
}