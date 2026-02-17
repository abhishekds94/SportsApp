# Sports App - Android Developer Assessment

A modern Android application for browsing sports leagues and teams, built with Jetpack Compose and Clean Architecture.

## 📱 Features

- **Browse Leagues** - Filter teams by sport and league
- **Search Teams** - Global search across all teams
- **Team Details** - View comprehensive team information
- **Modern UI** - Material 3 design with Jetpack Compose
- **Offline Support** - Graceful error handling and loading states

## 🏗️ Architecture

### MVVM + Clean Architecture

```
┌─────────────────────────────────────────┐
│           Presentation Layer            │
│  (Jetpack Compose + ViewModels)         │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│            Domain Layer                  │
│     (Use Cases + Models)                 │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│            Data Layer                    │
│  (Repositories + Remote Data Sources)    │
└──────────────────────────────────────────┘
```

### Module Structure

```
app/                    # Application entry point
├── MainActivity
├── Navigation
└── Dependency Injection

core/
├── common/            # Shared utilities & extensions
├── designsystem/      # UI components & theme
└── network/          # API client configuration

data/
├── teams/            # Teams data layer
└── events/           # Events data layer

feature/
├── search/           # Search functionality
└── teamdetail/       # Team details view
```

### Dependency Graph

```
app
 ├─> feature:search
 ├─> feature:teamdetail
 ├─> core:designsystem
 └─> core:common

feature:*
 ├─> data:teams
 ├─> data:events
 ├─> core:designsystem
 └─> core:common

data:*
 ├─> core:network
 └─> core:common
```

## 🛠️ Tech Stack

| Category | Libraries |
|----------|-----------|
| **UI** | Jetpack Compose, Material 3 |
| **Architecture** | MVVM, Clean Architecture, Multi-module |
| **DI** | Hilt |
| **Networking** | Retrofit, OkHttp, Kotlinx Serialization |
| **Async** | Kotlin Coroutines, Flow |
| **Image Loading** | Coil |
| **Testing** | JUnit, MockK, Turbine, Truth |
| **Build** | Gradle (Kotlin DSL), KSP |

## 🚀 Getting Started

### Prerequisites

- **Android Studio:** Hedgehog (2023.1.1) or later
- **JDK:** 17
- **Android SDK:** 34
- **Min SDK:** 24

### Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd SportsApp
   ```

2. **Open in Android Studio**
    - File → Open
    - Select project directory
    - Wait for Gradle sync

3. **Build the project**
   ```bash
   ./gradlew clean build
   ```

4. **Run on emulator/device**
   ```bash
   ./gradlew installDebug
   ```
   Or click Run ▶️ in Android Studio

### API Configuration

The app uses **TheSportsDB Free API** with built-in API key ("3").
No additional configuration required.

**API Base URL:** `https://www.thesportsdb.com/api/v1/json/3/`

## 🧪 Testing

### Run All Tests
```bash
./gradlew test
```

### Run Module-Specific Tests
```bash
./gradlew :feature:search:test
./gradlew :data:teams:test
```

### Test Coverage
- **ViewModels:** Search, TeamDetail
- **Repositories:** Teams, Events
- **Focus:** Critical user paths and business logic
- **Tools:** MockK for mocking, Turbine for Flow testing

## 📁 Project Structure

```
SportsApp/
├── app/
│   ├── src/main/
│   │   ├── kotlin/com/sportsapp/
│   │   │   ├── MainActivity.kt
│   │   │   ├── SportsApplication.kt
│   │   │   ├── di/              # Dependency injection
│   │   │   └── navigation/      # Navigation logic
│   │   └── res/
│   │       ├── values/strings.xml
│   │       └── ...
│   └── build.gradle.kts
│
├── buildSrc/
│   └── src/main/kotlin/
│       └── Dependencies.kt      # Centralized dependency management
│
├── core/
│   ├── common/                  # Shared utilities
│   ├── designsystem/            # UI components & theme
│   └── network/                 # API configuration
│
├── data/
│   ├── teams/                   # Teams data layer
│   └── events/                  # Events data layer
│
├── feature/
│   ├── search/                  # Search screen
│   └── teamdetail/              # Team details screen
│
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## 🎨 Key Features Implementation

### State Management
- **StateFlow** for UI state
- **Resource** sealed class for loading/success/error states
- Unidirectional data flow (MVI pattern)

### Error Handling
- Network error detection
- User-friendly error messages
- Retry mechanisms
- Empty state handling

### UI/UX
- Material 3 design system
- Loading indicators
- Empty states with helpful messages
- Smooth animations
- Responsive layouts

## 📦 Build Variants

### Debug
- Development build
- Logging enabled
- No code obfuscation

### Release
- Production build
- ProGuard/R8 optimization enabled
- Code shrinking and obfuscation
- Optimized for performance

## 🔧 Configuration

### Dependencies
All dependencies are centralized in `buildSrc/src/main/kotlin/Dependencies.kt`:
- Easy version management
- Consistent across modules
- Type-safe accessors

### Build Configuration
```kotlin
android {
    compileSdk = 34
    minSdk = 24
    targetSdk = 34
    
    buildFeatures {
        compose = true
    }
}
```

## 📝 Code Quality

### Kotlin Conventions
- Immutability preferred
- Extension functions for utility code
- Sealed classes for state modeling
- Coroutines for asynchronous operations

### Architecture Principles
- Single Responsibility Principle
- Dependency Inversion
- Separation of Concerns
- Testability

### Code Organization
- Package by feature
- Clear module boundaries
- Consistent naming conventions
- Comprehensive documentation

## 🤖 AI Usage

This project was developed with AI assistance (Claude). See `AI_USAGE.md` for:
- How AI was used
- Key prompts and conversations
- Understanding of generated code
- Modifications made to AI suggestions

## 📄 License

This project is created for assessment purposes.

## 👤 Author

[Your Name]
[Your Email]
[LinkedIn/GitHub]

---

## 📚 Additional Documentation

- [Architecture Decision Records](docs/adr/) (if applicable)
- [API Documentation](docs/api.md) (if applicable)
- [Contributing Guidelines](CONTRIBUTING.md) (if applicable)

## 🐛 Known Issues

None at this time.

## 🚧 Future Improvements

- Offline caching with Room database
- Pagination for large team lists
- Advanced search filters
- Favorite teams
- Dark mode enhancements

---

**Built with ❤️ using Jetpack Compose**