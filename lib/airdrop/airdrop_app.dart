import 'package:flutter/material.dart';
import 'package:flutter_redux/flutter_redux.dart';
import 'package:redux/redux.dart';

import 'airdrop_widget.dart';

import './redux/actions.dart';
import './redux/reducers.dart';
import './redux/state.dart';

class AirdropApp extends StatelessWidget {
  final Store<AppState> store = Store<AppState>(
    appReducer,
    initialState: AppState.initial(),
  );

  @override
  Widget build(BuildContext context) {
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
