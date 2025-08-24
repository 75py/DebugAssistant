# GitHub Copilot Instructions for Debug Assistant

## Project Overview

Debug Assistant is an Android application designed to help Android developers by providing quick access to development tools through system tiles. The app enables toggling proxy settings and ADB (Android Debug Bridge) directly from the Quick Settings panel.

**Package Name:** `com.nagopy.android.debugassistant`  
**Application ID:** `com.nagopy.android.debugassistant`  
**Language:** Kotlin  
**Min SDK:** 28 (Android 9.0)  
**Target SDK:** 34 (Android 14)  
**Architecture:** Clean Architecture with MVVM

## Project Structure

This is a multi-module Android project with the following structure:

```
debugassistant/
├── app/                          # Main application module
│   ├── src/main/
│   │   ├── java/com/nagopy/android/debugassistant/
│   │   │   ├── ui/               # UI components (Activities, ViewModels, Compose)
│   │   │   │   ├── main/         # Main screen components
│   │   │   │   ├── theme/        # Theme and styling
│   │   │   │   └── tile/         # Quick Settings tile services
│   │   │   ├── AppModule.kt      # Koin dependency injection
│   │   │   └── DebugAssistantApplication.kt
│   │   └── AndroidManifest.xml
│   └── src/androidTest/          # Instrumentation tests
├── data/
│   └── repository/               # Data layer - Repository implementations
│       ├── src/main/java/com/nagopy/android/debugassistant/data/repository/
│       └── src/test/
├── domain/
│   ├── model/                    # Domain models and entities
│   └── usecase/                  # Business logic and use cases
│       ├── src/main/java/com/nagopy/android/debugassistant/domain/usecase/
│       └── src/test/
└── build.gradle                  # Root build configuration
```

## Technology Stack & Dependencies

### Core Technologies
- **Kotlin**: Primary programming language
- **Android SDK**: Target SDK 34, Min SDK 28
- **Jetpack Compose**: Modern UI toolkit (`compose_version = '1.5.0'`)
- **Gradle**: Build system with Kotlin DSL support

### Architecture & Patterns
- **Clean Architecture**: Separation of concerns with data/domain/ui layers
- **MVVM**: Model-View-ViewModel pattern with Compose
- **Repository Pattern**: Data access abstraction
- **Use Case/Interactor Pattern**: Business logic encapsulation
- **Dependency Injection**: Koin framework

### Testing
- **JUnit**: Unit testing framework
- **MockK**: Mocking framework for Kotlin
- **Compose Testing**: UI testing with `androidx.compose.ui.test`
- **Android Test**: Instrumentation tests

### Code Quality & Tooling
- **Detekt**: Static code analysis (config: `detekt.yml`)
- **Ktlint**: Code formatting and style enforcement
- **GitHub Actions**: CI/CD pipeline

## Key Features & Components

### Main Features
1. **Proxy Toggle**: Enable/disable system HTTP proxy via Quick Settings
2. **ADB Toggle**: Enable/disable Android Debug Bridge via Quick Settings
3. **Permission Management**: Handles `WRITE_SECURE_SETTINGS` permission

### Core Components
- **MainActivity**: Main screen with Compose UI
- **ProxyTileService**: Quick Settings tile for proxy control
- **AdbTileService**: Quick Settings tile for ADB control
- **Repositories**: Data access for system settings
- **Use Cases**: Business logic for enable/disable operations

## Coding Patterns & Conventions

### Package Structure
- Base package: `com.nagopy.android.debugassistant`
- Modules follow feature-based packaging
- Clear separation between UI, domain, and data layers

### Naming Conventions
- **Classes**: PascalCase (e.g., `MainActivity`, `EnableProxyUseCase`)
- **Functions**: camelCase (e.g., `enableProxy()`, `onProxySwitchClicked()`)
- **Composables**: PascalCase with `@Composable` annotation
- **Variables**: camelCase (e.g., `isProxyEnabled`, `proxyHost`)
- **Constants**: UPPER_SNAKE_CASE

### Compose UI Patterns
- Use `@Composable` functions for UI components
- State management with `remember` and state hoisting
- Material Design components
- Modifier chaining for styling

### Architecture Patterns
```kotlin
// Use Case Interface
interface EnableProxyUseCase {
    fun enableProxy(host: String, port: Int): Boolean
}

// Interactor Implementation
class EnableProxyInteractor(
    private val repository: GlobalSettingsRepository
) : EnableProxyUseCase {
    override fun enableProxy(host: String, port: Int): Boolean {
        // Business logic implementation
    }
}

// Repository Interface
interface GlobalSettingsRepository {
    fun putString(key: String, value: String): Boolean
    fun putInt(key: String, value: Int): Boolean
}
```

### Dependency Injection with Koin
```kotlin
val domainModule = module {
    single<EnableProxyUseCase> { EnableProxyInteractor(get()) }
    single<GlobalSettingsRepository> { GlobalSettingRepositoryImpl(get()) }
}
```

## Testing Approach

### Unit Tests
- Test use cases/interactors with MockK
- Focus on business logic validation
- Repository pattern allows easy mocking

### UI Tests
- Compose testing with `ComposeTestRule`
- Test user interactions and state changes
- UI component isolation testing

### Test Structure Example
```kotlin
class EnableProxyInteractorTest {
    private lateinit var interactor: EnableProxyInteractor
    private lateinit var repository: GlobalSettingsRepository

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        interactor = EnableProxyInteractor(repository)
    }

    @Test
    fun enableProxy_success() {
        every { repository.putString(any(), any()) } returns true
        val result = interactor.enableProxy("127.0.0.1", 8080)
        assertTrue(result)
    }
}
```

## Build & Development

### Gradle Tasks
- `./gradlew build` - Full build
- `./gradlew testDebugUnitTest` - Run unit tests
- `./gradlew detekt` - Run static analysis
- `./gradlew ktlintCheck` - Check code formatting

### Code Quality Configuration
- **Detekt**: Custom rules in `detekt.yml`
  - Disabled long parameter lists for Compose
  - Ignores private functions in complexity checks
  - Max line length: 140 characters
- **Ktlint**: Standard Kotlin formatting rules

## Development Guidelines

### When Adding New Features
1. **Domain First**: Start with use case interfaces in domain module
2. **Repository Pattern**: Add repository interface and implementation
3. **Dependency Injection**: Register in appropriate Koin modules
4. **UI Layer**: Create Compose UI components with proper state management
5. **Testing**: Add unit tests for business logic and UI tests for interactions

### System Integration
- Use `android.provider.Settings.Global` for system settings
- Handle `WRITE_SECURE_SETTINGS` permission requirements
- Implement proper error handling for system-level operations

### Tile Services
- Extend `TileService` for Quick Settings integration
- Handle tile state synchronization
- Provide user feedback through tile states

## Common Patterns to Follow

### State Management in Compose
```kotlin
@Composable
fun ProxySection(
    isProxyEnabled: Boolean,
    proxyHost: String,
    onProxyHostChanged: (String) -> Unit,
    onProxySwitchClicked: (Boolean) -> Unit
) {
    // UI implementation with state hoisting
}
```

### Use Case Implementation
```kotlin
class EnableProxyInteractor(
    private val globalSettingsRepository: GlobalSettingsRepository
) : EnableProxyUseCase {
    override fun enableProxy(proxyInfo: ProxyInfo): Boolean {
        return try {
            globalSettingsRepository.putString(
                Settings.Global.HTTP_PROXY,
                "${proxyInfo.host}:${proxyInfo.port}"
            )
        } catch (e: Exception) {
            false
        }
    }
}
```

### Repository Pattern
```kotlin
interface GlobalSettingsRepository {
    fun getString(key: String): String?
    fun putString(key: String, value: String): Boolean
    fun getInt(key: String, defaultValue: Int = 0): Int
    fun putInt(key: String, value: Int): Boolean
}
```

## CI/CD Integration

The project uses GitHub Actions with:
- **Java 17 (Temurin distribution)**
- **Gradle caching** for faster builds
- **Unit test execution** on push/PR
- **Detekt analysis** for code quality
- **Path ignoring** for documentation changes

When suggesting code changes, ensure they:
1. Follow the established architecture patterns
2. Include appropriate error handling
3. Add corresponding tests
4. Meet code quality standards (Detekt/Ktlint)
5. Handle Android system permissions properly

## Security Considerations

- **WRITE_SECURE_SETTINGS**: Required permission for system setting modifications
- **System Settings**: Handle SecurityException when accessing restricted settings
- **User Permissions**: Provide clear permission requirement information
- **Error States**: Graceful degradation when permissions are not granted

This application modifies sensitive system settings and should handle security restrictions appropriately.