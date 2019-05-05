import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';
import 'dart:async';

import '../utils/hex_color.dart';
import '../order_detail/order_detail_row_widget.dart';

class TransactionDetailWidget extends StatefulWidget {
  TransactionDetailWidget({Key key}) : super(key: key);

  @override
  _TransactionDetailWidgetState createState() => _TransactionDetailWidgetState();
}

class _TransactionDetailWidgetState extends State<TransactionDetailWidget> {

  List<String> _params = ["", "", "", "", "", "", "", "", "", "", "", "", "", "",];

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

    if (_params[0] == "") {
      _getInitDataFromNative();
    }
  
    return Scaffold(
      appBar: null,
      backgroundColor: HexColor.backgroundColor,
      body: Container(
        margin: const EdgeInsets.all(0.0),
        decoration: new BoxDecoration(
                  color: HexColor.backgroundColor,
                  borderRadius: new BorderRadius.only(
                      topLeft: const Radius.circular(8.0),
                      topRight: const Radius.circular(8.0))),
        child: Column(
          children: <Widget>[
            new Container(
              height: 60,
              // color: HexColor.backgroundColor,
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: <Widget>[
                  new Text(
                    _params[0],
                    style: TextStyle(
                      color: HexColor.textColor,
                      fontSize: 16,
                      fontWeight: FontWeight.w500
                    ),
                  ),
                ],
              )
            ),
            OrderDetailRowWidget(leftText: _params[1], rightText: _params[2]),
            OrderDetailRowWidget(leftText: _params[3], rightText: _params[4]),
            OrderDetailRowWidget(leftText: _params[5], rightText: _params[6]),
            OrderDetailRowWidget(leftText: _params[7], rightText: _params[8]),
            OrderDetailRowWidget(leftText: _params[9], rightText: _params[10]),
            OrderDetailRowWidget(leftText: _params[11], rightText: _params[12]),
            new Container(
              height: 1.0,
              color: HexColor.cardBackgroundColor,
            ),
            new Container(
              height: 49,
              color: HexColor.backgroundColor,
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: <Widget>[
                  new Text(
                    _params[13],
                    style: TextStyle(
                      color: HexColor.textColor,
                      fontSize: 14,
                      fontWeight: FontWeight.w500
                    ),
                  ),
                ],
              )
            ),
            new Container(
              height: 1.0,
              color: HexColor.cardBackgroundColor,
            ),
          ],
        ),
      ),
    );
  }
}
