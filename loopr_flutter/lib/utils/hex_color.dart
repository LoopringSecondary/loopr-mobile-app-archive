import 'package:flutter/material.dart';

class HexColor extends Color {
  static int _getColorFromHex(String hexColor) {
    hexColor = hexColor.toUpperCase().replaceAll("#", "");
    if (hexColor.length == 6) {
      hexColor = "FF" + hexColor;
    }
    return int.parse(hexColor, radix: 16);
  }

  HexColor(final String hexColor) : super(_getColorFromHex(hexColor));

  static HexColor backgroundColor = HexColor("#16162A");
  static HexColor cardBackgroundColor = HexColor("#21203A");

  static HexColor textColor = HexColor("#cccccc");
  static HexColor textLightColor = HexColor("#666666");

}