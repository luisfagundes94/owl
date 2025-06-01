# Owl (Device Scanner Android App)

A modern Android application for scanning and listing devices on the local network.

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