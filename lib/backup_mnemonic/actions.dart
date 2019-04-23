class RemoveItemAction {
  final String item;

  RemoveItemAction(this.item);
}

class AddItemAction {
  final List<String> items;

  AddItemAction(this.items);
}

class DisplayListOnlyAction {}

class DisplayListWithNewItemAction {}

class SaveListAction {}