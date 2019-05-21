import 'package:flutter/material.dart';
import 'mnemonic_enter_derivation_path_widget.dart';

class MnemonicEnterDerivationPathApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: '',
      debugShowCheckedModeBanner: false,
      routes: {
        '/': (context) => MnemonicEnterDerivationPathWidget(),
      },
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
    );
  }
}
