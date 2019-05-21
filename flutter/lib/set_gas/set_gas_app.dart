import 'package:flutter/material.dart';
import 'set_gas_widget.dart';

class SetGasApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: '',
      debugShowCheckedModeBanner: false,
      routes: {
        '/': (context) => SetGasWidget(),
      },
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
    );
  }
}
