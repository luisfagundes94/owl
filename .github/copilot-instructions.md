## Copilot Instructions for Android Project
- Give me always the latest recommended practices for Android development.

## Security
- Check for security vulnerabilities after generation.
- Avoid hardcoding sensitive information like API keys or passwords.
- Use secure coding practices.

## Version Control
- Always starts the commit message with a verb in present participle (refactoring, fixing, etc).
- Update .gitignore for new build artifacts or dependencies.

## Code Style
- Follow the project's coding style guide.

## Testing Requirements
- Write unit tests for new features.
- Use MockK for mocking dependencies in tests.
- Use Turbine for testing flows.

## Code Quality
- Ensure code is clean, scalable and maintainable.
- Use design patterns where appropriate.
- Avoid code duplication.
- Use meaningful variable and function names.

## Architecture
- Follow MVVM architecture.
- Follow Clean Architecture principles.
- Use ViewModels for UI-related data.
- Use StateFlow for data observation.
- Use Repository pattern for data access.
- Use Hilt for dependency injection.
- Use flow for asynchronous operations.
- Use sealed classes or data classes for representing UI states, choose what is best given the context.

## Jetpack Compose
- Use Jetpack Compose for UI development.
- Follow best practices for state management.
- Generate composables with performance in mind.
