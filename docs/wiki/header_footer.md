# header 和 footer

在日常的开发中我们有很多的场景需要使用 header 和 footer，但现在 Android 官方提供的 API 中只有 [ListView] 支持 header 和 footer，而借助 [AssemblyAdapter] 可以让 [GridView]、[Spinner]、[Gallery]、[RecyclerView]、[ExpandableListView]、[ViewPager] 也支持 header 和 footer

### 添加 header、footer

用于 header 和 footer 的 [Item] 同用于列表数据的 [Item] 并没有任何区别，因此先参考 [README] 中的介绍定义好一对 [Item] 和 [ItemFactory]，如下：

```kotlin
class TextItem(parent: ViewGroup) : AssemblyItem<String>(R.layout.item_text, parent) {

    override fun onSetData(position: Int, s: String?) {
        (itemView as TextView).text = s
    }

    class Factory : AssemblyItemFactory<String>() {

        override fun match(data: Any?) = data is String

        override fun createAssemblyItem(parent: ViewGroup) = TextItem(parent)
    }
}
```

然后通过 [AssemblyAdapter] 的 `addHeaderItem(ItemFactory, Object)` 或 `addFooterItem(ItemFactory, Object)` 方法添加到 Adapter 中即可，如下：

```kotlin
val adapter = AssemblyRecyclerAdapter().apply {
  addHeaderItem(TextItem.Factory(), "我是小额头呀！")
  addFooterItem(TextItem.Factory(), "我是小尾巴呀！")
}
```

addHeaderItem(ItemFactory, Object) 和 addFooterItem(ItemFactory, Object) 的第二个参数是 [Item] 需要的数据，直接传进去即可

### 控制 header、footer 显示隐藏或更新数据

```kotlin
val adapter = AssemblyRecyclerAdapter()
adapter.addHeaderItem(TextItem.Factory(), "我是小额头 1 呀！")
adapter.addHeaderItem(TextItem.Factory(), "我是小额头 2 呀！")
adapter.addFooterItem(TextItem.Factory(), "我是小尾巴 1 呀！")
adapter.addFooterItem(TextItem.Factory(), "我是小尾巴 2 呀！")

// 隐藏 header1 和 footer 2
adapter.headerItemManager.setItemEnabled(0, false)
adapter.footerItemManager.setItemEnabled(1, false)

// 显示 header1 和 footer 2
adapter.headerItemManager.setItemEnabled(0, true)
adapter.footerItemManager.setItemEnabled(1, true)

// 更新 header2 和 footer 1 的数据
adapter.headerItemManager.setItemData(1, "我不是小额头 2 呀！")
adapter.footerItemManager.setItemData(0, "我不是小尾巴 1 呀！")
```

[AssemblyAdapter]: ../../assembly-adapter/src/main/java/me/panpf/adapter/AssemblyAdapter.java
[ItemFactory]: ../../assembly-adapter/src/main/java/me/panpf/adapter/ItemFactory.java
[Item]: ../../assembly-adapter/src/main/java/me/panpf/adapter/Item.java

[ExpandableListView]: https://developer.android.google.cn/reference/android/widget/ExpandableListView
[GridView]: https://developer.android.google.cn/reference/android/widget/GridView
[ListView]: https://developer.android.google.cn/reference/android/widget/ListView
[Spinner]: https://developer.android.google.cn/reference/android/widget/Spinner
[Gallery]: https://developer.android.google.cn/reference/android/widget/Gallery
[RecyclerView]: https://developer.android.google.cn/reference/androidx/recyclerview/widget/RecyclerView
[ViewPager]: https://developer.android.google.cn/reference/androidx/viewpager/widget/ViewPager
[README]: ../../README.md
