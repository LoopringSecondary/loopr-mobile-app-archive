import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'dart:async';

import '../utils/hex_color.dart';
import '../common/upwallet_slider.dart';

class SetGasWidget extends StatefulWidget {
  SetGasWidget({Key key}) : super(key: key);

  @override
  _SetGasWidgetState createState() => _SetGasWidgetState();
}

class _SetGasWidgetState extends State<SetGasWidget> with TickerProviderStateMixin {

  AnimationController _controller;
  Animation _animation;

  double _sliderValue = 2;

  double _gasPriceInGwei = 1;
  double _maxGasValue = 10;
  double _ethPrice = 0;
  double _gasLimit = 0;

  List<String> _params = ["", "", "", "", "", "", "", "", ""];

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
        _gasPriceInGwei = double.parse(body[0]);
        _maxGasValue = double.parse(body[1]);
        _ethPrice = double.parse(body[2]);
        _gasLimit = double.parse(body[3]);
      });
    } on Exception catch (e) {
      print("MethodChannel... $e");
    }
  }

  Future<void> _updateSliderValue() async {
    try {
      MethodChannel channel = const MethodChannel(methodChannel);
      final response = await channel.invokeMethod("setGas.update", [_sliderValue]);
      print(response);
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

  void _pressedRecommendPrice() {

  }

  @override
  Widget build(BuildContext context) {
    print("update SetGasWidget");
    final double height = 431;
    _controller.forward();

    if (_params[0] == "") {
      _getInitDataFromNative();
    }
    print("rendering.... $_gasPriceInGwei");
    double amountInEther = _gasPriceInGwei / 1000000000;
    double totalGasInEther = amountInEther * _gasLimit;
    print("_gasLimit.... $_gasLimit");

    // TODO: add currency method
    double transactionFeeInFiat = totalGasInEther * _ethPrice;
    String gasValueLabelText = "$totalGasInEther ETH";
    String gasTip = _params[6];
    String gasTipLabelText = "$gasTip ($_gasPriceInGwei Gwei)";

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
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: <Widget>[
                  new Text(
                    _params[4],
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
              height: 22.0,
              width: 180.0,
            ),
            new Container(
              padding: new EdgeInsets.only(left: 15, right: 15),
              child: Column(
                children: <Widget>[
                  new Container(
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: <Widget>[
                        new Text(
                          gasValueLabelText,
                          style: TextStyle(
                            color: HexColor.textColor,
                            fontSize: 14,
                            fontWeight: FontWeight.w500
                          ),
                        ),
                        SizedBox(
                          height: 30,
                          child: 
                            CupertinoButton(
                              padding: EdgeInsets.all(0),
                              child: Text(
                                _params[5],
                                style: TextStyle(
                                  color: Colors.white,
                                  fontSize: 14,
                                  fontWeight: FontWeight.w500
                                ),
                              ),
                              onPressed: () {
                                _pressedRecommendPrice();
                              },
                            ),
                        ),
                      ],
                    ),
                  ),
                  new Container(
                    height: 0.0,
                    width: 180.0,
                  ),
                  new Container(
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: <Widget>[
                        new Text(
                          gasTipLabelText,
                          style: TextStyle(
                            color: HexColor.textLightColor,
                            fontSize: 12,
                            fontWeight: FontWeight.w500,
                          ),
                        ),
                      ],
                    ),
                  )
                ],
              ),
            ),
            new Container(
              height: 24.0,
              width: 180.0,
            ),
            new Container(
              width: double.infinity,
              padding: new EdgeInsets.only(left: 15, right: 15),
              child: Column(
                mainAxisSize: MainAxisSize.max,
                children: <Widget>[
                  new UpwalletSlider(
                    value: _sliderValue,
                    onChanged: (double value) {
                      print("slider value: $value");
                      double gasPriceInGwei = value.round().toDouble();
                      setState(() {
                        _sliderValue = value;
                        _gasPriceInGwei = gasPriceInGwei;
                      });
                    },
                    onChangeEnd: (double value) {
                      _updateSliderValue();
                    },
                    min: 1,
                    max: _maxGasValue,
                    activeColor: HexColor.theme,
                    trackColor: HexColor.cardHighLightColor,
                  )
                ],
              ),
            ),
            new Container(
              height: 10.0,
              width: 180.0,
            ),
            new Container(
              padding: new EdgeInsets.only(left: 15, right: 15),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: <Widget>[
                  new Text(
                    _params[7],
                    style: TextStyle(
                      color: HexColor.textLightColor,
                      fontSize: 12,
                      fontWeight: FontWeight.w500
                    ),
                  ),
                  new Text(
                    _params[8],
                    style: TextStyle(
                      color: HexColor.textLightColor,
                      fontSize: 12,
                      fontWeight: FontWeight.w500
                    ),
                  ),
                ],
              ),
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
