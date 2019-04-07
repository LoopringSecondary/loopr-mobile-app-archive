import 'package:flutter/material.dart';
import 'send_list_chosse_widget.dart';

class SendListChooseApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      debugShowCheckedModeBanner: false,
      routes: {
        '/': (context) => SendListChooseWidget(title: 'second'),
      },
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
    );
  }
}
