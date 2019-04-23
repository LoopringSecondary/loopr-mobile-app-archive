import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';
import 'dart:async';

import './backup_mnemonic_grid_view_widget.dart';

import 'package:flutter_redux/flutter_redux.dart';
import 'package:meta/meta.dart';
import 'package:redux/redux.dart';

import './actions.dart';
import './middleware.dart';
import './reducers.dart';
import './state.dart';

import '../utils/hex_color.dart';

class BackupMnemonicWidget extends StatelessWidget {
  
  static const String methodChannel = "backupMnemonic";
  static MethodChannel platform = const MethodChannel('backupMnemonic');

  // Send data to native
  Future<void> _pressedVerifyButton() async {
    try {
      MethodChannel channel = const MethodChannel(methodChannel);
      await channel.invokeMethod("backupMnemonic.verify", []);
    } on Exception catch (e) {
      print("MethodChannel... $e");
    }
  }

  Future<void> _pressedSkipButton() async {
    try {
      MethodChannel channel = const MethodChannel(methodChannel);
      await channel.invokeMethod("backupMnemonic.skip", []);
    } on Exception catch (e) {
      print("MethodChannel... $e");
    }
  }

  @override
  Widget build(BuildContext context) {
    print("render in stateless widget ");

    return StoreConnector<AppState, _ViewModel>(
        converter: (Store<AppState> store) => _ViewModel.create(store),
        builder: (BuildContext context, _ViewModel viewModel) => 
          Scaffold(
              appBar: AppBar(
                title: Text(viewModel.pageTitle),
              ),
              body: ListView(children: 
                viewModel.items.map((_ItemViewModel item) => _createWidget(item)).toList()),
            ),
      );
  }

}

class _ViewModel {
  final String pageTitle;
  final List<_ItemViewModel> items;
  final Function() onAddItem;
  final String newItemToolTip;
  final IconData newItemIcon;


  static const String methodChannel = "backupMnemonic";
  static MethodChannel platform = const MethodChannel('backupMnemonic');

  _ViewModel(this.pageTitle, this.items, this.onAddItem, this.newItemToolTip, this.newItemIcon);

  factory _ViewModel.create(Store<AppState> store) {

    print("factory _ViewModel.create");

    List<_ItemViewModel> items = store.state.toDos
        .map((String item) => _ToDoItemViewModel(item, () {
              store.dispatch(RemoveItemAction(item));
              store.dispatch(SaveListAction());
            }, 'Delete', Icons.delete) as _ItemViewModel)
        .toList();

    if (store.state.listState == ListState.listWithNewItem) {
      items.add(_EmptyItemViewModel('Type the next task here', (String title) {
        store.dispatch(DisplayListOnlyAction());
        store.dispatch(AddItemAction(title));
        store.dispatch(SaveListAction());
        print("Type the next task here");
      }, 'Add'));
    }

    return _ViewModel('To Do', items, () => store.dispatch(DisplayListWithNewItemAction()), 'Add new to-do item', Icons.add);
  }

}

abstract class _ItemViewModel {}

@immutable
class _ToDoItemViewModel extends _ItemViewModel {
  final String title;
  final Function() onDeleteItem;
  final String deleteItemToolTip;
  final IconData deleteItemIcon;

  _ToDoItemViewModel(this.title, this.onDeleteItem, this.deleteItemToolTip, this.deleteItemIcon);
}

// 
class _BackupMnemonicWidgetState extends State<BackupMnemonicWidget> {
  List<String> _words = [];

  Future<void> methodCallHandler(MethodCall methodCall) async {
    switch (methodCall.method) {
      case 'backupMnemonic.update':
        print("received");
        print(methodCall.arguments);
        InheritedStateContainer.of(context).appModel.words = methodCall.arguments;

        setState(() {
          _words = methodCall.arguments;
        });
        break;
      default:
        print("other methods");
    }
  }


  @override
  Widget build(BuildContext context) {
    print("render in backup_mnemonic_widget ...  $_words");

    return Scaffold(
      appBar: null,
      backgroundColor: HexColor("#16162A"),
      body: Center(
        child: Column(
          mainAxisSize: MainAxisSize.max,
          // mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            new Container(
              height: 10.0,
              width: 180.0,
            ),
            new Container(
              constraints: new BoxConstraints.expand(
                height: 200
              ),
              padding: new EdgeInsets.only(left: 18, right: 18),
              decoration: new BoxDecoration(
                image: new DecorationImage(
                  image: new AssetImage('assets/mnemonic-1@3x.png')
                )
              ),
              child: Column(
                children: <Widget>[
                  new Container(
                    // TODO: need to set line height.
                    padding: EdgeInsets.only(left: 20.0, right: 20.0, top: 30),
                      child: new Text(
                      "Please don't show up in public places (prevent cameras from taking photos) or take a screen shot(your operating system may back up images to cloud storage). These operations can bring you huge and irreversible security risks.",
                      style: TextStyle(
                        color: Colors.white,
                        fontSize: 15,
                        fontStyle: FontStyle.italic),
                    ),
                  )
                ],
              )
            ),
            new Container(
              constraints: new BoxConstraints.expand(
                height: 200
              ),
              padding: new EdgeInsets.only(left: 20, right: 20),
              decoration: new BoxDecoration(
                image: new DecorationImage(
                  image: new AssetImage('assets/mnemonic-2@3x.png')
                )
              ),
              child: InheritedStateContainer(
                        child: new BackupMnemonicGridViewWidget(),
                        appModel: model,
                      ),
            ),
            new Expanded(
              child: new Align(
                alignment: Alignment.bottomCenter,
                child: Row(
                  mainAxisSize: MainAxisSize.max,
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children: <Widget>[
                    new Container(
                      height: 10.0,
                      width: 20.0,
                    ),
                    Expanded(child: SizedBox(
                      height: 45,
                      child: 
                        CupertinoButton(
                          color: HexColor.theme,
                          padding: EdgeInsets.only(left: 2, right: 2),
                          borderRadius: BorderRadius.circular(22),
                          child: const Text(
                            'Verify',
                            style: TextStyle(color: Colors.white, fontSize: 16),
                          ),
                          onPressed: () {
                            _pressedVerifyButton();
                            print("_pressedVerifyButton onPressed");
                          },
                        ),
                    )),
                    new Container(
                      height: 10.0,
                      width: 15.0,
                    ),
                    Expanded(child: SizedBox(
                      height: 45,
                      child:
                        CupertinoButton(
                          color: HexColor("#2B2C47"),
                          padding: EdgeInsets.only(left: 2, right: 2),
                          borderRadius: BorderRadius.circular(22),
                          child: const Text(
                            'Skip',
                            style: TextStyle(color: Colors.white, fontSize: 16),
                          ),
                          onPressed: () {
                            _pressedSkipButton();
                            print("_pressedSkipButton onPressed");
                          },
                        ),
                    )),
                    new Container(
                      height: 10.0,
                      width: 20.0,
                    ),
                  ]
                )
              ),
            ),
            new Container(
              height: 30.0,
              width: 180.0,
            ),
          ],
        ),
      ),
    );
  }
}
