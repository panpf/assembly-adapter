# 设置 spanSize 和 fullSpan

[RecyclerView] 的 [GridLayoutManager] 和 [StaggeredGridLayoutManager] 支持让一个 item 横跨多列显示，但 API 使用起来较为复杂

AssemblyAdapter 将相应 API 进行了封装提供了 [AssemblyGridLayoutManager] 和 [AssemblyStaggeredGridLayoutManager]
，可以通过 position, itemType, ItemFactory 来设置 spanSize 或 fullSpan，使用起来更方便

### 示例

```kotlin
/**
 * AssemblyGridLayoutManager
 */
RecyclerView(context).apply {
    adapter = AssemblyRecyclerAdapter<Any>(
        listOf(
            AppInfoItemFactory(),
            AppsOverviewItemFactory(),
            ListSeparatorItemFactory()
        )
    )

    layoutManager = newAssemblyGridLayoutManager(spanCount = 3) {
        itemSpanByPosition(
            10 to ItemSpan.fullSpan(),  // 配置 position 为 10 的 item 充满 span
            6 to ItemSpan.span(2) // 配置 position 为 6 的 item 跨两个 span
        )
        itemSpanByItemTypePosition(
            1 to ItemSpan.fullSpan(),  // 配置 itemType 为 1 的 item 充满 span
            2 to ItemSpan.span(2) // 配置 itemType 为 2 的 item 跨两个 span
        )
        itemSpanByItemFactory(
            AppsOverviewItemFactory::class to ItemSpan.fullSpan(),  // 配置 AppsOverviewItemFactory 充满 span
            ListSeparatorItemFactory::class to ItemSpan.span(2) // 配置 ListSeparatorItemFactory 跨两个 span
        )
    }
}

/**
 * AssemblyStaggeredGridLayoutManager
 */
RecyclerView(context).apply {
    adapter = AssemblyRecyclerAdapter<Any>(
        listOf(
            AppInfoItemFactory(),
            AppsOverviewItemFactory(),
            ListSeparatorItemFactory()
        )
    )

    layoutManager = newAssemblyStaggeredGridLayoutManager(spanCount = 3) {
        fullSpanByPosition(10, 6)   // 配置 position 为 10 和 6 的 item 充满 span
        fullSpanByItemTypePosition(1, 2)  // 配置 itemType 为 1 和 2 的 item 充满 span
        // 配置 AppsOverviewItemFactory 和 ListSeparatorItemFactory 充满 span
        fullSpanByItemFactory(AppsOverviewItemFactory::class, ListSeparatorItemFactory::class)
    }
}
```

### 更多示例

* [RecyclerGridFragment]
* [RecyclerStaggeredGridFragment]

[RecyclerGridFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/recycler/RecyclerGridFragment.kt

[RecyclerStaggeredGridFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/recycler/RecyclerStaggeredGridFragment.kt

[AssemblyGridLayoutManager]: ../../assemblyadapter-recycler/src/main/java/com/github/panpf/assemblyadapter/recycler/AssemblyGridLayoutManager.kt

[AssemblyStaggeredGridLayoutManager]: ../../assemblyadapter-recycler/src/main/java/com/github/panpf/assemblyadapter/recycler/AssemblyStaggeredGridLayoutManager.kt

[RecyclerView]: https://developer.android.google.cn/reference/androidx/recyclerview/widget/RecyclerView

[GridLayoutManager]: https://developer.android.google.cn/reference/androidx/recyclerview/widget/GridLayoutManager

[StaggeredGridLayoutManager]: https://developer.android.google.cn/reference/androidx/recyclerview/widget/StaggeredGridLayoutManager