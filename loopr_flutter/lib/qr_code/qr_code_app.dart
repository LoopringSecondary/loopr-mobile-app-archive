import 'package:flutter/material.dart';
import 'qr_code_widget.dart';

class QRCodeApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: '',
      debugShowCheckedModeBanner: false,
      routes: {
        '/': (context) => QRCodeWidget(title: 'second'),
      },
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
    );
  }
}
