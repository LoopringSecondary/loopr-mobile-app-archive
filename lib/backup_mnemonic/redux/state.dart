class AppState {
  final List<String> toDos;

  AppState(this.toDos);

  factory AppState.initial() => AppState(List.unmodifiable([]));
}
