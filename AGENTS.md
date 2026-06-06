# GamePlanner — Agent Guide

Kotlin Multiplatform (Compose Multiplatform) targeting Android, iOS, Desktop (JVM).
Package: `art.bangmarcel.gameplanner`

## Commands

- Desktop (hot reload): `./gradlew :desktopApp:hotRun --auto`
- Desktop (standard): `./gradlew :desktopApp:run`
- Android (assemble): `./gradlew :androidApp:assembleDebug`
- iOS: open `iosApp/` in Xcode and run from there
- All tests: `./gradlew :shared:allTests`

## Architecture

Three Gradle modules — `:shared` (core), `:androidApp` (thin shell), `:desktopApp` (thin shell).

**All app logic lives in `:shared`** — screens, viewmodels, repositories, DAOs, entities, database setup. Platform apps only provide the entry point and call `App()`.

Shared source sets: `commonMain`, `androidMain`, `jvmMain`, `iosMain`.

### Key libraries
- **Navigation**: Voyager (`Screen` + `ScreenModel` pattern) — screens are classes implementing `Screen`, viewmodels implement `ScreenModel`
- **Database**: Room with `expect/actual` pattern — `getDatabaseBuilder()` and `AppDatabaseConstructor` have platform-specific `actual` implementations
- **SQLite**: Bundled driver (`BundledSQLiteDriver`) on all platforms — no native SQLite dependency needed
- **KSP**: Room compiler runs per target (Android, JVM, iOS Arm64, iOS Simulator Arm64)

### Platform database paths
- **JVM/Desktop**: `~/.craftingplanner/sqlite.db` (folder is `.craftingplanner`, not `.gameplanner`)
- **Android**: app's standard `getDatabasePath`
- **iOS**: app's `NSDocumentDirectory`

### Android gotcha
`DatabaseBuilder.android.kt` requires `appContext` to be initialized (a `lateinit var`). The Android entry point must set this before Room is used.

## Conventions

- Design system is documented in `DESIGN.md` — dark theme, Inter typography, JetBrains Mono for numeric data. Read it before editing any UI.
- Code style: `kotlin.code.style=official`
- Existing typo in source: the entity package is `entitties` (not `entities`) — preserve this path for now to avoid breaking imports
- `AppDatabase` version is `1` — when adding/changing entities, bump version and provide a migration (Room will crash without it)
