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
  String _status = '';
  String _price = '';
  String _tradingFee = '';
  String _filled = '';
  String _txHash = '';
  String _timeToLive = '';

  // This value must be equal to the value in iOS and Android
  static const String methodChannel = "orderDetail";

  // Receive data from native
  Future<void> _getInitDataFromNative() async {
    try {
      MethodChannel channel = const MethodChannel(methodChannel);
      final response = await channel.invokeMethod("orderDetail.get", []);
      List<String> body = response.cast<String>();
      setState(() {
        _status = body[0];
        _price = body[1];
        _tradingFee = body[2];
        _filled = body[3];
        _txHash = body[4];
        _timeToLive = body[5];
      });
    } on Exception catch (e) {
      print("MethodChannel... $e");
    }
  }

  @override
  Widget build(BuildContext context) {
    print("update OrderDetailWidget");

    if (_status == "") {
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
            OrderDetailRowWidget(leftText: "Status", rightText: _status),
            OrderDetailRowWidget(leftText: "Price", rightText: _price),
            OrderDetailRowWidget(leftText: "Trading Fee", rightText: _tradingFee),
            OrderDetailRowWidget(leftText: "Filled", rightText: _filled),
            OrderDetailRowWidget(leftText: "TxHash", rightText: _txHash),
            OrderDetailRowWidget(leftText: "Time to Live", rightText: _timeToLive),
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
