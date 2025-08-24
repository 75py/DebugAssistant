# Code Style and Conventions

## Naming Conventions
- **Classes**: PascalCase (e.g., `MainActivity`, `EnableProxyUseCase`)
- **Functions**: camelCase (e.g., `enableProxy()`, `onProxySwitchClicked()`)
- **Composables**: PascalCase with `@Composable` annotation
- **Variables**: camelCase (e.g., `isProxyEnabled`, `proxyHost`)
- **Constants**: UPPER_SNAKE_CASE

## Architecture Patterns
- **Clean Architecture**: Data/Domain/UI layer separation
- **MVVM**: Model-View-ViewModel with Compose
- **Repository Pattern**: Data access abstraction
- **Use Case/Interactor Pattern**: Business logic encapsulation

## Compose UI Patterns
- Use `@Composable` functions for UI components
- State management with `remember` and state hoisting
- Material Design components
- Modifier chaining for styling

## Dependency Injection with Koin
```kotlin
val domainModule = module {
    single<EnableProxyUseCase> { EnableProxyInteractor(get()) }
    single<GlobalSettingsRepository> { GlobalSettingRepositoryImpl(get()) }
}
```

## Error Handling
- Handle SecurityException when accessing system settings
- Provide graceful degradation when permissions not granted
- Use try-catch blocks for system-level operations