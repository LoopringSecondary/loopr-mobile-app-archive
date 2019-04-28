import 'package:redux/redux.dart';
import './state.dart';
import './actions.dart';

AppState appReducer(AppState state, action) => AppState(toDoListReducer(state.toDos, action));

final Reducer<List<String>> toDoListReducer = combineReducers([
  TypedReducer<List<String>, AddItemAction>(_addItem)
]);

List<String> _addItem(List<String> toDos, AddItemAction action) {
  toDos = action.items;
  return action.items;
} 
