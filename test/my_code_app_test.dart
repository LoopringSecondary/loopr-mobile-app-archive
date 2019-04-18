import 'package:flutter_test/flutter_test.dart';

import 'package:loopr_flutter/qr_code/qr_code_app.dart';

void main() {
  testWidgets('Counter increments smoke test', (WidgetTester tester) async {
    // Build our app and trigger a frame.
    await tester.pumpWidget(QRCodeApp());
  });
}
