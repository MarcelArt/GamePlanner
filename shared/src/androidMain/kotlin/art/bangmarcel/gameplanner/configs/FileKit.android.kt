package art.bangmarcel.gameplanner.configs

import android.content.Context
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.manualFileKitCoreInitialization

lateinit var appContext: Context
actual fun newFileKit() {
    FileKit.manualFileKitCoreInitialization(context = appContext)
}