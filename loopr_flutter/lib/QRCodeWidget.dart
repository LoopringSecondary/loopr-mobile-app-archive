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
  static const platform = const MethodChannel('qrcode/value');

  String _qrCodeData = '';
  
  Future<void> _getBatteryLevel() async {
    print("_getBatteryLevel");
    String qrCodeData;
    try {
      final int result = await platform.invokeMethod('getBatteryLevel');
      qrCodeData = '$result';
    } on PlatformException catch (e) {
      print("PlatformException... $e");
      qrCodeData = "";
    }

    setState(() {
      _qrCodeData = qrCodeData;
    });
  }

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    // _getBatteryLevel();
  }

  Future<void> _hello() async {
    print("_hello");
    String qrCodeData;
    try {
      MethodChannel channel = const MethodChannel('upwallet/hello');
      final response = await channel.invokeMethod("loopring", []);
      String body = response;
      qrCodeData = '$body';
    } on PlatformException catch (e) {
      print("PlatformException... $e");
      qrCodeData = "";
    }

    print("looks good");

    setState(() {
      _qrCodeData = qrCodeData;
    });
  }

  @override
  Widget build(BuildContext context) {

    // _getBatteryLevel();
    MethodChannel channel = const MethodChannel('upwallet/hello');
    final response = channel.invokeMethod("loopring", []);
    if (_qrCodeData == "") {
      _hello();
    }
    // This will be printed on the console log.
    print(response);
    // _qrCodeData = response;
    
    print("hello world");
    print(widget.title);
  
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
