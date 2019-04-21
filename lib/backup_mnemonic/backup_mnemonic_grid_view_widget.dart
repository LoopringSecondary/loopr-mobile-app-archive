import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'dart:async';

class BackupMnemonicGridViewWidget extends StatefulWidget {
  BackupMnemonicGridViewWidget({Key key, this.words}) : super(key: key);

  final List<String> words;

  @override
  _BackupMnemonicGridViewWidgetState createState() => _BackupMnemonicGridViewWidgetState();
}

class _BackupMnemonicGridViewWidgetState extends State<BackupMnemonicGridViewWidget> {
  List<String> _words = [];
  static const String methodChannel = "backupMnemonic";

  MethodChannel platform = const MethodChannel('backupMnemonic');

  @override
  void initState() {
    super.initState();
    platform.setMethodCallHandler(methodCallHandler);
  }

  Future<void> methodCallHandler(MethodCall methodCall) async {
    switch (methodCall.method) {
      case 'backupMnemonic.update':
        print(methodCall.arguments);
        setState(() {
          _words = methodCall.arguments;
        });
        break;
      default:
        print("other methods");
    }
  }

  @override
  Widget build(BuildContext context) {

    List<Widget> list = [];
    for(var i = 0; i < _words.length; i++) {
      String word = _words[i];
      int num = i+1;
      list.add(
        new Container(
          padding: EdgeInsets.only(left: 4.0, right: 0.0, top: 0, bottom: 0),
          child: new Text(
            "$num. $word",
            style: TextStyle(
              color: Colors.white,
              fontSize: 14,
              fontWeight: FontWeight.w500),
          ),
        )
      );
    }

    return new Container(
      child: new GridView.count(
          crossAxisCount: 3,
          childAspectRatio: 2.6,
          padding: const EdgeInsets.only(left: 20.0, right: 20.0, top: 30, bottom: 4),
          mainAxisSpacing: 4.0,
          crossAxisSpacing: 4.0,
          children: list
      )
    );
  }

}
