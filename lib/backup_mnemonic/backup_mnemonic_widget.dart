import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';
import 'dart:async';

import './backup_mnemonic_grid_view_widget.dart';

import '../utils/hex_color.dart';

class BackupMnemonicWidget extends StatefulWidget {
  BackupMnemonicWidget({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _BackupMnemonicWidgetState createState() => _BackupMnemonicWidgetState();
}

class _BackupMnemonicWidgetState extends State<BackupMnemonicWidget> {
  List<String> _words = [];
  static const String methodChannel = "backupMnemonic";

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

  @override
  Widget build(BuildContext context) {
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
              child: new BackupMnemonicGridViewWidget(words: _words),
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
                            // _copyAddress();
                            print("Copy Address onPressed");
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
                            // _saveToAlbum();
                            print("Save to Album onPressed");
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
