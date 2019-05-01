import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_redux/flutter_redux.dart';
import 'package:redux/redux.dart';
import 'dart:async';

import 'airdrop_widget.dart';

import './redux/actions.dart';
import './redux/reducers.dart';
import './redux/state.dart';

class AirdropApp extends StatelessWidget {
  final Store<AppState> store = Store<AppState>(
    appReducer,
    initialState: AppState.initial(),
  );

  static const String methodChannel = "airdrop";
  final MethodChannel platform = const MethodChannel('airdrop');

  Future<void> methodCallHandler(MethodCall methodCall) async {
    switch (methodCall.method) {
      case 'airdrop.update':
        print("received");
        print(methodCall.arguments);
        // TODO: this line of code is very critical!
        // We need to cast the data. Otherwise, it fails.
        // List<String> newMnemonic = methodCall.arguments.cast<String>();
        // store.dispatch(AddItemAction(newMnemonic));
        break;
      default:
        print("other methods");
    }
  }

  @override
  Widget build(BuildContext context) {
    platform.setMethodCallHandler(methodCallHandler);

    print("AirdropApp");
    return StoreProvider(
      store: this.store,
      child: MaterialApp(
        title: '',
        debugShowCheckedModeBanner: false,
        routes: {
          '/': (context) => AirdropWidget(),
        },
        theme: ThemeData(
          primarySwatch: Colors.blue,
        ),
      )
    );
  }
}
