import 'package:flutter/material.dart';
import 'backup_mnemonic_widget.dart';

class BackupMnemonicApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: '',
      debugShowCheckedModeBanner: false,
      routes: {
        '/': (context) => BackupMnemonicWidget(title: 'second'),
      },
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
    );
  }
}
