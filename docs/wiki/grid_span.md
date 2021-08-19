# 设置 spanSize 和 fullSpan

[RecyclerView] 的 [GridLayoutManager] 和 [StaggeredGridLayoutManager] 支持让一个 item 横跨多列显示，但 API
使用起来较为复杂

AssemblyAdapter 将相应 API 进行了封装提供了 [AssemblyGridLayoutManager] 和 [AssemblyStaggeredGridLayoutManager]，可以通过 ItemFactory 来设置 spanSize 或 fullSpan，使用起来更方便

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

    layoutManager = AssemblyGridLayoutManager(
        context = context,
        spanCount = 3,
        gridLayoutItemSpanMap = mapOf(
            AppsOverviewItemFactory::class to ItemSpan.fullSpan(),  // 配置 AppsOverviewItemFactory 充满 span
            ListSeparatorItemFactory::class to ItemSpan.span(2) // 配置 ListSeparatorItemFactory 跨两个 span
        )
    )
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

    layoutManager = AssemblyStaggeredGridLayoutManager(
        spanCount = 3,
        // 配置 AppsOverviewItemFactory 和 ListSeparatorItemFactory 充满 span
        fullSpanItemFactoryList = listOf(
            AppsOverviewItemFactory::class,
            ListSeparatorItemFactory::class
        )
    )
}
```

[AssemblyGridLayoutManager]: ../../assemblyadapter-recycler/src/main/java/com/github/panpf/assemblyadapter/recycler/AssemblyGridLayoutManager.kt

[AssemblyStaggeredGridLayoutManager]: ../../assemblyadapter-recycler/src/main/java/com/github/panpf/assemblyadapter/recycler/AssemblyStaggeredGridLayoutManager.kt

[RecyclerView]: https://developer.android.google.cn/reference/androidx/recyclerview/widget/RecyclerView

[GridLayoutManager]: https://developer.android.google.cn/reference/androidx/recyclerview/widget/GridLayoutManager

[StaggeredGridLayoutManager]: https://developer.android.google.cn/reference/androidx/recyclerview/widget/StaggeredGridLayoutManager