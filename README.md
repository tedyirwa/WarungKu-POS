# WarungKu POS

An offline Android Point of Sale (POS) application for a small grocery shop (*warung sembako*).

## Project Status

🚧 In Development — MVP

## Features

- [ ] Product management
- [ ] Cashier and checkout
- [ ] Stock management
- [ ] Transaction history
- [ ] Daily sales summary

## Tech Stack

- Kotlin
- Jetpack Compose
- Material 3
- MVVM
- ViewModel and StateFlow
- Coroutines and Flow
- Room Database
- Repository Pattern
- Navigation Compose
- JUnit

## Architecture and Data

- Single Android application module
- Local database using Room
- No backend or cloud database
- Designed for a single user on one Android device

## Key Requirements

- Atomic checkout transactions
- Stock must never become negative
- Transaction prices are stored as snapshots
- Local transaction history and daily revenue summary

## Development Environment

- Android Studio
- Kotlin
- Gradle Kotlin DSL

## License

To be determined.
