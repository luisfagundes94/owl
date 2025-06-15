# Owl (Device Scanner Android App)

A modern Android application for scanning and listing devices on the local network.

## Screenshots
### Light Mode
<p float="left">
  <img src="https://github.com/luisfagundes94/owl/blob/master/screenshots/light_device_list_screen.png" width="200" /> 
  <img src="https://github.com/luisfagundes94/owl/blob/master/screenshots/light_wifi_router_list_screen.png" width="200" /> 
</p>

### Dark Mode
<p float="left">
  <img src="https://github.com/luisfagundes94/owl/blob/master/screenshots/dark_device_list_screen.png" width="200" /> 
  <img src="https://github.com/luisfagundes94/owl/blob/master/screenshots/dark_wifi_router_list_screen.png" width="200" /> 
</p>

## Features

- Scan local network for devices
- Display device information in a list
- Built with Jetpack Compose, Hilt, and modern Android libraries
- Unit tested with MockK, Turbine, and kotlinx-coroutines-test

## Tech Stack

- **Kotlin** (Programming Language)
- **Jetpack Compose** (UI toolkit)
- **Hilt** (Dependency Injection)
- **DataStore** (Local Preferences)
- **Room **(Local Database)
- **Coroutines & Flow** (Asynchronous programming)
- **JUnit, MockK, Turbine** (Unit Testing)

## Project Structure

- `app/` - Main application module (entry point, DI setup)
- `core/domain/` - Shared domain layer (use cases, business logic, models)
- `core/data/` - Shared data layer (repositories, data sources, models)
- `core/common/` - Shared utilities, extensions, and base classes
- `core/designsystem/` - Design system (UI components, themes, styles)
- `core/network/` - Network utilities (scanner, connectivity, wifi)
- `core/testing/` - Shared test utilities and rules
- `feature/device/` - Device scanning feature
- `feature/history/` - Network history feature

## Testing

To run unit tests:

```sh
./gradlew test
