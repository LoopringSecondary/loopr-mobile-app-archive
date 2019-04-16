import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';
import 'dart:async';

import '../utils/hex_color.dart';

class OrderDetailWidget extends StatefulWidget {
  OrderDetailWidget({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _OrderDetailWidgetState createState() => _OrderDetailWidgetState();
}

class _OrderDetailWidgetState extends State<OrderDetailWidget> {
  String _qrCodeData = '';

  // This value must be equal to the value in iOS and Android
  static const String methodChannel = "orderDetail";

  // Receive data from native
  Future<void> _getInitDataFromNative() async {
    String qrCodeData;
    try {
      MethodChannel channel = const MethodChannel(methodChannel);
      final response = await channel.invokeMethod("orderDetail.get", []);
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

  @override
  Widget build(BuildContext context) {
    print("update OrderDetailWidget");

    if (_qrCodeData == "") {
      _getInitDataFromNative();
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
          ],
        ),
      ),
    );
  }
}
