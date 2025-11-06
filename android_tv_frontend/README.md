# Android TV Frontend

How to switch mock vs real API:
- Debug build: Mock API enabled (BuildConfig.USE_MOCK_API = true).
- Release build: Real API expected (BuildConfig.USE_MOCK_API = false).
- You can change these in app/build.gradle.kts defaultConfig/buildTypes.

Configure real API:
- Update BuildConfig.API_BASE_URL in app/build.gradle.kts to your backend base URL.
- Ensure endpoints exposed as described in kavia-docs/android-tv-media-rating-design-architecture.md.
