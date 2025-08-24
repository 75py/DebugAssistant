# Essential Development Commands

## Build Commands
- `./gradlew build` - Full project build
- `./gradlew assembleDebug` - Build debug APK
- `./gradlew assembleRelease` - Build release APK

## Testing Commands
- `./gradlew testDebugUnitTest` - Run unit tests
- `./gradlew connectedAndroidTest` - Run instrumentation tests

## Code Quality
- `./gradlew detekt` - Run static code analysis
- `./gradlew ktlintCheck` - Check code formatting
- `./gradlew ktlintFormat` - Auto-format code

## Gradle Management
- `./gradlew clean` - Clean build artifacts
- `./gradlew --refresh-dependencies` - Refresh dependencies

## System Utilities (Linux)
- `git` - Version control
- `find . -name "*.kt"` - Find Kotlin files
- `grep -r "pattern" --include="*.kt"` - Search in Kotlin files
- `ls -la` - List files with details
- `cd path/to/directory` - Change directory