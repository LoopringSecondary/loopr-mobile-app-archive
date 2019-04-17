import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';
import 'dart:async';

import '../utils/hex_color.dart';
import 'order_detail_row_widget.dart';

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
      backgroundColor: HexColor.backgroundColor,
      body: Container(
        margin: const EdgeInsets.all(0.0),
        color: HexColor.backgroundColor,
        child: Column(
          children: <Widget>[
            OrderDetailRowWidget(leftText: "Status", rightText: "Completed"),
            OrderDetailRowWidget(leftText: "Price", rightText: "0.05 LRC/WETH"),
            OrderDetailRowWidget(leftText: "Trading Fee", rightText: "1.100 LRC"),
            OrderDetailRowWidget(leftText: "Filled", rightText: "330.0%"),
            OrderDetailRowWidget(leftText: "TxHash", rightText: "0xb38c...3963d"),
            OrderDetailRowWidget(leftText: "Time to Live", rightText: "02-25 00:04 ~ 02-25 01:04"),
            new Container(
              height: 1.0,
              color: HexColor.cardBackgroundColor,
            )
          ],
        ),
      ),
    );
  }
}
