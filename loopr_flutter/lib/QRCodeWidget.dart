import 'package:flutter/material.dart';
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
      final response = await channel.invokeMethod("loopring", []);
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
              color: Colors.white,
              child: Column(
                children: <Widget>[
                  new QrImage(
                    data: _qrCodeData,
                    size: 200.0,
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
