# ViewItemFactory

有的时候只需要在列表的固定位置显示一个不需要数据的布局或 view，使用 [ViewItemFactory] 可以省去创建额外 [ItemFactory] 的成本，如下：

```kotlin
val adapter = AssemblyRecyclerAdapter(objects).apply {
  addHeaderItem(ViewItemFactory(R.layout.list_header))
}
```

[ViewItemFactory]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/ViewItemFactory.java
[ItemFactory]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/ItemFactory.java
