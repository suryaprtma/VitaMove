# Analisis Keseluruhan Project: My Fitness Pro (VitaMove)

## 1. Gambaran Umum

Aplikasi **My Fitness Pro** (branding: **VitaMove**) adalah aplikasi kebugaran Android native yang dibangun dengan **Kotlin + Jetpack Compose + Material3**. Aplikasi ini masih dalam tahap pengembangan dengan arsitektur **Single-Activity** dan navigasi manual berbasis state (`when (currentScreen)`).

---

## 2. Arsitektur Aplikasi

### Pola: Single-Activity + Compose State Routing

```
MainActivity.kt (1 Activity)
    |
    |-- setContent { when (currentScreen) { ... } }
    |
    ├── 1 ViewModel: AuthViewModel
    ├── 1 Repository: AuthRepository
    ├── 0 Dependency Injection (tanpa Hilt/Koin)
    └── Navigasi: manual MutableState<String>
```

### Komponen Utama:

| Layer | File | Fungsi |
|-------|------|--------|
| **Entry Point** | `MyApplicationApp.kt` | Application class, inisialisasi Firebase |
| **Activity** | `MainActivity.kt` | Router utama, state navigasi global (currentScreen, waterCount, dll) |
| **ViewModel** | `AuthViewModel.kt` | Satu-satunya ViewModel, handle login/register/logout via StateFlow |
| **Repository** | `AuthRepository.kt` | Baca/tulis data user ke SharedPreferences dengan Gson |
| **Session** | `SessionManager.kt` | Wrapper SharedPreferences untuk session aktif |
| **Theme** | `ui/theme/` | Color, Theme, Type — Material3 dengan dynamic color + ungu gradien |

---

## 3. Database yang Digunakan

### A. Database Saat Ini: SharedPreferences + Gson

**BUKAN database sesungguhnya.** Data disimpan sebagai string JSON di SharedPreferences.

| SharedPreferences | Isi |
|------------------|-----|
| `user_session` | `uid`, `email`, `username`, `fullName`, `isLoggedIn` |
| `user_db_permanent` | `Map<String, Pair<User, String>>` — semua user terdaftar (key: email) |

**Cara kerja:**
- Seluruh "database" user adalah `MutableMap<String, Pair<User, String>>` (key = email, value = User + password)
- Di-serialize ke JSON pakai **Gson**, disimpan ke SharedPreferences
- Setiap operasi login/register baca seluruh map, proses, lalu tulis ulang
- Password disimpan dalam bentuk **plaintext** (aman, BCrypt sudah di-dependency tapi tidak dipakai)
- Default admin: `admin@fitnes.com` / `admin123`

### B. Room Database (Tidak Aktif)

| File | Status |
|------|--------|
| `data/AppDatabase.kt` | **Dikosongkan** — tidak ada instance database |
| `data/UserDao.kt` | **Dikosongkan** — tidak bisa query |
| `data/UserEntity.kt` | Masih ada definisi `@Entity(tableName = "user_profile")` tapi tidak pernah dipakai |

Room sudah di-dependency (`room-runtime:2.6.1`, `room-ktx:2.6.1`) tapi **tidak diinisialisasi** dan **tidak dipanggil** di kode mana pun.

### C. Firebase (Belum Aktif)

| Komponen | Status |
|----------|--------|
| `firebase-bom:33.0.0` | ✅ Dependency ada |
| `firebase-auth-ktx:22.3.1` | ✅ Dependency ada |
| `firebase-firestore-ktx:24.10.3` | ✅ Dependency ada |
| `firebase-analytics:21.5.1` | ✅ Dependency ada |
| `google-services.json` | ✅ File sudah ada di `app/` |
| `Firebase.initialize(this)` | ✅ Dipanggil di `MyApplicationApp.kt` |
| **API Firebase (`signIn`, `createUser`, `collection`, dll)** | **❌ BELUM ADA SATU PUN PANGGILAN** |

Firebase sudah di-setup secara infrastruktur tapi **belum diintegrasikan secara fungsional**.

---

## 4. Flow Navigasi

```
App Launch
    │
    ▼
"auth" screen ────────────────────────────────────────┐
    │                                                  │
    ├── Login sukses? ──► "success_login"             │
    │                          │                       │
    │                          ▼                       │
    │                   SuccessLoginScreen             │
    │                   (animasi 3 detik)              │
    │                          │                       │
    │            ┌─────────────┴──────────────┐        │
    │            ▼                            ▼        │
    │   (isNewUser=true)              (isNewUser=false) │
    │            │                            │        │
    │            ▼                            ▼        │
    │   "gender_selection"               "home" ───────┘
    │            │                            │
    │            ▼                            ├── "profile" → ProfileScreen
    │   "age_selection"                       ├── "workout_detail"
    │            │                            │       └→ WorkoutDetailScreen
    │            ▼                            │           (timer exercise)
    │   "weight_selection"                    │
    │            │                            ▼
    │            ▼                    (onLogoutClick)
    │   "height_selection"               "auth"
    │            │
    │            ▼
    │         "home"
    │
    └── (onLogout) ──► "auth"
```

### Detail Navigasi:

- **Tidak menggunakan Jetpack Navigation** — navigasi murni `when (currentScreen)` di `setContent {}`
- **State global** di-maintain di `MainActivity.kt`:
  - `currentScreen: MutableState<String>` — navigasi utama
  - `waterCount: MutableState<Int>` — dibagi antara HomeScreen dan ProfileScreen
  - `selectedExercise: MutableState<String>` — dari Home ke WorkoutDetail
  - `isNewUserProcess: MutableState<Boolean>` — gate onboarding setelah register

### Screen yang Ada:

| Screen | File | Status |
|--------|------|--------|
| SplashScreen | `SplashScreen.kt` | ❌ Didefinisikan tapi tidak dipanggil |
| AuthScreen | `AuthScreen.kt` | ✅ Container login/register |
| LoginScreen | `LoginScreen.kt` | ✅ Form login |
| RegistrationScreen | `RegistrationScreen.kt` | ✅ Form register |
| SuccessLoginScreen | `SuccessLoginScreen.kt` | ✅ Animasi sukses 3 detik |
| GenderSelectionScreen | `GenderSelectionScreen.kt` | ✅ Onboarding gender |
| AgeSelectionScreen | `AgeSelectionScreen.kt` | ✅ Onboarding usia (LazyRow) |
| WeightSelectionScreen | `WeightSelectionScreen.kt` | ✅ Onboarding berat (ruler) |
| HeightSelectionScreen | `HeightSelectionScreen.kt` | ✅ Onboarding tinggi (ruler) |
| HomeScreen | `HomeScreen.kt` | ✅ Dashboard utama |
| ProfileScreen | `ProfileScreen.kt` | ✅ Profil + water tracker |
| WorkoutDetailScreen | `WorkoutDetailScreen.kt` | ✅ Timer exercise |
| TrainingScreen | `TrainingScreen.kt` | ❌ Didefinisikan tapi tidak dipanggil |
| ReadyToWorkoutScreen | `ReadyToWorkoutScreen.kt` | ❌ Didefinisikan tapi tidak dipanggil |

---

## 5. Data Flow

```
USER INTERACTION (Login/Register)
    │
    ▼
Composable Screen (LoginScreen / RegistrationScreen)
    │  panggil callback onLoginClick / onRegisterClick
    ▼
AuthScreen.kt
    │  panggil authViewModel.login() / authViewModel.register()
    ▼
AuthViewModel.kt
    │  set _isLoading = true
    │  buat LoginRequest / RegisterRequest
    │  launch coroutine → authRepository.login() / .register()
    ▼
AuthRepository.kt
    │  getUserDatabase() → baca SharedPreferences "user_db_permanent"
    │  parse JSON → MutableMap<String, Pair<User, String>>
    │  login:  cari email, cocokkan password (plaintext)
    │  register: cek duplikat, buat User baru, simpan ke map
    │  saveUserDatabase() → serialize map → tulis ke SharedPreferences
    │  saveSession() → tulis ke SharedPreferences "user_session"
    │  return AuthResult.Success / Error
    ▼
AuthViewModel.kt
    │  update _loginState / _registerState
    │  update _currentUser
    ▼
MainActivity.kt
    │  LaunchedEffect observe StateFlow
    │  jika Success → ganti currentScreen
    ▼
SCREEN BERIKUTNYA (Home / Onboarding)
```

---

## 6. Struktur File Lengkap

```
app/src/main/java/com/example/myapplication/
│
├── MyApplicationApp.kt          # Application class, Firebase.init
├── MainActivity.kt              # Router utama + state navigasi global
│
├── AuthScreen.kt                # Container login/register
├── LoginScreen.kt               # Form login
├── RegistrationScreen.kt        # Form registrasi
├── SuccessLoginScreen.kt        # Animasi setelah login berhasil
├── SplashScreen.kt              # Splash screen (TIDAK DIPAKAI)
│
├── GenderSelectionScreen.kt    # Onboarding: pilih gender
├── AgeSelectionScreen.kt       # Onboarding: pilih usia
├── WeightSelectionScreen.kt    # Onboarding: pilih berat badan
├── HeightSelectionScreen.kt    # Onboarding: pilih tinggi badan
│
├── HomeScreen.kt               # Dashboard utama
├── TrainingScreen.kt           # Layar persiapan workout (TIDAK DIPAKAI)
├── ReadyToWorkoutScreen.kt     # Layar siap workout (TIDAK DIPAKAI)
├── WorkoutDetailScreen.kt      # Timer exercise
├── ProfileScreen.kt            # Profil + water tracker + logout
│
├── data/
│   ├── AppDatabase.kt          # Room DB (DIKOSONGKAN)
│   ├── UserDao.kt              # Room DAO (DIKOSONGKAN)
│   ├── UserEntity.kt           # Room Entity (TIDAK DIPAKAI)
│   ├── UserRepository.kt       # Repository lama (DIKOSONGKAN)
│   ├── model/
│   │   └── User.kt             # Data model + AuthResult sealed class
│   ├── repository/
│   │   └── AuthRepository.kt   # Repository aktif (SharedPrefs-based)
│   └── preferences/
│       └── SessionManager.kt   # Wrapper SharedPreferences session
│
├── ui/
│   └── theme/
│       ├── Color.kt            # Warna Material3
│       ├── Theme.kt            # Tema dengan dynamic color
│       └── Type.kt             # Tipografi
│
└── viewmodel/
    └── AuthViewModel.kt        # Satu-satunya ViewModel
```

---

## 7. Ringkasan Status

| Aspek | Status Saat Ini |
|-------|----------------|
| **Arsitektur** | Single-Activity, Compose state routing, 1 ViewModel, tanpa DI |
| **Navigasi** | Manual `when (currentScreen)` — tidak pakai Jetpack Navigation |
| **Database Aktif** | SharedPreferences + Gson (bukan database sungguhan) |
| **Room Database** | Entity ada, DAO & Database dikosongkan — **tidak aktif** |
| **Firebase Auth** | Dependency & init done — **tidak ada panggilan API** |
| **Firebase Firestore** | Dependency & init done — **tidak ada panggilan API** |
| **Penyimpanan Password** | **Plainttext** (BCrypt di dependency tapi tidak dipakai) |
| **Akun Default** | `admin@fitnes.com` / `admin123` |
| **UI Framework** | Jetpack Compose + Material3, tema ungu gradien "VitaMove" |
| **Screen Tidak Terpakai** | SplashScreen, TrainingScreen, ReadyToWorkoutScreen |

---

*Dokumen Analisis Teknis - My Fitness Pro App*
