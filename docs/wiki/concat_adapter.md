# 为 BaseAdapter 等更多 Adapter 提供 Concat 支持

AssemblyAdapter 为常用的几种 Adapter 提供了 Concat 支持，如下：

* [BaseAdapter]：[ConcatListAdapter]
* [BaseExpandableListAdapter]：[ConcatExpandableListAdapter]
* [PagerAdapter]：[ConcatPagerAdapter]
* [FragmentStatePagerAdapter]：[ConcatFragmentStatePagerAdapter]

用法和 [ConcatAdapter] 一样，也能实现 header 和 footer [了解更多](header_and_footer.md)

### 更多示例

* [ListFragment]
* [ExpandableListFragment]
* [PagerViewFragment]
* [Pager2Fragment]

[ListFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/list/ListFragment.kt

[ExpandableListFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/list/ExpandableListFragment.kt

[PagerViewFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/pager/PagerViewFragment.kt

[Pager2Fragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/pager2/Pager2Fragment.kt

[ConcatListAdapter]: ../../assemblyadapter-list/src/main/java/com/github/panpf/assemblyadapter/list/ConcatListAdapter.kt

[ConcatExpandableListAdapter]: ../../assemblyadapter-list/src/main/java/com/github/panpf/assemblyadapter/list/expandable/ConcatExpandableListAdapter.kt

[ConcatPagerAdapter]: ../../assemblyadapter-pager/src/main/java/com/github/panpf/assemblyadapter/pager/ConcatPagerAdapter.kt

[ConcatFragmentStatePagerAdapter]: ../../assemblyadapter-pager/src/main/java/com/github/panpf/assemblyadapter/pager/ConcatFragmentStatePagerAdapter.kt

[BaseAdapter]: https://developer.android.google.cn/reference/android/widget/BaseAdapter

[BaseExpandableListAdapter]: https://developer.android.google.cn/reference/android/widget/BaseExpandableListAdapter

[PagerAdapter]: https://developer.android.google.cn/reference/androidx/viewpager/widget/PagerAdapter

[FragmentStatePagerAdapter]: https://developer.android.google.cn/reference/androidx/fragment/app/FragmentStatePagerAdapter

[FragmentStatePagerAdapter]: https://developer.android.google.cn/reference/androidx/fragment/app/FragmentStatePagerAdapter

[ConcatAdapter]: https://developer.android.google.cn/reference/androidx/recyclerview/widget/ConcatAdapter
