import 'package:flutter/material.dart';
import 'qr_code_widget.dart';

class QRCodeApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    print("QRCodeApp");
    return MaterialApp(
      title: '',
      debugShowCheckedModeBanner: false,
      routes: {
        '/': (context) => QRCodeWidget(),
      },
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
    );
  }
}
