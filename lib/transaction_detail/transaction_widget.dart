import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';
import 'dart:async';

import '../utils/hex_color.dart';
import '../order_detail/order_detail_row_widget.dart';

class TransactionDetailWidget extends StatefulWidget {
  TransactionDetailWidget({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _TransactionDetailWidgetState createState() => _TransactionDetailWidgetState();
}

class _TransactionDetailWidgetState extends State<TransactionDetailWidget> {

  List<String> _params = [];

  // This value must be equal to the value in iOS and Android
  static const String methodChannel = "transactionDetail";

  // Receive data from native
  Future<void> _getInitDataFromNative() async {
    try {
      MethodChannel channel = const MethodChannel(methodChannel);
      final response = await channel.invokeMethod("transactionDetail.get", []);
      List<String> body = response.cast<String>();
      print(body);
      setState(() {
        _params = body;
      });
    } on Exception catch (e) {
      print("MethodChannel... $e");
    }
  }

  @override
  Widget build(BuildContext context) {
    print("update TransactionDetailWidget");

    if (_params.length == 0) {
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
            OrderDetailRowWidget(leftText: _params[0], rightText: _params[1]),
            OrderDetailRowWidget(leftText: _params[2], rightText: _params[3]),
            OrderDetailRowWidget(leftText: _params[4], rightText: _params[5]),
            OrderDetailRowWidget(leftText: _params[6], rightText: _params[7]),
            OrderDetailRowWidget(leftText: _params[8], rightText: _params[9]),
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
