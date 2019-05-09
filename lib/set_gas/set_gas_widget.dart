import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'dart:async';

import '../utils/hex_color.dart';

class SetGasWidget extends StatefulWidget {
  SetGasWidget({Key key}) : super(key: key);

  @override
  _SetGasWidgetState createState() => _SetGasWidgetState();
}

class _SetGasWidgetState extends State<SetGasWidget> with TickerProviderStateMixin {

  AnimationController _controller;
  Animation _animation;

  double _sliderValue = 1;
  List<String> _params = ["", "", "", "", "", "", "", "", "", "", "", "", "", "",];

  // This value must be equal to the value in iOS and Android
  static const String methodChannel = "setGas";

  // Receive data from native
  Future<void> _getInitDataFromNative() async {
    try {
      MethodChannel channel = const MethodChannel(methodChannel);
      final response = await channel.invokeMethod("setGas.get", []);
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
    print("update SetGasWidget");
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
            new Container(
              height: 1.0,
              color: HexColor.cardHighLightColor,
            ),
            new Container(
              width: double.infinity,
              color: Colors.red,
              child: Column(
                mainAxisSize: MainAxisSize.max,
                children: <Widget>[
                  new CupertinoSlider(
                    value: _sliderValue,
                    onChanged: (double value) {
                      print("slider value: $value");
                      setState(() {
                        _sliderValue = value;
                      });
                    },
                    min: 1,
                    max: 10,
                    divisions: 10,
                    activeColor: HexColor.theme,
                  )
                ],
              ),
            ) 
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
