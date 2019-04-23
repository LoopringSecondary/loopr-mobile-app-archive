import 'package:flutter/material.dart';
import 'backup_mnemonic_widget.dart';
import 'package:flutter_redux/flutter_redux.dart';
import 'package:redux/redux.dart';

import './actions.dart';
import './middleware.dart';
import './reducers.dart';
import './state.dart';

import 'package:flutter/services.dart';
import 'dart:async';

class BackupMnemonicApp extends StatelessWidget {
  final Store<AppState> store = Store<AppState>(
    appReducer,
    initialState: AppState.initial(),
    middleware: createStoreMiddleware(),
  );

  static const String methodChannel = "backupMnemonic";
  final MethodChannel platform = const MethodChannel('backupMnemonic');

  Future<void> methodCallHandler(MethodCall methodCall) async {
    switch (methodCall.method) {
      case 'backupMnemonic.update':
        print("received");
        print(methodCall.arguments);

        // InheritedStateContainer.of(context).appModel.words = methodCall.arguments;

        /*
        setState(() {
          _words = methodCall.arguments;
        });
        */
        store.dispatch(AddItemAction('hello'));
        store.dispatch(SaveListAction());
        store.dispatch(DisplayListOnlyAction());

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
