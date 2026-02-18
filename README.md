# SportsApp (Android Developer Assessment)

A youth sports league browsing app built with **Jetpack Compose**, **MVVM**, modularized architecture, and **TheSportsDB** API.

## Features
- Browse leagues
- Browse teams in a league
- Team details
- Search teams
- Favorites (local persistence)

## Tech Stack
- Kotlin, Coroutines, Flow/StateFlow
- Jetpack Compose
- Retrofit + Kotlin Serialization
- Hilt (DI)
- Room (favorites persistence)
- JUnit + kotlinx.coroutines test utilities

## API
Base URL: `https://www.thesportsdb.com/api/v1/json/<ApiKey>/`

## To Add API Key,
- Create `local.properties` file and add this property `SPORTS_DB_API_KEY=<API_KEY_VALUE>`

Used endpoints:
- `all_leagues.php`
- `search_all_teams.php?l={league}`
- `searchteams.php?t={name}` (team details/search)

Rate limit (free tier): ~30 req/min

## Architecture
The app follows **MVVM** with clear separation across UI, domain, and data.

### Module Overview
- `app`
    - Entry point, navigation host, app-level wiring
- `core:common`
    - Shared utilities, result wrappers, error models
- `core:network`
    - Retrofit setup, interceptors, API call helpers
- `core:designsystem`
    - Reusable Compose UI components/theme
- `domain:leagues`, `domain:teams`
    - Domain models + use cases
- `data:leagues`, `data:teams`
    - Repository implementations, remote data sources, mappers
    - `data:teams` includes Room database for favorites
- `feature:leagues`
    - Leagues UI + ViewModel
- `feature:search`
    - Search UI + ViewModel
- `feature:teamdetail`
    - Team detail UI + ViewModel
- `feature:favorites`
    - Favorites UI + ViewModel

### State Management
- Each screen has a ViewModel exposing a `StateFlow` UI state
- UI renders based on state:
    - Loading
    - Error (with message + retry when applicable)
    - Empty state
    - Content

## Error Handling
- Network calls go through a safe API wrapper
- Errors are mapped to user-friendly messages (timeouts, connectivity issues, HTTP codes like 429, etc.)
- UI displays meaningful error states with retry where appropriate

## Build Configuration
- Debug and Release build types are configured
- Release builds enable:
    - R8 / Proguard minification
    - Resource shrinking

## Running the App
### Requirements
- Android Studio (latest stable recommended)
- JDK 17

### Steps
1. Open the project in Android Studio
2. Let Gradle sync
3. Run the `app` configuration on an emulator/device

> Note: Ensure the repository includes Gradle wrapper files:
> - `gradle/wrapper/gradle-wrapper.jar`
> - `gradle/wrapper/gradle-wrapper.properties`

## Testing
Unit tests are included for:
- ViewModels (leagues/search/teamdetail)
- Repository (teams)

Run:
- From Android Studio: right click a test package → Run tests
- Or from terminal:
    - `./gradlew test`

## AI Usage (as required by assessment policy)
AI tools were used to accelerate implementation, review architecture, and fix common Compose/Flow/Hilt edge cases.
All generated or suggested code was validated by:
- compiling/linting (where possible)
- ensuring dependency direction remained correct
- verifying state behavior and navigation flows
- confirming API endpoint usage matched TheSportsDB free tier constraints

### Examples of how AI was used
1. **Architecture guidance**
    - Prompts like:
        - “Propose a modular MVVM structure for leagues/teams/team detail with clean dependency direction.”
        - “Suggest a state model for Compose screens including loading/error/empty/content.”

2. **Error handling & networking**
    - Prompts like:
        - “Design a `safeApiCall` wrapper and error mapper for Retrofit calls including timeouts and HTTP 429.”

3. **Room favorites + flows**
    - Prompts like:
        - “How do I expose favorites as Flow from Room and integrate with ViewModel state?”

4. **Unit testing patterns**
    - Prompts like:
        - “Write a coroutine-based ViewModel unit test using a MainDispatcherRule and fake repositories.”

### UI Prototyping using Google Stitch

I used Google Stitch (https://stitch.withgoogle.com/) to generate UI concepts and layouts using natural language prompts.

This helped accelerate UI ideation and validate layout hierarchy, loading states, and content presentation.

The generated designs were used as visual references, and all production code was implemented manually using Jetpack Compose, following MVVM and Clean Architecture principles.

## Notes / Potential Improvements
- Increase test coverage (favorites, leagues repo, mapper/error mapper tests)
- Consider keeping domain modules pure Kotlin (avoid UI/navigation DI deps)
- Add more UI polish/accessibility checks if time permits
- Move Hardcoded strings away from the UI into Strings.xml

## Screenshots
### App Working
<div>
    <img src="https://github.com/abhishekds94/SportsApp/blob/master/screenshots/1.png" width="180">
    <img src="https://github.com/abhishekds94/SportsApp/blob/master/screenshots/2.png" width="180">
    <img src="https://github.com/abhishekds94/SportsApp/blob/master/screenshots/3.png" width="180">
    <img src="https://github.com/abhishekds94/SportsApp/blob/master/screenshots/4.png" width="180">
</div>

<div>
    <img src="https://github.com/abhishekds94/SportsApp/blob/master/screenshots/5.png" width="180">
    <img src="https://github.com/abhishekds94/SportsApp/blob/master/screenshots/6.png" width="180">
    <img src="https://github.com/abhishekds94/SportsApp/blob/master/screenshots/7.png" width="180">
    <img src="https://github.com/abhishekds94/SportsApp/blob/master/screenshots/8.png" width="180">
</div>

<div>
    <img src="https://github.com/abhishekds94/SportsApp/blob/master/screenshots/9.png" width="180">
    <img src="https://github.com/abhishekds94/SportsApp/blob/master/screenshots/10.png" width="180">
    <img src="https://github.com/abhishekds94/SportsApp/blob/master/screenshots/11.png" width="180">
    <img src="https://github.com/abhishekds94/SportsApp/blob/master/screenshots/12.png" width="180">
</div>

### UI design using Stitch with Google

<img src="https://github.com/abhishekds94/SportsApp/blob/master/screenshots/UI%20design%20using%20AI.png">
