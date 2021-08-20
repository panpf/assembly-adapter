# AssemblySingleData*Adapter

在使用 Concat\*Adapter 实现 header 或 footer 时，有时候需要一个只有一条数据的 Adapter 来作为 header 或 footer，这时候
AssemblySingleData\*Adapter 就派上用场了，如下：

```kotlin
val appsOverviewAdapter =
    AssemblySingleDataRecyclerAdapter(AppsOverviewItemFactory(requireActivity()))
val recyclerAdapter = AssemblyRecyclerAdapter<Any>(
    listOf(
        AppItemFactory(requireActivity()),
        ListSeparatorItemFactory(requireActivity())
    )
)
val footerLoadStateAdapter =
    AssemblySingleDataRecyclerAdapter(LoadStateItemFactory(requireActivity()))

binding.recyclerRecycler.apply {
    adapter = ConcatAdapter(appsOverviewAdapter, recyclerAdapter, footerLoadStateAdapter)
}

// 数据加载成功后更新数据
appsOverviewAdapter.data = AppsOverview()
footerLoadStateAdapter.data = LoadState.NotLoading(true)
```

### 更多示例

* [ListFragment]
* [ExpandableListFragment]
* [PagerViewFragment]
* [PagerFragmentFragment]
* [Pager2FragmentFragment]
* [RecyclerLinearFragment]
* [RecyclerGridFragment]
* [RecyclerStaggeredGridFragment]

[ListFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/list/ListFragment.kt

[ExpandableListFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/list/ExpandableListFragment.kt

[PagerViewFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/pager/PagerViewFragment.kt

[PagerFragmentFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/pager/PagerFragmentFragment.kt

[Pager2FragmentFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/pager2/Pager2FragmentFragment.kt

[RecyclerLinearFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/recycler/RecyclerLinearFragment.kt

[RecyclerGridFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/recycler/RecyclerGridFragment.kt

[RecyclerStaggeredGridFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/recycler/RecyclerStaggeredGridFragment.kt