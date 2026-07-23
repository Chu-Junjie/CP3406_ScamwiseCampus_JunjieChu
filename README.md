# ScamWise Campus

**Pause. Check. Protect.**

ScamWise Campus is an educational Android application that helps students practise safer digital judgement when responding to suspicious messages, links, marketplace offers, banking requests, impersonation attempts, and other common scam situations.

The application provides interactive scam scenarios, structured decision-making activities, immediate educational feedback, locally stored progress statistics, and an optional Google Safe Browsing URL verification feature.

---

## Project Information

- **Application name:** ScamWise Campus
- **Platform:** Android
- **Language:** Kotlin
- **UI framework:** Jetpack Compose
- **Design system:** Material 3
- **Minimum SDK:** API 24
- **Target SDK:** API 36
- **Package name:** `com.chujunjie.scamwisecampus`

---

## Main Features

### Interactive Scam Practice

Users can practise recognising and responding to scam scenarios across the following categories:

- Job scams
- Banking scams
- Parcel scams
- Marketplace scams
- Impersonation scams
- Phishing scams

Each scenario guides the user through four learning steps:

1. Assess the level of risk.
2. Identify warning signs.
3. Choose the safest next action.
4. Rate confidence in the decision.

After submission, the app provides a score and educational feedback explaining the safer response.

### Local Practice History

Completed attempts are stored locally using Room.

The app records learning information such as:

- Scenario category
- Difficulty
- Risk-assessment result
- Safe-action result
- Confidence level
- Confidence calibration
- Total score

No account or cloud storage is required.

### Statistics Dashboard

The Statistics screen presents:

- Total completed attempts
- Average score
- Highest score
- Risk-assessment accuracy
- Safe-action accuracy
- Confidence calibration
- Performance by scam category
- Recent attempts
- Recommended practice category

### Link Verification Lab

Users may optionally submit an HTTP or HTTPS URL to Google Safe Browsing.

Before a URL is submitted, the app:

- Explains what information will be transmitted
- Requires explicit user consent
- Encourages the removal of personal tokens where possible
- Explains that the result is guidance rather than a guarantee

The feature checks for known threat matches, including:

- Social engineering
- Malware
- Unwanted software
- Potentially harmful applications

### Settings

Users can:

- Follow the device theme
- Select light mode
- Select dark mode
- Clear all locally stored practice history
- Review privacy information

Deleting practice history requires confirmation because the action cannot be undone.

---

## Application Screens

The application contains four main top-level destinations:

| Screen | Purpose |
|---|---|
| Home | Introduces the app and provides quick access to recommended activities |
| Practice | Lists the available scam-awareness scenarios |
| Statistics | Displays locally calculated learning progress |
| Settings | Controls appearance and locally stored data |

Additional screens include:

- Scenario Activity
- Scenario Result
- Link Verification Lab

---

## Architecture

The project follows a layered Android architecture.

```text
UI
├── Jetpack Compose screens
├── UI state
└── ViewModels

Domain
├── Models
├── Scenario evaluation
└── Learning and scoring logic

Data
├── Repository implementations
├── Room database
├── DataStore preferences
└── Google Safe Browsing service
```

The main architectural components include:

- Jetpack Compose
- Material 3
- Navigation Compose
- ViewModel
- Kotlin Coroutines
- StateFlow
- Repository pattern
- Room
- Preferences DataStore
- Koin dependency injection
- Retrofit
- Kotlin serialization

---

## Project Structure

```text
app/src/main/java/com/chujunjie/scamwisecampus
├── data
│   ├── local
│   ├── remote
│   └── repository
├── di
├── domain
│   └── model
├── ui
│   ├── navigation
│   ├── screens
│   └── theme
├── MainActivity.kt
└── ScamWiseApplication.kt
```

---

## Data Storage

### Room

Room stores completed scenario attempts in the app's private local database.

The stored information is used to calculate the Statistics screen.

### Preferences DataStore

Preferences DataStore stores the selected theme mode:

- System
- Light
- Dark

### Data Deletion

Users can permanently delete all practice history from the Settings screen.

The app displays a confirmation dialog before deletion and provides feedback after the operation finishes.

---

## Internet API

The Link Verification Lab uses the Google Safe Browsing API.

A request is made only when the user:

1. Enters a valid HTTP or HTTPS URL.
2. Reviews the disclosure.
3. Provides explicit consent.
4. Presses **Check URL**.

The URL is not submitted automatically while the user is typing.

A Safe Browsing result does not prove that a website is safe. The app continues to recommend independent verification of the sender, domain, request, and official service channel.

---

## API Key Configuration

The Safe Browsing API key is not committed to GitHub.

Create or update the project-level `local.properties` file:

```properties
sdk.dir=D\:\\Android\\Sdk
SAFE_BROWSING_API_KEY=YOUR_API_KEY_HERE
```

Replace the SDK path with the Android SDK location on your computer.

Do not commit `local.properties`. The project `.gitignore` excludes this file.

After adding or changing the API key, rebuild the application:

```powershell
.\gradlew.bat :app:assembleDebug
```

---

## Privacy and Security

ScamWise Campus follows a privacy-conscious design.

The application:

- Does not require an account
- Does not request access to contacts
- Does not request location
- Does not read messages
- Does not read personal files
- Does not upload practice history
- Stores learning data locally
- Requests consent before submitting a URL
- Allows users to delete local practice history
- Keeps the API key outside version control

The app is an educational tool and does not guarantee that a URL, message, person, or service is safe.

---

## Accessibility

Accessibility improvements include:

- Semantic page headings
- Clear visible button labels
- Accessible navigation labels
- Merged semantics for radio-button and checkbox cards
- Dynamic announcements for validation errors and results
- Descriptive loading, empty, and error states
- Material touch targets
- Support for light and dark themes
- Scrollable layouts for smaller screens
- Large-font and landscape testing

Decorative icons do not create unnecessary TalkBack announcements.

---

## Testing

The project includes local unit tests and Android instrumented tests.

### Unit Tests

Unit tests cover areas such as:

- Scenario scoring
- Risk assessment
- Warning-sign evaluation
- Safe-action evaluation
- Confidence calibration
- Statistics calculations
- ViewModel state changes
- URL validation
- Link-verification result handling

Run the unit tests with:

```powershell
.\gradlew.bat :app:testDebugUnitTest
```

### Instrumented Tests

Instrumented tests cover:

- Room database operations
- Home screen behaviour
- Statistics screen states
- Link Verification screen behaviour
- Jetpack Compose UI interaction

Run the instrumented tests with an emulator or physical Android device:

```powershell
.\gradlew.bat :app:connectedDebugAndroidTest
```

### Lint

Run Android lint with:

```powershell
.\gradlew.bat :app:lintDebug
```

### Debug Build

Build the debug APK with:

```powershell
.\gradlew.bat :app:assembleDebug
```

---

## Running the Project

### Requirements

- Android Studio
- JDK 17
- Android SDK API 36
- Android emulator or physical Android device
- Internet connection for Link Verification
- Google Safe Browsing API key for live URL checks

### Steps

1. Clone the repository:

```powershell
git clone https://github.com/Chu-Junjie/CP3406_ScamwiseCampus_JunjieChu
```

2. Open the project in Android Studio.
3. Allow Gradle to synchronise dependencies.
4. Add the Safe Browsing API key to `local.properties`.
5. Start an Android emulator.
6. Run the app from Android Studio or use:

```powershell
.\gradlew.bat :app:installDebug
```

---

## Build Output

After running:

```powershell
.\gradlew.bat :app:assembleDebug
```

the debug APK is generated at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

---

## Known Limitations

- Google Safe Browsing identifies known threat matches and may not detect newly created or previously unreported threats.
- A URL with no known threat match is not guaranteed to be safe.
- Practice scenarios are educational simulations rather than live message analysis.
- Statistics are stored only on the current device.
- Removing the app or clearing its application data removes locally stored progress.
- The application currently supports English content.

---

## Ethical Use

ScamWise Campus is designed for education and scam-awareness practice.

It must not be presented as a replacement for:

- Official fraud-reporting services
- Financial institutions
- Law-enforcement advice
- Professional cybersecurity investigation
- Independent verification through official channels

Users should avoid interacting with suspicious links and should contact the relevant organisation using independently verified contact information.

---

## Author

**Junjie Chu**

James Cook University  
CP3406 Mobile Computing
