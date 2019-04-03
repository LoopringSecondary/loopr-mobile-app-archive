import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';
import 'package:qr_flutter/qr_flutter.dart';
import 'package:flutter/services.dart';
import 'dart:async';

import 'HexColor.dart';

class QRCodeWidget extends StatefulWidget {
  QRCodeWidget({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _QRCodeWidgetState createState() => _QRCodeWidgetState();
}

class _QRCodeWidgetState extends State<QRCodeWidget> {
  String _qrCodeData = '';

  Future<void> _getQRCodeDataFromNative() async {
    String qrCodeData;
    try {
      MethodChannel channel = const MethodChannel('qrCodeDisplay');
      final response = await channel.invokeMethod("qrCodeDisplay.get", []);
      String body = response;
      qrCodeData = '$body';
    } on PlatformException catch (e) {
      print("PlatformException... $e");
      qrCodeData = "";
    }

    setState(() {
      _qrCodeData = qrCodeData;
    });
  }

  Future<void> _copyAddress() async {
    try {
      MethodChannel channel = const MethodChannel('qrCodeDisplay');
      await channel.invokeMethod("qrCodeDisplay.copyAdress", []);
    } on PlatformException catch (e) {
      print("PlatformException... $e");
    }
  }

  Future<void> _saveToAlbum() async {
    try {
      MethodChannel channel = const MethodChannel('qrCodeDisplay');
      await channel.invokeMethod("qrCodeDisplay.saveToAlbum", []);
    } on PlatformException catch (e) {
      print("PlatformException... $e");
    }
  }

  @override
  Widget build(BuildContext context) {
    if (_qrCodeData == "") {
      _getQRCodeDataFromNative();
    }
  
    return Scaffold(
      appBar: null,
      backgroundColor: HexColor("#16162A"),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            new Container(
              padding: EdgeInsets.all(8.0),
              decoration: new BoxDecoration(
                color: HexColor("#21203A"),
              ),
              child: Column(
                children: <Widget>[
                  new QrImage(
                    data: _qrCodeData,
                    size: 200.0,
                    backgroundColor: Colors.white
                  ),
                  CupertinoButton.filled(
                    borderRadius: BorderRadius.circular(10),
                    child: const Text('Copy Address'),
                    onPressed: () {
                      _copyAddress();
                      print("Copy Address onPressed");
                    },
                  ),
                  CupertinoButton(
                    child: const Text('Save to Album'),
                    onPressed: () {
                      _saveToAlbum();
                      print("Save to Album onPressed");
                    },
                  ),
                ],
              ),
            )
          ],
        ),
      ),
    );
  }
}
