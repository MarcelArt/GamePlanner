package art.bangmarcel.gameplanner

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import art.bangmarcel.gameplanner.configs.initFileKit
import art.bangmarcel.gameplanner.database.createDatabase
import art.bangmarcel.gameplanner.repositories.GameRepo
import art.bangmarcel.gameplanner.screens.GameListScreen
import cafe.adriel.voyager.navigator.Navigator
import art.bangmarcel.gameplanner.theme.AppTheme

@Composable
@Preview
fun App() {
    initFileKit()
    AppTheme {
        val db = remember { createDatabase() }
        val gRepo = remember { GameRepo(db.gameDao()) }

        Navigator(GameListScreen(gRepo))
    }
}