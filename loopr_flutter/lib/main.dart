import 'package:flutter/material.dart';
import 'MyHomeApp.dart';
import 'QRCodeApp.dart';

// Android
void main() => runApp(
  new MaterialApp(
    home: MyApp(),
    debugShowCheckedModeBanner: false,
    routes: <String, WidgetBuilder> {
      '/qrCode': (BuildContext context) => QRCodeApp(),
      '/default': (BuildContext context) => QRCodeApp(),
    },
  )
);

// iOS
// https://github.com/flutter/flutter/issues/22356
@pragma('vm:entry-point')
void qrCode() => runApp(QRCodeApp());
