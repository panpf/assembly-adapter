# 通过 ConcatAdapter 实现 header 和 footer

RecyclerView 1.2.0 版本新增了 ConcatAdapter，它可以按顺序将多个 Adapter 连接起来作为一个 Adapter 设置给 RecyclerView

所以借助 ConcatAdapter 我们就可以轻松的实现 header 和 footer，如下：

```kotlin
val headerAdapter: AssemblyRecyclerAdapter = ...
val appAdapter: AssemblyRecyclerAdapter = ...
val footerAdapter: AssemblyRecyclerAdapter = ...

RecyclerView(activity).adapter = ConcatAdapter(headerAdapter, appAdapter, footerAdapter)

// 需要更新 header 或 footer 的时候只需更新相应 adapter 的数据即可，如下：
val headerDataList = listOf(...)
headerAdapter.submitList(headerDataList)
```

AssemblyAdapter 也为 BaseAdapter、BaseExpandableListAdapter、PagerAdapter、FragmentStatePagerAdapter
提供了专用的 ConcatAdapter 实现（[了解更多](concat_adapter.md)），因此这些 Adapter 也可以轻的实现 header 和 footer

### 更多示例

* [ListFragment]
* [ExpandableListFragment]
* [PagerViewFragment]
* [PagerFragmentFragment]
* [Pager2Fragment]
* [RecyclerLinearFragment]
* [RecyclerGridFragment]
* [RecyclerStaggeredGridFragment]

[ListFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/list/ListFragment.kt

[ExpandableListFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/list/ExpandableListFragment.kt

[PagerViewFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/pager/PagerViewFragment.kt

[PagerFragmentFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/pager/PagerFragmentFragment.kt

[Pager2Fragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/pager2/Pager2Fragment.kt

[RecyclerLinearFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/recycler/RecyclerLinearFragment.kt

[RecyclerGridFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/recycler/RecyclerGridFragment.kt

[RecyclerStaggeredGridFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/recycler/RecyclerStaggeredGridFragment.kt