# Payment Chaser — Native Android Application

> "You do the work. We chase the payment."

Payment Chaser is a native Android application built with **Kotlin** and **Jetpack Compose**, implementing **Material Design 3**, **Clean Architecture**, and **MVVM**. It tracks outstanding client payments, automates multi-stage reminder schedules, handles partial settlements, and provides one-tap WhatsApp and Email intent generation.

---

## 1. Architecture & Component Status

| Category | Component | Status | Notes |
| :--- | :--- | :--- | :--- |
| **UI Layer** | Onboarding Screen | **IMPLEMENTED** | Multi-step guidance (Business name, currency, permissions) |
| **UI Layer** | Authentication Screen | **IMPLEMENTED** | Firebase Email/Password, registration, reset, persistent session |
| **UI Layer** | Dashboard Screen | **IMPLEMENTED** | Real-time Room flow, hero balance card, 3-metric KPI grid |
| **UI Layer** | Payments List Screen | **IMPLEMENTED** | Search, filter chips (All, Overdue, Pending, Paid), left status bar |
| **UI Layer** | Add Payment Screen | **IMPLEMENTED** | < 30s logging, presets (+$500 to +$5k), client autofill, Net presets |
| **UI Layer** | Payment Details Screen | **IMPLEMENTED** | Breakdown, timeline audit log, record payment modal, delete guard |
| **UI Layer** | Clients Screen | **IMPLEMENTED** | Client list, search, add/edit, active debt deletion protection |
| **UI Layer** | Smart Reminders Screen| **IMPLEMENTED** | Master Autopilot switch, 3 tones, 4-step cadence customizer |
| **UI Layer** | Analytics Screen | **IMPLEMENTED** | Collection rate %, total collected/overdue, top debtors |
| **UI Layer** | Settings Screen | **IMPLEMENTED** | Profile, currency switcher, manual cloud sync, sign out |
| **UI Layer** | Premium Screen | **IMPLEMENTED** | Monthly/Annual switcher, tier comparison, Play Billing hook |
| **Local DB** | Room Database | **IMPLEMENTED** | `PaymentEntity`, `ClientEntity`, `ReminderLogEntity`, DAOs, TypeConverters |
| **Arithmetic**| Decimal-Safe Currency | **IMPLEMENTED** | Integer cents (`Long`) and `BigDecimal` representation; no float drift |
| **Background**| WorkManager Scheduling| **IMPLEMENTED** | `ReminderScheduler` (-3d, 0d, +3d, +7d), cancellation on paid |
| **Intents** | WhatsApp & Email | **IMPLEMENTED** | Real `ACTION_VIEW` & `ACTION_SENDTO` intents with truthful logging |
| **Cloud Sync**| Cloud Firestore Sync | **IMPLEMENTED** | Bidirectional Room <-> Firestore sync with offline tolerance |
| **Security** | Firestore Security Rules| **IMPLEMENTED** | Enforces user ownership (`request.auth.uid == userId`) |
| **Testing** | Unit Tests | **IMPLEMENTED** | `PaymentCalculationTest` (cents safety, overdue status, installments) |

---

## 2. Directory Structure

```
android/
├── app/
│   ├── build.gradle.kts                      // Target SDK 35, Compose, Hilt, Room, Firebase
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml           // Permissions: INTERNET, POST_NOTIFICATIONS
│   │   │   ├── res/                          // themes.xml, strings.xml, backup_rules.xml
│   │   │   └── java/com/paymentchaser/app/
│   │   │       ├── PaymentChaserApp.kt       // @HiltAndroidApp with NotificationChannel
│   │   │       ├── MainActivity.kt           // Navigation Compose with 11 destinations
│   │   │       ├── data/
│   │   │       │   ├── model/                // PaymentEntity, ClientEntity, ReminderLogEntity
│   │   │       │   ├── local/                // PaymentDao, ClientDao, ReminderLogDao, Database
│   │   │       │   └── repository/           // PaymentRepository, AuthRepository, SyncRepository
│   │   │       ├── di/                       // AppModule (Hilt Database & Repository bindings)
│   │   │       ├── util/                     // ShareIntentsHelper (WhatsApp, Email, SMS)
│   │   │       ├── worker/                   // ReminderWorker & ReminderScheduler
│   │   │       └── ui/
│   │   │           ├── theme/                // Color.kt, Theme.kt, Type.kt (M3 Tokens)
│   │   │           ├── analytics/            // AnalyticsScreen & ViewModel
│   │   │           ├── auth/                 // AuthScreen & ViewModel
│   │   │           ├── clients/              // ClientsScreen & ViewModel
│   │   │           ├── dashboard/            // DashboardScreen & ViewModel
│   │   │           ├── onboarding/           // OnboardingScreen
│   │   │           ├── payment/              // AddPayment, PaymentsList, PaymentDetails
│   │   │           ├── premium/              // PremiumScreen
│   │   │           ├── reminders/            // SmartRemindersScreen & ViewModel
│   │   │           └── settings/             // SettingsScreen & ViewModel
│   │   └── test/
│   │       └── java/com/paymentchaser/app/PaymentCalculationTest.kt
│   └── proguard-rules.pro
├── gradle/
│   └── libs.versions.toml                    // Gradle Version Catalog
├── build.gradle.kts                          // Root build script
├── settings.gradle.kts                       // Settings script
├── firestore.rules                           // User data isolation security rules
└── README.md
```

---

## 3. How to Import and Build in Android Studio

### Prerequisites
1. Android Studio Ladybug (2024.2.1) or newer.
2. Java Development Kit (JDK) 17.
3. Android SDK 35 installed via SDK Manager.

### Step-by-Step Build Instructions

1. **Clone / Open:**
   - In Android Studio, select **File > Open...**
   - Choose the `/android` directory.
   - Wait for Gradle to finish indexing and syncing dependencies.

2. **Firebase Configuration:**
   - Go to your [Firebase Console](https://console.firebase.google.com/).
   - Add an Android App with package name: `com.paymentchaser.app`.
   - Download `google-services.json` and place it in `/android/app/google-services.json`.
   - In Firebase Authentication, enable **Email/Password** and **Google**.
   - In Cloud Firestore, deploy the included `firestore.rules`.

3. **Build Debug APK via CLI:**
   ```bash
   cd android
   ./gradlew assembleDebug
   ```
   Output binary: `app/build/outputs/apk/debug/app-debug.apk`

4. **Run Unit Tests:**
   ```bash
   ./gradlew testDebugUnitTest
   ```

5. **Generate Release AAB for Google Play Store:**
   ```bash
   ./gradlew bundleRelease
   ```
   Output bundle: `app/build/outputs/bundle/release/app-release.aab`

---

## 4. Unverified Items & Physical-Device Test Checklist

Because this web sandbox environment lacks an Android SDK, JDK, and Gradle execution daemon, the following cannot be compiled or verified within this container and must be tested in Android Studio:

- [ ] **Android Studio Gradle Sync:** Verify `google-services.json` resolution with Gradle 8.7.
- [ ] **Physical Device Notification Handshake:** Test Android 13+ runtime permission prompt for `POST_NOTIFICATIONS`.
- [ ] **WhatsApp Intent Handshake:** Confirm `Intent.ACTION_VIEW` resolves cleanly on devices with WhatsApp installed and handles devices without it.
- [ ] **WorkManager Alarm Manager Exemption:** Verify background reminder execution across OEM battery optimizations (e.g., Samsung, Xiaomi).
- [ ] **Google Play Billing Setup:** Configure product IDs in Google Play Console before testing `BillingClient` purchase triggers.
