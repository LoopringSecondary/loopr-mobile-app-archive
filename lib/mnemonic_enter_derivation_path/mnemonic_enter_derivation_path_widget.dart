import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter/services.dart';
import 'dart:async';

import '../utils/hex_color.dart';

class MnemonicEnterDerivationPathWidget extends StatefulWidget {
  MnemonicEnterDerivationPathWidget({Key key}) : super(key: key);

  @override
  _MnemonicEnterDerivationPathWidgetState createState() => _MnemonicEnterDerivationPathWidgetState();
}

class _MnemonicEnterDerivationPathWidgetState extends State<MnemonicEnterDerivationPathWidget>
  with TickerProviderStateMixin {

  AnimationController _controller;
  Animation _animation;

  List<String> _params = ["", "", "", "", "", "", "", "", "", "", "", "", "", "",];

  // This value must be equal to the value in iOS and Android
  static const String methodChannel = "mnemonicEnterDerivationPath";

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

  Future<void> _updateDerivationPath(String name, String derivationPath) async {
    try {
      MethodChannel channel = const MethodChannel(methodChannel);
      final response = await channel.invokeMethod("mnemonicEnterDerivationPath.update", [name, derivationPath]);
      print(response);      
    } on Exception catch (e) {
      print("MethodChannel... $e");
    }
  }

  void onTapped(String name, String derivationPath) {
    print(name);
    _updateDerivationPath(name, derivationPath);
  }

  @override
  Widget build(BuildContext context) {
    print("update MnemonicEnterDerivationPathWidget");
    final double height = 431;
    _controller.forward();

    final List<String> names = <String>['Loopring Wallet', 'imToken'];
    final List<String> derivationPaths = <String>["m/44'/60'/0'/0", "m/44'/60'/0'/0"];

    var listView = ListView.separated(
      padding: const EdgeInsets.all(8.0),
      itemCount: names.length,
      itemBuilder: (BuildContext context, int index) {
        var name = names[index];
        var derivationPath = derivationPaths[index];
        return ListTile(
          title: Text(
            name,
            style: TextStyle(
              color: HexColor.textColor,
              fontSize: 16,
              fontWeight: FontWeight.w500
            ),
          ),
          subtitle: Text(
            derivationPath,
            style: TextStyle(
              color: HexColor.textLightColor,
              fontSize: 14,
              fontWeight: FontWeight.w400
            ),
          ),
          onTap: () => onTapped(name, derivationPath),
        );
      },
      separatorBuilder: (BuildContext context, int index) => const Divider(),
    );
  
    return AnimatedBuilder(
      animation: _controller,
      builder: (BuildContext context, Widget child) {
        return Scaffold(
          backgroundColor: Colors.transparent,
          body: Transform(
            transform: Matrix4.translationValues(0, _animation.value * height, 0),
            child: listView,
          ),
        );
      }
    );
  }
}
