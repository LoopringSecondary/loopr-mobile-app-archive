import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';
import 'dart:async';

import './backup_mnemonic_grid_view_widget.dart';

import '../utils/hex_color.dart';

// This is likely all your InheritedWidget will ever need.
class InheritedStateContainer extends InheritedWidget {
  // The data is whatever this widget is passing down.
  final List<String> words;

  // InheritedWidgets are always just wrappers.
  // So there has to be a child, 
  // Although Flutter just knows to build the Widget thats passed to it
  // So you don't have have a build method or anything.
  InheritedStateContainer({
    Key key,
    this.words,
    @required Widget child,
  }) : super(key: key, child: child);
  
  // This is a better way to do this, which you'll see later.
  // But basically, Flutter automatically calls this method when any data
  // in this widget is changed. 
  // You can use this method to make sure that flutter actually should
  // repaint the tree, or do nothing.
  // It helps with performance.
  @override
  bool updateShouldNotify(InheritedStateContainer old) {
    print("updateShouldNotify");
    return true;
  }

  static InheritedStateContainer of(BuildContext context) =>
      context.inheritFromWidgetOfExactType(InheritedStateContainer);

}

class BackupMnemonicWidget extends StatefulWidget {
  BackupMnemonicWidget({Key key, this.title}) : super(key: key);

  final String title;

/*
  // This creates a method on the AppState that's just like 'of'
  // On MediaQueries, Theme, etc
  // This is the secret to accessing your AppState all over your app
  static _BackupMnemonicWidgetState of(BuildContext context) {
    return (context.inheritFromWidgetOfExactType(_InheritedStateContainer)
            as _InheritedStateContainer)
        .data;
  }
*/

  @override
  _BackupMnemonicWidgetState createState() => _BackupMnemonicWidgetState();
}

class _BackupMnemonicWidgetState extends State<BackupMnemonicWidget> {
  List<String> _words = [];
  static const String methodChannel = "backupMnemonic";
  MethodChannel platform = const MethodChannel('backupMnemonic');

  // Receive data from native
  Future<void> _getDataFromNative() async {
    try {
      MethodChannel channel = const MethodChannel(methodChannel);
      final response = await channel.invokeMethod("backupMnemonic.get", []);
      List<String> body = response.cast<String>();
      setState(() {
        _words = body;
      });
    } on Exception catch (e) {
      print("MethodChannel... $e");
    }
  }

  Future<void> methodCallHandler(MethodCall methodCall) async {
    switch (methodCall.method) {
      case 'backupMnemonic.update':
        print("received");
        print(methodCall.arguments);
        InheritedStateContainer(words: methodCall.arguments, child: new BackupMnemonicGridViewWidget());
        setState(() {
          _words = methodCall.arguments;
        });
        break;
      default:
        print("other methods");
    }
  }

  // Send data to native
  Future<void> _pressedVerifyButton() async {
    try {
      MethodChannel channel = const MethodChannel(methodChannel);
      await channel.invokeMethod("backupMnemonic.verify", []);
    } on Exception catch (e) {
      print("MethodChannel... $e");
    }
  }

  Future<void> _pressedSkipButton() async {
    try {
      MethodChannel channel = const MethodChannel(methodChannel);
      await channel.invokeMethod("backupMnemonic.skip", []);
    } on Exception catch (e) {
      print("MethodChannel... $e");
    }
  }

  @override
  Widget build(BuildContext context) {
    print("render in backup_mnemonic_widget ...  $_words");

    platform.setMethodCallHandler(methodCallHandler);

    if (_words.length == 0) {
      _getDataFromNative();
    }
  
    return Scaffold(
      appBar: null,
      backgroundColor: HexColor("#16162A"),
      body: Center(
        child: Column(
          mainAxisSize: MainAxisSize.max,
          // mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            new Container(
              height: 10.0,
              width: 180.0,
            ),
            new Container(
              constraints: new BoxConstraints.expand(
                height: 200
              ),
              padding: new EdgeInsets.only(left: 18, right: 18),
              decoration: new BoxDecoration(
                image: new DecorationImage(
                  image: new AssetImage('assets/mnemonic-1@3x.png')
                )
              ),
              child: Column(
                children: <Widget>[
                  new Container(
                    // TODO: need to set line height.
                    padding: EdgeInsets.only(left: 20.0, right: 20.0, top: 30),
                      child: new Text(
                      "Please don't show up in public places (prevent cameras from taking photos) or take a screen shot(your operating system may back up images to cloud storage). These operations can bring you huge and irreversible security risks.",
                      style: TextStyle(
                        color: Colors.white,
                        fontSize: 15,
                        fontStyle: FontStyle.italic),
                    ),
                  )
                ],
              )
            ),
            new Container(
              constraints: new BoxConstraints.expand(
                height: 200
              ),
              padding: new EdgeInsets.only(left: 20, right: 20),
              decoration: new BoxDecoration(
                image: new DecorationImage(
                  image: new AssetImage('assets/mnemonic-2@3x.png')
                )
              ),
              child: InheritedStateContainer(
                        child: new BackupMnemonicGridViewWidget(),
                        words: _words,
                      ),
            ),
            new Expanded(
              child: new Align(
                alignment: Alignment.bottomCenter,
                child: Row(
                  mainAxisSize: MainAxisSize.max,
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children: <Widget>[
                    new Container(
                      height: 10.0,
                      width: 20.0,
                    ),
                    Expanded(child: SizedBox(
                      height: 45,
                      child: 
                        CupertinoButton(
                          color: HexColor.theme,
                          padding: EdgeInsets.only(left: 2, right: 2),
                          borderRadius: BorderRadius.circular(22),
                          child: const Text(
                            'Verify',
                            style: TextStyle(color: Colors.white, fontSize: 16),
                          ),
                          onPressed: () {
                            _pressedVerifyButton();
                            print("_pressedVerifyButton onPressed");
                          },
                        ),
                    )),
                    new Container(
                      height: 10.0,
                      width: 15.0,
                    ),
                    Expanded(child: SizedBox(
                      height: 45,
                      child:
                        CupertinoButton(
                          color: HexColor("#2B2C47"),
                          padding: EdgeInsets.only(left: 2, right: 2),
                          borderRadius: BorderRadius.circular(22),
                          child: const Text(
                            'Skip',
                            style: TextStyle(color: Colors.white, fontSize: 16),
                          ),
                          onPressed: () {
                            _pressedSkipButton();
                            print("_pressedSkipButton onPressed");
                          },
                        ),
                    )),
                    new Container(
                      height: 10.0,
                      width: 20.0,
                    ),
                  ]
                )
              ),
            ),
            new Container(
              height: 30.0,
              width: 180.0,
            ),
          ],
        ),
      ),
    );
  }
}
