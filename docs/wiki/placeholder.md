# 使用占位符

有时候需要显示一个具有一定长度的空列表，列表中所有元素都是空的，并且展现固定的样式

AssemblyAdapter 在检测到数据为 null 时会将其用 [Placeholder] 代替，然后去寻找匹配的 [ItemFactory]，因此我们只需要在创建 Adapter
时提供一个可以匹配 [Placeholder] 的 [ItemFactory] 即可，如下所示：

```kotlin
val placeholderItemFactory = ViewItemFactory<Placeholder>(
    dataClass = Placeholder::class,
    layoutResId = R.layout.item_app_placeholder
)
val recyclerAdapter = AssemblyRecyclerAdapter(
    listOf(
        AppItemFactory(requireActivity()),
        ListSeparatorItemFactory(requireActivity()),
        placeholderItemFactory,
    ),
    arrayOfNulls<Any?>(100).toList()
)
```

### 更多示例

* [AppPlaceholderItemFactory]
* [AppGroupPlaceholderItemFactory]
* [ListPlaceholderFragment]
* [ExpandableListPlaceholderFragment]
* [PagerFragmentPlaceholderFragment]
* [PagerViewPlaceholderFragment]
* [Pager2PlaceholderFragment]
* [RecyclerLinearPlaceholderFragment]
* [RecyclerListAdapterPlaceholderFragment]

[Placeholder]: ../../assemblyadapter-common-core/src/main/java/com/github/panpf/assemblyadapter/Placeholder.kt

[ItemFactory]: ../../assemblyadapter-common-item/src/main/java/com/github/panpf/assemblyadapter/ItemFactory.kt

[ListPlaceholderFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/list/ListPlaceholderFragment.kt

[ExpandableListPlaceholderFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/list/ExpandableListPlaceholderFragment.kt

[PagerViewPlaceholderFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/pager/PagerViewPlaceholderFragment.kt

[PagerFragmentPlaceholderFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/pager/PagerFragmentPlaceholderFragment.kt

[Pager2PlaceholderFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/pager2/Pager2PlaceholderFragment.kt

[RecyclerLinearPlaceholderFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/recycler/RecyclerLinearPlaceholderFragment.kt

[RecyclerListAdapterPlaceholderFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/recycler/RecyclerListAdapterPlaceholderFragment.kt

[AppGroupPlaceholderItemFactory]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/item/AppGroupPlaceholderItemFactory.kt

[AppPlaceholderItemFactory]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/item/AppPlaceholderItemFactory.kt