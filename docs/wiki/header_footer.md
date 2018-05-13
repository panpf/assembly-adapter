# header 和 footer

在日常的开发中我们也有很多的场景需要使用 header 和 footer，但现在 Android 官方提供的 API 中只有 [ListView] 支持 header 和 footer，而借助 [AssemblyAdapter] 可以让 [GridView]、[Spinner]、[Gallery]、[RecyclerView]、[ExpandableListView]、[ViewPager] 也支持 header 和 footer

### 添加 header、footer

用于 header 和 footer 的 [Item] 同用于列表数据的 [Item] 并没有什么区别，因此先参考 [README] 中的介绍定义好一对 [Item] 和 [ItemFactory]

然后通过 [AssemblyAdapter] 的 `addHeaderItem(ItemFactory, Object)` 或 `addFooterItem(ItemFactory, Object)` 方法添加到 Adapter 中即可，如下：

```kotlin
val adapter = AssemblyRecyclerAdapter(objects).apply {
  addHeaderItem(HeaderItemFactory(), "我是小额头呀！")
  addFooterItem(HeaderItemFactory(), "我是小尾巴呀！")
}
```

addHeaderItem(ItemFactory, Object) 和 addFooterItem(ItemFactory, Object) 的第二个参数是 [Item] 需要的数据，直接传进去即可

### 隐藏或显示 header、footer

addHeaderItem() 或 addFooterItem() 都会返回一个用于控制 header 或 footer 的 [ItemHolder]，你可以通过 [ItemHolder] 显示或隐藏 header、footer，如下：

```kotlin
val adapter = AssemblyRecyclerAdapter(objects)

ItemHolder userItemHolder = adapter.addHeaderItem(HeaderItemFactory(), "我是小额头呀！")

// 隐藏
userItemHolder.enabled = false

// 显示
userItemHolder.enabled = true
```

### 更新 header、footer 的数据

```kotlin
val adapter = AssemblyRecyclerAdapter(objects)

ItemHolder userItemHolder = adapter.addHeaderItem(HeaderItemFactory(), "我是小额头呀！")

// 刷新数据
userItemHolder.data = "我是新的小额头呀！"
```

[AssemblyAdapter]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/AssemblyAdapter.java

[ItemFactory]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/ItemFactory.java
[Item]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/Item.java
[ItemHolder]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/ItemHolder.java

[ExpandableListView]: https://developer.android.google.cn/reference/android/widget/ExpandableListView
[GridView]: https://developer.android.google.cn/reference/android/widget/GridView
[ListView]: https://developer.android.google.cn/reference/android/widget/ListView
[Spinner]: https://developer.android.google.cn/reference/android/widget/Spinner
[Gallery]: https://developer.android.google.cn/reference/android/widget/Gallery
[RecyclerView]: https://developer.android.google.cn/reference/androidx/recyclerview/widget/RecyclerView
[ViewPager]: https://developer.android.google.cn/reference/androidx/viewpager/widget/ViewPager
[README]: ../../README.md
