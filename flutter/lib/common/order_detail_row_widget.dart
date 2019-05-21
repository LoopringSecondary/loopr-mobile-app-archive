import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';

import '../utils/hex_color.dart';

class OrderDetailRowWidget extends StatefulWidget {
  OrderDetailRowWidget({
    Key key, this.leftText, this.rightText,
    this.backgroundColor, this.lineColor}) : super(key: key);

  final String leftText;
  final String rightText;
  final Color backgroundColor;
  final Color lineColor;

  @override
  _OrderDetailRowWidgetState createState() => _OrderDetailRowWidgetState();
}

class _OrderDetailRowWidgetState extends State<OrderDetailRowWidget> {

  @override
  Widget build(BuildContext context) {  
    return Container(
      margin: const EdgeInsets.all(0.0),
      color: this.widget.backgroundColor,
      child: Column(
        children: <Widget>[
          new Container(
            height: 1.0,
            color: this.widget.lineColor,
          ),
          new Container(
            margin: const EdgeInsets.only(left: 24, right: 24),
            height: 49,
            color: this.widget.backgroundColor,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: <Widget>[
                new Text(
                  this.widget.leftText,
                  style: TextStyle(color: HexColor.textLightColor, fontSize: 14),
                ),
                new Text(
                  this.widget.rightText,
                  style: TextStyle(color: HexColor.textColor, fontSize: 14),
                )
              ],
            ),
          )
        ],
      ),
    );
  }
}
