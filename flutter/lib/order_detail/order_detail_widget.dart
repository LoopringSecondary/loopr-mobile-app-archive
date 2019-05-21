import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';
import 'dart:async';

import '../utils/hex_color.dart';
import '../common/order_detail_row_widget.dart';

class OrderDetailWidget extends StatefulWidget {
  OrderDetailWidget({Key key}) : super(key: key);

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

    String txHash = "";
    if (_status == "") {
      _getInitDataFromNative();
    } else {
      txHash = _txHash.substring(0, 13) + '...' + _txHash.substring(_txHash.length-1-13, _txHash.length-1);
    }

    return Scaffold(
      appBar: null,
      backgroundColor: HexColor.backgroundColor,
      body: Container(
        margin: const EdgeInsets.all(0.0),
        color: HexColor.backgroundColor,
        child: Column(
          children: <Widget>[
            OrderDetailRowWidget(leftText: "Status", rightText: _status, backgroundColor: HexColor.backgroundColor, lineColor: HexColor.cardBackgroundColor),
            OrderDetailRowWidget(leftText: "Price", rightText: _price, backgroundColor: HexColor.backgroundColor, lineColor: HexColor.cardBackgroundColor),
            OrderDetailRowWidget(leftText: "Trading Fee", rightText: _tradingFee, backgroundColor: HexColor.backgroundColor, lineColor: HexColor.cardBackgroundColor),
            OrderDetailRowWidget(leftText: "Filled", rightText: _filled, backgroundColor: HexColor.backgroundColor, lineColor: HexColor.cardBackgroundColor),
            OrderDetailRowWidget(leftText: "TxHash", rightText: txHash, backgroundColor: HexColor.backgroundColor, lineColor: HexColor.cardBackgroundColor),
            OrderDetailRowWidget(leftText: "Time to Live", rightText: _timeToLive, backgroundColor: HexColor.backgroundColor, lineColor: HexColor.cardBackgroundColor),
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
