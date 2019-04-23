import 'dart:async';

import 'package:redux/redux.dart';
import './actions.dart';
import './state.dart';

List<Middleware<AppState>> createStoreMiddleware() => [
      TypedMiddleware<AppState, SaveListAction>(_saveList),
    ];

Future _saveList(Store<AppState> store, SaveListAction action, NextDispatcher next) async {
  print("saveList");
  next(action);
}