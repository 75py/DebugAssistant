# Task Completion Checklist

When completing a development task, follow these steps:

## Code Changes
1. **Make minimal changes**: Only modify what's necessary for the task
2. **Follow architecture patterns**: Maintain Clean Architecture structure
3. **Add tests**: Include unit tests for business logic, UI tests for interactions
4. **Handle errors**: Implement proper error handling for system operations

## Build and Quality Checks
1. **Build the project**: `./gradlew build`
2. **Run tests**: `./gradlew testDebugUnitTest`
3. **Check code quality**: `./gradlew detekt`
4. **Format code**: `./gradlew ktlintCheck` or `./gradlew ktlintFormat`

## System Integration
- Verify WRITE_SECURE_SETTINGS permission handling
- Test with system settings modifications
- Ensure Quick Settings tiles work properly

## Documentation
- Update documentation if changes affect API or architecture
- Add comments for complex logic (matching existing style)
- Update README if new features are added

## Final Verification
- Test on device/emulator with required permissions
- Verify no regression in existing functionality
- Check that all modules compile and tests pass