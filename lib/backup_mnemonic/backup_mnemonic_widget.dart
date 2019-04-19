import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';
import 'dart:async';

import '../utils/hex_color.dart';

class BackupMnemonicWidget extends StatefulWidget {
  BackupMnemonicWidget({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _BackupMnemonicWidgetState createState() => _BackupMnemonicWidgetState();
}

class _BackupMnemonicWidgetState extends State<BackupMnemonicWidget> {
  List<String> _words = [];
  static const String methodChannel = "backupMnemonic";

  // Receive data from native
  Future<void> _getDataFromNative() async {
    try {
      MethodChannel channel = const MethodChannel(methodChannel);
      final response = await channel.invokeMethod("backupMnemonic.get", []);
      List<String> body = response.cast<String>();
      setState(() {
        _words = body;
      });
    } on Exception catch (e) {
      print("MethodChannel... $e");
    }
  }

  // Send data to native
  Future<void> _copyAddress() async {
    try {
      MethodChannel channel = const MethodChannel(methodChannel);
      await channel.invokeMethod("qrCodeDisplay.copyAdress", []);
    } on Exception catch (e) {
      print("MethodChannel... $e");
    }
  }

  Future<void> _saveToAlbum() async {
    try {
      MethodChannel channel = const MethodChannel(methodChannel);
      await channel.invokeMethod("qrCodeDisplay.saveToAlbum", []);
    } on Exception catch (e) {
      print("MethodChannel... $e");
    }
  }

  @override
  Widget build(BuildContext context) {
    if (_words.length == 0) {
      _getDataFromNative();
    }
  
    return Scaffold(
      appBar: null,
      backgroundColor: HexColor("#16162A"),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            new Container(
              height: 50.0,
              width: 180.0,
            ),
            SizedBox(
              width: 180.0, // specific value
              child: 
                CupertinoButton(
                  color: HexColor("#FDAE25"),
                  padding: EdgeInsets.only(left: 2, right: 2),
                  borderRadius: BorderRadius.circular(22),
                  child: const Text(
                    'Copy Address',
                    style: TextStyle(color: Colors.white, fontSize: 16),
                  ),
                  onPressed: () {
                    // _copyAddress();
                    print("Copy Address onPressed");
                  },
                ),
            ),
            SizedBox(
              width: 180.0, // specific value
              child: 
                CupertinoButton(
                  child: const Text(
                    'Save to Album',
                    style: TextStyle(color: Colors.white, fontSize: 16),
                  ),
                  onPressed: () {
                    // _saveToAlbum();
                    print("Save to Album onPressed");
                  },
                ),
            )
          ],
        ),
      ),
    );
  }
}
