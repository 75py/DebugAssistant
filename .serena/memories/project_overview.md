# DebugAssistant Project Overview

## Purpose
Debug Assistant is an Android application that helps Android developers by providing quick access to development tools through system tiles. The app enables toggling proxy settings and ADB (Android Debug Bridge) directly from the Quick Settings panel.

## Tech Stack
- **Language**: Kotlin
- **Min SDK**: 28 (Android 9.0)
- **Current SDK**: compileSdk 35, targetSdk 35
- **Architecture**: Clean Architecture with MVVM
- **UI Framework**: Jetpack Compose 1.8.0
- **Dependency Injection**: Koin 3.5.6
- **Build System**: Gradle with Android Gradle Plugin 8.8.0/8.9.2
- **Testing**: JUnit, MockK, Compose Testing

## Project Structure
Multi-module Android project:
- `app/` - Main application module (UI, ViewModels, Activities, Tile Services)
- `data/repository/` - Data layer with repository implementations
- `domain/usecase/` - Business logic and use cases
- `domain/model/` - Domain models and entities

## Key Features
1. Proxy Toggle: Enable/disable system HTTP proxy via Quick Settings
2. ADB Toggle: Enable/disable Android Debug Bridge via Quick Settings  
3. Permission Management: Handles WRITE_SECURE_SETTINGS permission

## Package Structure
Base package: `com.nagopy.android.debugassistant`
- Clear separation between UI, domain, and data layers
- Feature-based packaging within modules