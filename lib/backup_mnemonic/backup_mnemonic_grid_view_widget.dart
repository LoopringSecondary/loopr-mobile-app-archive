import 'package:flutter/material.dart';

class BackupMnemonicGridViewWidget extends StatefulWidget {
  BackupMnemonicGridViewWidget({Key key, this.words}) : super(key: key);

  final List<String> words;

  @override
  _BackupMnemonicGridViewWidgetState createState() => _BackupMnemonicGridViewWidgetState();
}

class _BackupMnemonicGridViewWidgetState extends State<BackupMnemonicGridViewWidget> {

  @override
  Widget build(BuildContext context) {

    List<Widget> list = [];
    for(var i = 0; i < this.widget.words.length; i++) {
      String word = this.widget.words[i];
      int num = i+1;
      list.add(
        new Container(
          padding: EdgeInsets.only(left: 4.0, right: 0.0, top: 8, bottom: 0),
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
          padding: const EdgeInsets.only(left: 4.0, right: 4.0, top: 10, bottom: 4),
          mainAxisSpacing: 4.0,
          crossAxisSpacing: 4.0,
          children: list
      )
    );
  }

}
