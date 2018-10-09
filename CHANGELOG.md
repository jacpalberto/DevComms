# Changelog
Changes will be posted in here. They relate to all apps in all flavors

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.0.1] - 2018-09-21

### Added

- Version to the SplashScreen based on `BuildConfig.VERSION_NAME`
- Added RxKotlin 2.3.0

### Removed

- Remove FirebaseDatabase dependency

### Fixed
- fix forEach bug that crashed
- fix twitter bug that showed twitter even when it was null

### Changed
- Updated version on dependecy: `com.google.firebase:firebase-database` 16.0.1 -> 16.0.2
- Updated version on dependecy: `com.google.firebase:firebase-database` 17.3.1 -> 17.3.2
- Updated kotlin version: kotlin_version = '1.2.70' -> '1.2.71'
- Updated responsesModels to DataResponse to handle errors and status
- Updated migration to AndroidX
- Updated room queries to RxRoom with RxKotlin

[Unreleased]: https://github.com/GDLDevComms/DevComms/compare/v1.0.1...HEAD
[1.0.1]: https://github.com/GDLDevComms/DevComms/compare/v1.0.1...v1.0.0