import 'package:redux/redux.dart';
import './state.dart';
import './actions.dart';

AppState appReducer(AppState state, action) => AppState(toDoListReducer(state.toDos, action), listStateReducer(state.listState, action));

final Reducer<List<String>> toDoListReducer = combineReducers([
  TypedReducer<List<String>, AddItemAction>(_addItem)
]);

List<String> _addItem(List<String> toDos, AddItemAction action) {
  toDos = action.items;
  return action.items;
} 

final Reducer<ListState> listStateReducer = combineReducers<ListState>([
  TypedReducer<ListState, DisplayListOnlyAction>(_displayListOnly),
  TypedReducer<ListState, DisplayListWithNewItemAction>(_displayListWithNewItem),
]);

ListState _displayListOnly(ListState listState, DisplayListOnlyAction action) => ListState.listOnly;

ListState _displayListWithNewItem(ListState listState, DisplayListWithNewItemAction action) => ListState.listWithNewItem;