import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'dart:async';

import '../utils/hex_color.dart';
import '../common/order_detail_row_widget.dart';

class TransactionDetailWidget extends StatefulWidget {
  TransactionDetailWidget({Key key}) : super(key: key);

  @override
  _TransactionDetailWidgetState createState() => _TransactionDetailWidgetState();
}

class _TransactionDetailWidgetState extends State<TransactionDetailWidget>
  with TickerProviderStateMixin {

  AnimationController _controller;
  Animation _animation;

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
  void initState() {
    super.initState();

    _controller = AnimationController(vsync: this, duration: Duration(microseconds: 300000));
    _animation = Tween(begin: 1.0, end: 0.0).animate(CurvedAnimation(
      parent: _controller,
      curve: Curves.fastOutSlowIn,
    ))..addStatusListener(handler);
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  void handler(status) {
    if (status == AnimationStatus.completed) {
      
    }
  }

  @override
  Widget build(BuildContext context) {
    print("update TransactionDetailWidget");
    final double height = 431;
    _controller.forward();

    if (_params[0] == "") {
      _getInitDataFromNative();
    }

    var view =  Scaffold(
      appBar: null,
      backgroundColor: HexColor.cardBackgroundColor,
      body: Container(
        margin: const EdgeInsets.all(0.0),
        decoration: new BoxDecoration(
                  color: HexColor.cardBackgroundColor,
                  borderRadius: new BorderRadius.only(
                      topLeft: const Radius.circular(8.0),
                      topRight: const Radius.circular(8.0))),
        child: Column(
          children: <Widget>[
            new Container(
              height: 60,
              // color: HexColor.cardBackgroundColor,
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
            OrderDetailRowWidget(leftText: _params[1], rightText: _params[2], backgroundColor: HexColor.cardBackgroundColor, lineColor: HexColor.cardHighLightColor),
            OrderDetailRowWidget(leftText: _params[3], rightText: _params[4], backgroundColor: HexColor.cardBackgroundColor, lineColor: HexColor.cardHighLightColor),
            OrderDetailRowWidget(leftText: _params[5], rightText: _params[6], backgroundColor: HexColor.cardBackgroundColor, lineColor: HexColor.cardHighLightColor),
            OrderDetailRowWidget(leftText: _params[7], rightText: _params[8], backgroundColor: HexColor.cardBackgroundColor, lineColor: HexColor.cardHighLightColor),
            OrderDetailRowWidget(leftText: _params[9], rightText: _params[10], backgroundColor: HexColor.cardBackgroundColor, lineColor: HexColor.cardHighLightColor),
            OrderDetailRowWidget(leftText: _params[11], rightText: _params[12], backgroundColor: HexColor.cardBackgroundColor, lineColor: HexColor.cardHighLightColor),
            new Container(
              height: 1.0,
              color: HexColor.cardHighLightColor,
            ),
            new Container(
              height: 49,
              color: HexColor.cardBackgroundColor,
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
  
    return AnimatedBuilder(
      animation: _controller,
      builder: (BuildContext context, Widget child) {
        return Scaffold(
          backgroundColor: Colors.transparent,
          body: Transform(
            transform: Matrix4.translationValues(0, _animation.value * height, 0),
            child: view,
          ),
        );
      }
    );
  }
}
