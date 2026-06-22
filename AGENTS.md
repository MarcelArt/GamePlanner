# GamePlanner — Agent Guide

Kotlin Multiplatform (Compose Multiplatform) targeting Android, iOS, Desktop (JVM).
Package root: `art.bangmarcel.gameplanner`. All app logic lives in `:shared`; `:androidApp` and `:desktopApp` are thin shells that call `App()`.

## Hard rules for agents

1. **Edit scope — screens only.** You may only edit files under
   `shared/src/commonMain/kotlin/art/bangmarcel/gameplanner/screens/`.
   If a task requires changes to viewmodels, repositories, DAOs, entities, database,
   DI (`configs/DI.kt`), or theme — **stop and tell the user** what needs changing.
   Do not edit those files yourself. (More directories may be opened up later.)
2. **No hardcoded styling.** Never hardcode colors, fonts, typography, or shapes
   (no literal `Color(0x…)`, no `RoundedCornerShape(8.dp)`, no `FontFamily.Inter`,
   no raw `TextStyle`). Always pull tokens from the existing theme:
   - Colors → `MaterialTheme.colorScheme.*` (`surface`, `onSurface`, `background`,
     `primary`, `surfaceContainerHigh`, `onSurfaceVariant`, …)
   - Typography → `MaterialTheme.typography.*`
   - Shapes → `MaterialTheme.shapes.*`
   The design language (dark-first, Inter, JetBrains Mono for numerics, tonal layering)
   is defined in `DESIGN.md` and materialized in
   `shared/.../theme/{Theme,Color,Type,Shape}.kt`. Read `DESIGN.md` before any UI work.
3. **No invented APIs/SDKs.** Never guess library APIs (Voyager, Room, Compose, Coil,
   FileKit, Koin). Use the **context7** skill / MCP to fetch current docs before writing
   calls you are not 100% sure about.
4. **Do not run Gradle.** Never invoke `./gradlew`, `gradle`, or any build/test/install
   command. The user runs builds and verification themselves. If you think a build is
   needed, say so and stop.

## Architecture

- Gradle modules: `:shared` (core), `:androidApp`, `:desktopApp`. Shared source sets:
  `commonMain`, `androidMain`, `jvmMain`, `iosMain`.
- Entry point: `App()` in `shared/.../App.kt` — initializes FileKit, Coil3 image loader
  (with `addPlatformFileSupport()`), Koin, then wraps a Voyager `Navigator` in `AppTheme`.
- **Navigation**: Voyager. Screens are classes implementing `Screen` with a `@Composable Content()`.
  ViewModels implement `ScreenModel`. In a screen, obtain a VM via
  `koinScreenModel<XxxViewModel>()`. Navigate with `LocalNavigator.currentOrThrow`
  (`navigator.push(...)`, `navigator.pop()`).
- **DI**: Koin, set up in `configs/DI.kt`. Database, DAO, repo, and each viewmodel are
  registered there. Adding a new screen that needs a new VM also requires a `factory { … }`
  entry in `DI.kt` — that's an out-of-scope edit; ask the user.
- **Database**: Room via `expect/actual`. `AppDatabase` (version **1**) declares entities
  and `gameDao()`. `getDatabaseBuilder()` / `AppDatabaseConstructor` have platform `actual`s.
  Driver is `BundledSQLiteDriver` everywhere — no native SQLite dependency.
  - Changing/adding entities ⇒ bump `AppDatabase.version` and provide a migration, or Room
    will crash. This is an out-of-scope edit; ask the user.
  - **KSP** runs per target (Android, JVM, iOS arm64, iOS Simulator arm64).
- **Platform DB paths**: JVM/Desktop writes to `~/.craftingplanner/sqlite.db`
  (folder is `.craftingplanner`, **not** `.gameplanner`); Android uses `getDatabasePath`;
  iOS uses `NSDocumentDirectory`.
- **Android gotcha**: `DatabaseBuilder.android.kt` relies on a `lateinit var appContext`
  that the Android entry point must set before any Room access.

## Repo-specific conventions

- Package typo to preserve: the entities package is `entitties` (double-t). Keep imports
  as `art.bangmarcel.gameplanner.entitties.*` — do not "fix" it.
- Code style: `kotlin.code.style=official`.
- Generated resources live under `gameplanner.shared.generated.resources.*`
  (`Res.drawable.*`, etc.) — reference via `painterResource(Res.drawable.…)`.

## Verification (run by the user, not the agent)

For reference, these are the commands the user uses to build/run:
- Desktop hot reload: `./gradlew :desktopApp:hotRun --auto`
- Desktop run: `./gradlew :desktopApp:run`
- Android: `./gradlew :androidApp:assembleDebug`
- iOS: open `iosApp/` in Xcode
- All tests: `./gradlew :shared:allTests`

When you finish editing, tell the user which of these to run — do not run them yourself.

## Reference docs in this repo
- `DESIGN.md` — design system (colors, typography, shapes, spacing, component specs). Read before UI work.
- `README.md` — high-level KMP project layout.
