import 'package:flutter/material.dart';
import 'airdrop/airdrop_app.dart';
import 'backup_mnemonic/backup_mnemonic_app.dart';
import 'order_detail/order_detail_app.dart';
import 'qr_code/qr_code_app.dart';
import 'send_list_choose/send_list_choose_app.dart';
import 'set_gas/set_gas_app.dart';
import 'transaction_detail/transaction_detail_app.dart';

// Android
// Refer https://github.com/flutter/flutter/issues/10813
void main() => runApp(
  new MaterialApp(
    debugShowCheckedModeBanner: false,
    routes: <String, WidgetBuilder> {
      'airdrop': (BuildContext context) => AirdropApp(),
      'backupMnemonic': (BuildContext context) => BackupMnemonicApp(),
      'orderDetail': (BuildContext context) => OrderDetailApp(),
      'qrCode': (BuildContext context) => QRCodeApp(),
      'sendListChoose': (BuildContext context) => SendListChooseApp(),
      'setGas': (BuildContext context) => SetGasApp(),
      'transactionDetail': (BuildContext context) => TransactionDetailApp(),
      'default': (BuildContext context) => QRCodeApp(),
    },
  )
);

// routes shouldn't start with /
// Otherwise it will return a warning
/*
I/flutter: ══╡ EXCEPTION CAUGHT BY FLUTTER FRAMEWORK ╞═════════════════════════════════════════════════════════
I/flutter: The following message was thrown:
    Could not navigate to initial route.
    The requested route name was: "/qrCode"
    The following routes were therefore attempted:
     * /
     * /qrCode
    This resulted in the following objects:
     * MaterialPageRoute<dynamic>(RouteSettings("/", null), animation: null)
     * null
    One or more of those objects was null, and therefore the initial route specified will be ignored and
    "/" will be used instead.
I/flutter: ════════════════════════════════════════════════════════════════════════════════════════════════════
I/flutter: hello world
    Flutter Demo Home Page
I/flutter: Another exception was thrown: Could not navigate to initial route.

*/

// iOS
// https://github.com/flutter/flutter/issues/22356
@pragma('vm:entry-point')
void airdrop() => runApp(AirdropApp());

@pragma('vm:entry-point')
void backupMnemonic() => runApp(BackupMnemonicApp());

@pragma('vm:entry-point')
void orderDetail() => runApp(OrderDetailApp());

@pragma('vm:entry-point')
void qrCode() => runApp(QRCodeApp());

@pragma('vm:entry-point')
void sendListChoose() => runApp(SendListChooseApp());

@pragma('vm:entry-point')
void setGas() => runApp(SetGasApp());

@pragma('vm:entry-point')
void transactionDetail() => runApp(TransactionDetailApp());
