import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter_redux/flutter_redux.dart';
import 'package:redux/redux.dart';
import 'package:flutter/services.dart';
import 'dart:async';

import './redux/state.dart';
import '../utils/hex_color.dart';

class AirdropWidget extends StatelessWidget {

  static const String methodChannel = "airdrop";
  static MethodChannel platform = const MethodChannel('airdrop');

  @override
  Widget build(BuildContext context) {
    print("render in stateless widget ");
    return StoreConnector<AppState, _ViewModel>(
        converter: (Store<AppState> store) => _ViewModel.create(store),
        builder: (BuildContext context, _ViewModel viewModel) => 
          Scaffold(
            appBar: null,
            backgroundColor: HexColor("#16162A"),
            body: Center(
              child: Column(
                mainAxisSize: MainAxisSize.max,
                children: <Widget>[
                  
                ]
              ),
            )
          )
    );
  }

}

class _ViewModel {
  final List<String> items;

  _ViewModel(this.items);

  factory _ViewModel.create(Store<AppState> store) {
    print("factory _ViewModel.create");
    List<String> items = store.state.toDos;
    return _ViewModel(items);
  }

}
