# Android TV Media Rating App

This repository contains the Android TV frontend for browsing media items and submitting ratings using a Leanback UI with the Ocean Professional theme.

Key features:
- Browse screen with DPAD navigable media cards
- Details screen with poster, synopsis placeholder, and Rate action
- Rating modal dialog (1–5 via like/love/dislike mapping) with in-memory persistence
- API client layer with Retrofit interfaces matching documented endpoints
- Feature flag to toggle between mock and real API

Project structure:
- android_tv_frontend/app/src/main/java/com/example/android_tv_frontend/
  - data/api: Retrofit interfaces and models
  - data/mock: Mock services with placeholder data
  - domain: Repositories (Ratings, Content, APA)
  - ui: ViewModel and UI Fragments (browse, details, rating)

Ocean Professional theme:
- Implemented in res/values/colors.xml and styles.xml
- Uses blue (#2563EB), amber (#F59E0B), and accessible text colors

Switch between mock and real API:
- Default build uses mock data: BuildConfig.USE_MOCK_API = true
- To switch to real API:
  1. Set buildConfigField("Boolean", "USE_MOCK_API", "false") in build.gradle or use Release build.
  2. Set BuildConfig.API_BASE_URL to your backend base URL (https://host).
  3. Ensure endpoints:
     - GET /likes/v1/like?group_id=...
     - POST /likes/v1/like
     - GET /content/v1/data?group_id=...
     - GET /apa/metadata
     - GET /apa/assets

Build:
- cd android_tv_frontend
- ./gradlew :app:assembleDebug

Run:
- Use Android TV emulator or device; app launches into Browse, select a card to open Details, then press Rate.

Error and empty states:
- If metadata legend keys are missing, the literal key names are used as placeholders.
- If legend values are empty, UI reserves space but shows no text.

