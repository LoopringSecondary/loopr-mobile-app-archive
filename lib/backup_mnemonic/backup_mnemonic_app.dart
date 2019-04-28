import 'package:flutter/material.dart';
import 'backup_mnemonic_widget.dart';
import 'package:flutter_redux/flutter_redux.dart';
import 'package:redux/redux.dart';

import './redux/actions.dart';
import './redux/reducers.dart';
import './redux/state.dart';

import 'package:flutter/services.dart';
import 'dart:async';

class BackupMnemonicApp extends StatelessWidget {
  final Store<AppState> store = Store<AppState>(
    appReducer,
    initialState: AppState.initial(),
  );

  static const String methodChannel = "backupMnemonic";
  final MethodChannel platform = const MethodChannel('backupMnemonic');

  Future<void> methodCallHandler(MethodCall methodCall) async {
    switch (methodCall.method) {
      case 'backupMnemonic.update':
        print("received");
        print(methodCall.arguments);
        // TODO: this line of code is very critical!
        // We need to cast the data. Otherwise, it fails.
        List<String> newMnemonic = methodCall.arguments.cast<String>();
        store.dispatch(AddItemAction(newMnemonic));
        break;
      default:
        print("other methods");
    }
  }

  @override
  Widget build(BuildContext context) {
    platform.setMethodCallHandler(methodCallHandler);

    return StoreProvider(
      store: this.store,
      child: MaterialApp(
        title: '',
        debugShowCheckedModeBanner: false,
        routes: {
          '/': (context) => BackupMnemonicWidget(),
        },
        theme: ThemeData(
          primarySwatch: Colors.blue,
        ),
      )
    );
  }
}
