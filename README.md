# Kisetsu

Kisetsu is an Android music player based on **Kisetsu 2.0.4-nightly**, rebuilt as a standalone, separately signed app.

## Highlights

- YouTube Music streaming and local library playback
- Modern player with lyrics, queue, downloads and offline mode
- Widgets, Android Auto support, and optional Google Cast (GMS flavor)
- FOSS and GMS flavors, multiple ABI variants

## Build

Requirements: JDK 21 and Android SDK.

```bash
./gradlew :app:assembleUniversalFossRelease
```

Release signing uses `app/keystore/release.keystore` with credentials from `local.properties` (`STORE_PASSWORD`, `KEY_ALIAS`, `KEY_PASSWORD`).

## App identity

- Name: **Kisetsu**
- Application ID: `com.kisetsu.music`
- Icon: black music note on white background

## License

Licensing inherited from the upstream Kisetsu project. See [LICENSE](LICENSE).
