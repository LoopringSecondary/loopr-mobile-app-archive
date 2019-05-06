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
  static HexColor cardHighLightColor = HexColor("#2B2C47");

  // The leading two digits are used for opaque. It's different from iOS
  static Color textColor = Color(0xCCFFFFFF);
  static Color textLightColor = Color(0x66FFFFFF);
  static Color theme = HexColor("#FDAE25");
}