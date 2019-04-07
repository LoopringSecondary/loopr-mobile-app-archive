import 'package:flutter/material.dart';
import 'my_app/my_app.dart';
import 'order_detail/order_detail_app.dart';
import 'qr_code/qr_code_app.dart';
import 'send_list_choose/send_list_choose_app.dart';

// Android
// Refer https://github.com/flutter/flutter/issues/10813
void main() => runApp(
  new MaterialApp(
    home: MyApp(),
    debugShowCheckedModeBanner: false,
    routes: <String, WidgetBuilder> {
      '/orderDetail': (BuildContext context) => OrderDetailApp(),
      '/qrCode': (BuildContext context) => QRCodeApp(),
      '/sendListChoose': (BuildContext context) => SendListChooseApp(),
      '/default': (BuildContext context) => QRCodeApp(),
    },
  )
);

// iOS
// https://github.com/flutter/flutter/issues/22356
@pragma('vm:entry-point')
void orderDetail() => runApp(OrderDetailApp());

@pragma('vm:entry-point')
void qrCode() => runApp(QRCodeApp());

@pragma('vm:entry-point')
void sendListChoose() => runApp(SendListChooseApp());
