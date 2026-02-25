
## GitHub Users App Documentation

### Overview
The GitHub Users App allows users to browse public GitHub accounts and view detailed profile information. It supports offline caching, paginated browsing, and resilient state handling to ensure smooth performance even under unreliable network conditions. The app is designed with scalability, testability, and clean separation of concerns in mind.


### Requirements

#### Functional Requirements
- Display a paginated list of GitHub users
- Navigate to a detailed user profile screen
- Show user information including avatar, bio, followers, and public repositories
- Cache users locally for offline access
- Automatically retry paging when network reconnects
- Display loading, success, and error states appropriately
- Allow retry on failed detail fetch

#### Non-Functional Requirements

- Light and dark Mode
- Fast startup with immediate cached data display
- Offline-first behavior using Room as the local database
- Graceful handling of API errors
- Smooth scrolling with Paging 3
- Lifecycle-aware state management
- Scalable and maintainable architecture

### High-Level Design

#### Architecture

The app follows **Clean Architecture with MVVM**, ensuring a clear separation of concerns and long-term scalability.

Clean Architecture divides the project into three layers with dependencies flowing inward toward the domain layer. This keeps business logic independent of frameworks and easy to test.


### Package Structure

Organized by layer at the top level, by feature within presentation.

- **data** — split into `local` (Room) and `remote` (Retrofit), with shared `repository` and `util` packages
- **domain** — pure Kotlin models, repository interfaces, and use cases
- **di** — Hilt modules
- **presentation** — features organized by screen, with shared components, navigation, and design system


### Implementation Details

#### Data Flow
User actions trigger ViewModel functions → use cases → repositories.
Repositories emit cached data first, fetch from network, then update the DB — ensuring offline resilience and a responsive UI.

#### Networking
Retrofit + OkHttp for API calls. Responses wrapped in `BaseResponse` (Loading, Success, Error) with centralized error handling.

#### Pagination
Paging 3 with `RemoteMediator`. GitHub's `since` parameter accepts the last fetched user id as the next page pointer, so no `RemoteKeys` table is needed.

#### Caching (SSOT)
Room is the single source of truth. The user list is fetched via `RemoteMediator` and read from DB. User detail emits cached data first, then updates from network. On failure, cached data remains visible with a retry option.

#### State Management
Each screen has a ViewModel exposing a single `StateFlow` of a sealed class state (Loading, Success, Error). UI observes via `collectAsState()`.

### Libraries Used

- **UI**: Jetpack Compose, Material 3
- **Dependency Injection**: Hilt
- **Navigation**: Navigation Compose (type-safe destinations)
- **Networking**: Retrofit, OkHttp
- **Local Storage**: Room
- **Pagination**: Paging 3
- **Asynchronous Work**: Kotlin Coroutines + Flow
- **Image Loading**: Coil
- **Logging**: Timber

---

### Code Style & Naming Conventions

- **Classes**: PascalCase (e.g., `UserDetailViewModel`, `UserRemoteMediator`)
- **Properties / Variables**: camelCase (e.g., `networkChecker`, `repository`)

---

### Branch Naming

Branches follow a type-based structure:

---

#### Potential Improvements

- Add search functionality for users
- Include unit test coverage
- Add UI tests for Compose screens
- Linting and code formatting
- CI/CD pipeline integration
---

#### Screenshots

For visual references of the app UI, check:

```
docs/screenshots/

````

Screens include:
- User List Screen
- User Detail Screen
- Error State
---

#### How to Build and Run

```bash
git clone <repo-url>
cd GithubUsersApp
````

* Open in Android Studio
* Sync Gradle
* Run on emulator or device (minSdk 24+)

No API key required — uses public GitHub REST API.

##### Known Limitations
- The user list requires at least one successful network fetch before it works offline
- If a user's detail was never fetched, only their avatar and login are shown when offline
- There is no cache expiry, so stale data may appear without a manual refresh
- Pagination assumes GitHub's `since` parameter always accepts the last fetched user id as a next page indicator

##### References
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [GitHub REST API](https://docs.github.com/en/rest/users/users)

