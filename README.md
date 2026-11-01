# Cargo Android App

## Overview
The Cargo Android App is designed to facilitate the management of cargo services, providing features for both couriers and customers. Users can log in, view pricing for specific routes, and track their loads in real-time.

## Features
- **Courier and Customer Login**: Separate login activities for couriers and customers to ensure secure access.
- **Pricing for Specific Routes**: Users can view pricing information based on selected routes.
- **Load Tracking**: Real-time tracking of cargo loads to keep users informed about their shipments.

## Project Structure
```
cargo-android-app
├── app
│   ├── src
│   │   ├── main
│   │   │   ├── AndroidManifest.xml
│   │   │   ├── java
│   │   │   │   └── com
│   │   │   │       └── example
│   │   │   │           └── cargo
│   │   │   │               ├── MainActivity.kt
│   │   │   │               ├── login
│   │   │   │               │   ├── CustomerLoginActivity.kt
│   │   │   │               │   └── CourierLoginActivity.kt
│   │   │   │               ├── ui
│   │   │   │               │   ├── dashboard
│   │   │   │               │   │   ├── CustomerDashboardFragment.kt
│   │   │   │               │   │   └── CourierDashboardFragment.kt
│   │   │   │               │   └── tracking
│   │   │   │               │       └── LoadTrackingFragment.kt
│   │   │   │               ├── data
│   │   │   │               │   ├── model
│   │   │   │               │   │   ├── RoutePrice.kt
│   │   │   │               │   │   ├── Load.kt
│   │   │   │               │   │   └── User.kt
│   │   │   │               │   ├── repository
│   │   │   │               │   │   └── CargoRepository.kt
│   │   │   │               │   └── local
│   │   │   │               │       └── AppDatabase.kt
│   │   │   │               ├── network
│   │   │   │               │   └── ApiService.kt
│   │   │   │               └── util
│   │   │   │                   └── Extensions.kt
│   │   │   ├── res
│   │   │   │   ├── layout
│   │   │   │   │   ├── activity_main.xml
│   │   │   │   │   ├── activity_customer_login.xml
│   │   │   │   │   ├── activity_courier_login.xml
│   │   │   │   │   ├── fragment_customer_dashboard.xml
│   │   │   │   │   ├── fragment_courier_dashboard.xml
│   │   │   │   │   └── fragment_load_tracking.xml
│   │   │   │   ├── values
│   │   │   │   │   ├── strings.xml
│   │   │   │   │   └── colors.xml
│   │   │   │   └── navigation
│   │   │   │       └── nav_graph.xml
│   │   ├── androidTest
│   │   └── test
│   ├── build.gradle
│   └── proguard-rules.pro
├── build.gradle
├── settings.gradle
├── gradle.properties
├── gradle
│   └── wrapper
│       └── gradle-wrapper.properties
├── .gitignore
└── README.md
```

## Getting Started
1. Clone the repository.
2. Open the project in your preferred IDE.
3. Build and run the application on an Android device or emulator.

## Dependencies
- Kotlin
- AndroidX libraries
- Retrofit for network operations
- Room for local database management

## License
This project is licensed under the MIT License - see the LICENSE file for details.