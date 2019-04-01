import 'package:flutter/material.dart';
import 'QRCodeWidget.dart';

class QRCodeApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
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
