import 'package:flutter/material.dart';
import './backup_mnemonic_widget.dart';


class BackupMnemonicGridViewWidget extends StatefulWidget {
  BackupMnemonicGridViewWidget({Key key}) : super(key: key);

  // final List<String> words;

  @override
  _BackupMnemonicGridViewWidgetState createState() => _BackupMnemonicGridViewWidgetState();
}

class _BackupMnemonicGridViewWidgetState extends State<BackupMnemonicGridViewWidget> {
  static const String methodChannel = "backupMnemonicGrid";

  @override
  void initState() {
    super.initState();
  }



  @override
  Widget build(BuildContext context) {
    final _words = InheritedStateContainer.of(context).words;
    print("render in BackupMnemonicGridViewWidget ...  $_words");


    List<Widget> list = [];
    for(var i = 0; i < _words.length; i++) {
      String word = _words[i];
      int num = i+1;
      list.add(
        new Container(
          padding: EdgeInsets.only(left: 4.0, right: 0.0, top: 0, bottom: 0),
          child: new Text(
            "$num. $word",
            style: TextStyle(
              color: Colors.white,
              fontSize: 14,
              fontWeight: FontWeight.w500),
          ),
        )
      );
    }

    return new Container(
      child: new GridView.count(
          crossAxisCount: 3,
          childAspectRatio: 2.6,
          padding: const EdgeInsets.only(left: 20.0, right: 20.0, top: 30, bottom: 4),
          mainAxisSpacing: 4.0,
          crossAxisSpacing: 4.0,
          children: list
      )
    );
  }

}
