package art.bangmarcel.gameplanner.configs

import art.bangmarcel.gameplanner.database.createDatabase
import art.bangmarcel.gameplanner.repositories.GameRepo
import art.bangmarcel.gameplanner.viewmodels.CreateGameViewModel
import art.bangmarcel.gameplanner.viewmodels.GameListLayoutViewModel
import art.bangmarcel.gameplanner.viewmodels.GameListViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun initKoin() {
    val appModule = module {
        val db = createDatabase()

        single { db }
        single { db.gameDao() }
        single { GameRepo(get()) }

        factory { GameListViewModel(get()) }
        factory { CreateGameViewModel(get()) }
        factory { GameListLayoutViewModel(get()) }
    }

    startKoin {
        modules(appModule)
    }
}