# Span Size

[RecyclerView] 的 [GridLayoutManager] 和 [StaggeredGridLayoutManager] 支持让一个 item 横跨多列显示，但 api 使用起来较为复杂，[AssemblyAdapter] 将相应 api 进行了封装使用起来更方便

### GridLayoutManager

```kotlin
val recyclerView: RecyclerView = ...

// AssemblyGridLayoutManager 会自动从 ItemFactory 中读取 span size 并应用
recyclerView.layoutManager = AssemblyGridLayoutManager(context, 3, recyclerView)

val adapter = AssemblyRecyclerAdapter(dataList).apply {
    // 手动设置横跨多少列显示
    addItemFactory(UserItem.Factory().setSpanSize(3))

    // 或者通过 fullSpan(RecyclerView) 方法自动从 GridLayoutManager 获取 span count
    addItemFactory(UserItem.Factory().fullSpan(recyclerView))
}

recyclerView.setAdapter(adapter);
```

### StaggeredGridLayoutManager

```kotlin
val recyclerView: RecyclerView = ...

val adapter = AssemblyRecyclerAdapter(dataList).apply {
    // StaggeredGridLayoutManager 只能设置充满
    addItemFactory(UserItem.Factory().fullSpan(recyclerView))
}

recyclerView.setAdapter(adapter);
```


[RecyclerView]: https://developer.android.google.cn/reference/androidx/recyclerview/widget/RecyclerView
[GridLayoutManager]: https://developer.android.google.cn/reference/androidx/recyclerview/widget/GridLayoutManager
[StaggeredGridLayoutManager]: https://developer.android.google.cn/reference/androidx/recyclerview/widget/StaggeredGridLayoutManager
[AssemblyAdapter]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/AssemblyAdapter.java
