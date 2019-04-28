# loopr-flutter-module

### References
- [Install Flutter](https://flutter.dev/docs/get-started/install/macos)
- [Add Flutter to existing apps](https://github.com/flutter/flutter/wiki/Add-Flutter-to-existing-apps)
- [Flutter and Native Communication](https://blog.testfairy.com/flutter-and-native-communication/)
- [Flutter 实现原理及实践](https://mp.weixin.qq.com/s/-nOsT8yQTvPAojmGIO7Yzg)
- [Introduction to Flutter: Building iOS and Android Apps from a Single Codebase](https://www.appcoda.com/flutter-basics/)
- [How to develop a platform channel in Flutter between Dart and Native Code](https://medium.com/@atul.sharma_94062/creating-a-bridge-in-flutter-between-dart-and-native-code-in-java-or-objectivec-5f80fd0cd713)
- [Introduction to Redux in Flutter](https://blog.novoda.com/introduction-to-redux-in-flutter/)
- [flutter_redux](https://github.com/brianegan/flutter_redux)

### Features that will be written in Flutter
- Chat
- QR code (Completed)
- Setting
- Setting detail
- dialog_wallet_create_success
- Airdrop
- Add Contact
- Add Tokens
- Order list
- Order detail (Completed)
- P2P list
- P2P detail
- Asset detail
- Transaction detail
- Set gas
- Select token to send
- Switch send token
- Order detail view
- Backup mnemonics (Completed)
- News

### UI view and data
An app is always to render data in a UI view. flutter-module is primary for UI view and should be stateless. How to fetch data, process data and store data are in iOS and Android native code.

### How to add a view
[Navigate with named routes](https://flutter.dev/docs/cookbook/navigation/named-routes)
[Flutter: Advance Routing and Navigator](https://medium.com/@nitishk72/flutter-advance-routing-and-navigator-df0f86f0974f)

### Open questions
- How to use FlutterDartProject https://stackoverflow.com/questions/55127464/how-to-use-flutterdartproject
- Flutter setState changing, but not rerendering https://stackoverflow.com/questions/52920963/flutter-setstate-changing-but-not-rerendering