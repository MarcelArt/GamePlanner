package art.bangmarcel.gameplanner

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import art.bangmarcel.gameplanner.configs.initFileKit
import art.bangmarcel.gameplanner.configs.initKoin
import art.bangmarcel.gameplanner.database.createDatabase
import art.bangmarcel.gameplanner.repositories.GameRepo
import art.bangmarcel.gameplanner.screens.GameListScreen
import cafe.adriel.voyager.navigator.Navigator
import art.bangmarcel.gameplanner.theme.AppTheme
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import io.github.vinceglb.filekit.coil.addPlatformFileSupport

@Composable
@Preview
fun App() {
    initFileKit()
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                addPlatformFileSupport()
            }
            .build()
    }
    initKoin()
    AppTheme {
        Navigator(GameListScreen())
    }
}