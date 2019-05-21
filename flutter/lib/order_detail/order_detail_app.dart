import 'package:flutter/material.dart';
import 'order_detail_widget.dart';

class OrderDetailApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: '',
      debugShowCheckedModeBanner: false,
      routes: {
        '/': (context) => OrderDetailWidget(),
      },
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
    );
  }
}
