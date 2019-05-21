import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';
import 'package:qr_flutter/qr_flutter.dart';
import 'package:flutter/services.dart';
import 'dart:async';

import '../utils/hex_color.dart';

class SendListChooseWidget extends StatefulWidget {
  SendListChooseWidget({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _SendListChooseWidgetState createState() => _SendListChooseWidgetState();
}

class _SendListChooseWidgetState extends State<SendListChooseWidget> {
  String _qrCodeData = '';
  static const String methodChannel = "qrCodeDisplay";

  // Receive data from native
  Future<void> _getQRCodeDataFromNative() async {
    String qrCodeData;
    try {
      MethodChannel channel = const MethodChannel(methodChannel);
      final response = await channel.invokeMethod("qrCodeDisplay.get", []);
      String body = response;
      qrCodeData = '$body';
    } on Exception catch (e) {
      print("MethodChannel... $e");
      qrCodeData = "";
    }

    setState(() {
      _qrCodeData = qrCodeData;
    });
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
              width: 270,
              padding: EdgeInsets.all(10.0),
              decoration: new BoxDecoration(
                color: HexColor("#21203A"),
                borderRadius: BorderRadius.circular(8),
              ),
              child: Column(
                children: <Widget>[
                  new Container(
                    height: 30.0,
                    width: 180.0,
                  ),
                  new QrImage(
                    data: _qrCodeData,
                    size: 180.0,
                    backgroundColor: Colors.white
                  ),
                  new Container(
                    height: 10.0,
                    width: 180.0,
                  ),
                  SizedBox(
                    height: 40.0,
                    width: 180.0,
                    child: 
                      Text(
                        _qrCodeData,
                        style: TextStyle(color: Colors.white),
                      ),
                  ),
                  new Container(
                    height: 20.0,
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
                          _copyAddress();
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
                          _saveToAlbum();
                          print("Save to Album onPressed");
                        },
                      ),
                  )
                ],
              ),
            ),
            new Container(
              height: 50.0,
              width: 180.0,
            ),
          ],
        ),
      ),
    );
  }
}
