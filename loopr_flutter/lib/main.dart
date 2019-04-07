import 'package:flutter/material.dart';
import 'my_app/my_app.dart';
import 'qr_code/qr_code_app.dart';

// Android
// Refer https://github.com/flutter/flutter/issues/10813
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
