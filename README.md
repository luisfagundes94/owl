# Owl (Device Scanner Android App)

A modern Android application for scanning and listing devices on the local network.

## Screenshots
### Light Mode
<p float="left">
  <img src="https://github.com/luisfagundes94/owl/blob/master/screenshots/light_device_list_screen.png" width="200" /> 
</p>

### Dark Mode
<p float="left">
  <img src="https://github.com/luisfagundes94/owl/blob/master/screenshots/dark_device_list_screen.png" width="200" /> 
</p>

## Features

- Scan local network for devices
- Display device information in a list
- Built with Jetpack Compose, Hilt, and modern Android libraries
- Unit tested with MockK, Turbine, and kotlinx-coroutines-test

## Tech Stack

- **Kotlin**
- **Jetpack Compose**
- **Hilt (Dependency Injection)**
- **Coroutines & Flow**
- **JUnit, MockK, Turbine** for testing

## Project Structure

- `app/` - Main application module
- `feature/device/` - Device scanning and presentation logic
- `core/testing/` - Shared test utilities and rules

## Testing

To run unit tests:

```sh
./gradlew test
