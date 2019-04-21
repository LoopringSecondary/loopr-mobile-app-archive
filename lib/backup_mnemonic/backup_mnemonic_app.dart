import 'package:flutter/material.dart';
import 'backup_mnemonic_widget.dart';
import 'package:flutter_redux/flutter_redux.dart';
import 'package:redux/redux.dart';

import 'backup_mnemonic_stateless_widget.dart';

import './middleware.dart';
import './reducers.dart';
import './state.dart';

class BackupMnemonicApp extends StatelessWidget {
  final Store<AppState> store = Store<AppState>(
    appReducer,
    initialState: AppState.initial(),
    middleware: createStoreMiddleware(),
  );

  @override
  Widget build(BuildContext context) {
    return StoreProvider(
      store: this.store,
      child: MaterialApp(
        title: '',
        debugShowCheckedModeBanner: false,
        routes: {
          '/': (context) => ToDoListPage(),
        },
        theme: ThemeData(
          primarySwatch: Colors.blue,
        ),
      )
    );
  }
}
