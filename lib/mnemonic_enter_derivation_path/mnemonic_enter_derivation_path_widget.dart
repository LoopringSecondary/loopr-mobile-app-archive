import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter/services.dart';
import 'dart:async';

import 'wallet_type.dart';
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

  final List<WalletType> walletTypes = [
    WalletType("Loopring Wallet", "m/44'/60'/0'/0"),
    WalletType("imToken", "m/44'/60'/0'/0"),
    WalletType("MetaMask", "m/44'/60'/0'/0"),
    WalletType("TREZOR (ETH)", "m/44'/60'/0'/0"),
    WalletType("TREZOR (ETC)", "m/44'/61'/0'/0"),
    WalletType("Ledger (ETH)", "m/44'/60'/0'"),
    WalletType("Ledger (ETC)", "m/44'/60'/160720'/0'"),
    WalletType("SingularDTV", "m/0'/0'/0'"),
    WalletType("Network: Testnets", "m/44'/1'/0'/0"),
    WalletType("Network: Expanse", "m/44'/40'/0'/0"),
    WalletType("Network: Ubiq", "m/44'/108'/0'/0"),
    WalletType("Network: Ellaism", "m/44'/163'/0'/0")
  ];

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

    var listView = ListView.separated(
      padding: const EdgeInsets.all(8.0),
      itemCount: walletTypes.length,
      itemBuilder: (BuildContext context, int index) {
        var walletType = walletTypes[index];
        return ListTile(
          title: Text(
            walletType.name,
            style: TextStyle(
              color: HexColor.textColor,
              fontSize: 16,
              fontWeight: FontWeight.w500
            ),
          ),
          subtitle: Text(
            walletType.derivationPath,
            style: TextStyle(
              color: HexColor.textLightColor,
              fontSize: 14,
              fontWeight: FontWeight.w400
            ),
          ),
          onTap: () => onTapped(walletType.name, walletType.derivationPath),
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
