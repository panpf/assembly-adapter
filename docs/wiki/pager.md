# 支持 ViewPager 和 ViewPager2

## ViewPager

### 导入

首先需要导入 `assemblyadapter-pager` 模块，请参考 [README] 中的 `导入` 部分

AssemblyAdapter 提供了以下 Adapter 来支持 ViewPager：

* [AssemblyPagerAdapter]: 多类型 Adapter 实现
* [AssemblySingleDataPagerAdapter]：单数据实现
* [ConcatPagerAdapter]: 连接 Adapter 实现
* [AssemblyFragmentStatePagerAdapter]: 多类型 Adapter 实现
* [AssemblySingleDataFragmentStatePagerAdapter]：单数据实现

### Concat 支持

通过 [ConcatPagerAdapter] 和 [ConcatFragmentStatePagerAdapter] 也为 [PagerAdapter]
和 [FragmentStatePagerAdapter] 提供了连接支持，可以轻松的支持 header 和 footer，用法同 [ConcatAdapter] 一样

### ViewPager 数据刷新支持

ViewPager 有一个一直以来都存在的 bug，就是在 PagerAdapter 数据改变时即使调用了 `notifyDataSetChanged` 方法 ViewPager 也不会刷新其内容

原因是 ViewPager 在收到数据改变回调时会通过 PagerAdapter 的 getItemPosition 方法依次获取当前显示的 item 的 position，只有 position
是 PagerAdapter.POSITION_NONE 时才会更新

而 PagerAdapter 的 getItemPosition 方法默认返回值是 PagerAdapter.POSITION_UNCHANGED，所以我们需要重写 getItemPosition 方法，在 item 对应的数据改变时返回 PagerAdapter.POSITION_NONE 即可

AssemblyAdapter 提供的所有 Pager 相关 Adapter 都重写了 getItemPosition 方法对数刷新提供了支持可放心使用

## ViewPager2

### 导入

首先需要导入 `assemblyadapter-pager2` 、`assemblyadapter-pager2-paging` 模块，请参考 [README] 中的 `导入` 部分

AssemblyAdapter 提供了以下 Adapter 来支持 ViewPager2：

* [AssemblyFragmentStateAdapter]: 多类型 Adapter 实现
* [AssemblySingleDataFragmentStateAdapter]：单数据实现
* [PagingDataFragmentStateAdapter]：Paging 实现
* [LoadStateFragmentStateAdapter]：LoadState 实现
* [AssemblyPagingDataFragmentStateAdapter]：多类型 + Paging Adapter 实现
* [AssemblyLoadStateFragmentStateAdapter]：多类型 + Paging 加载状态 Adapter 实现

### Paging 支持

AssemblyAdapter 提供了 [PagingDataFragmentStateAdapter] 和 [LoadStateFragmentStateAdapter]
、[AssemblyPagingDataFragmentStateAdapter]、[AssemblyLoadStateFragmentStateAdapter]
来为 [FragmentStateAdapter] 提供 Paging 支持，用法同 [PagingDataAdapter] 和  [AssemblyPagingDataAdapter] 一样

### 更多示例

* item
    * [AppGroupPagerItemFactory]
    * [AppGroupFragmentItemFactory]
* PagerAdapter
    * [PagerViewFragment]
    * [PagerViewPlaceholderFragment]
* PagerFragmentStatePagerAdapter
    * [PagerFragmentFragment]
    * [PagerFragmentPlaceholderFragment]
* PagerFragmentStateAdapter
    * [Pager2FragmentFragment]
    * [Pager2FragmentPagingFragment]
    * [Pager2FragmentPlaceholderFragment]

[AppGroupPagerItemFactory]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/item/pager/AppGroupPagerItemFactory.kt

[AppGroupFragmentItemFactory]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/item/pager/AppGroupFragmentItemFactory.kt

[PagerViewFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/pager/PagerViewFragment.kt

[PagerViewPlaceholderFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/pager/PagerViewPlaceholderFragment.kt

[PagerFragmentFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/pager/PagerFragmentFragment.kt

[PagerFragmentPlaceholderFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/pager/PagerFragmentPlaceholderFragment.kt

[Pager2FragmentFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/pager2/Pager2FragmentFragment.kt

[Pager2FragmentPagingFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/pager2/Pager2FragmentPagingFragment.kt

[Pager2FragmentPlaceholderFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/pager2/Pager2FragmentPlaceholderFragment.kt

[README]: ../../README.md

[AssemblyPagerAdapter]: ../../assemblyadapter-pager/src/main/java/com/github/panpf/assemblyadapter/pager/AssemblyPagerAdapter.kt

[AssemblySingleDataPagerAdapter]: ../../assemblyadapter-pager/src/main/java/com/github/panpf/assemblyadapter/pager/AssemblySingleDataPagerAdapter.kt

[ConcatPagerAdapter]: ../../assemblyadapter-pager/src/main/java/com/github/panpf/assemblyadapter/pager/ConcatPagerAdapter.kt

[ConcatFragmentStatePagerAdapter]: ../../assemblyadapter-pager/src/main/java/com/github/panpf/assemblyadapter/pager/ConcatFragmentStatePagerAdapter.kt

[AssemblySingleDataFragmentStatePagerAdapter]: ../../assemblyadapter-pager/src/main/java/com/github/panpf/assemblyadapter/pager/AssemblySingleDataFragmentStatePagerAdapter.kt

[AssemblyFragmentStatePagerAdapter]: ../../assemblyadapter-pager/src/main/java/com/github/panpf/assemblyadapter/pager/AssemblyFragmentStatePagerAdapter.kt

[AssemblyFragmentStateAdapter]: ../../assemblyadapter-pager2/src/main/java/com/github/panpf/assemblyadapter/pager2/AssemblyFragmentStateAdapter.kt

[AssemblySingleDataFragmentStateAdapter]: ../../assemblyadapter-pager2/src/main/java/com/github/panpf/assemblyadapter/pager2/AssemblySingleDataFragmentStateAdapter.kt

[PagingDataFragmentStateAdapter]: ../../assemblyadapter-pager2-paging/src/main/java/com/github/panpf/assemblyadapter/pager2/paging/PagingDataFragmentStateAdapter.kt

[LoadStateFragmentStateAdapter]: ../../assemblyadapter-pager2-paging/src/main/java/com/github/panpf/assemblyadapter/pager2/paging/LoadStateFragmentStateAdapter.kt

[AssemblyPagingDataFragmentStateAdapter]: ../../assemblyadapter-pager2-paging/src/main/java/com/github/panpf/assemblyadapter/pager2/paging/AssemblyPagingDataFragmentStateAdapter.kt

[AssemblyPagingDataFragmentStateAdapter]: ../../assemblyadapter-pager2-paging/src/main/java/com/github/panpf/assemblyadapter/pager2/paging/AssemblyPagingDataFragmentStateAdapter.kt

[AssemblyLoadStateFragmentStateAdapter]: ../../assemblyadapter-pager2-paging/src/main/java/com/github/panpf/assemblyadapter/pager2/paging/AssemblyLoadStateFragmentStateAdapter.kt

[AssemblyPagingDataAdapter]: ../../assemblyadapter-recycler-paging/src/main/java/com/github/panpf/assemblyadapter/recycler/paging/AssemblyPagingDataAdapter.kt

[PagerAdapter]: https://developer.android.google.cn/reference/androidx/viewpager/widget/PagerAdapter

[FragmentStatePagerAdapter]: https://developer.android.google.cn/reference/androidx/fragment/app/FragmentStatePagerAdapter

[FragmentStateAdapter]: https://developer.android.google.cn/reference/androidx/viewpager2/adapter/FragmentStateAdapter

[ConcatAdapter]: https://developer.android.google.cn/reference/androidx/recyclerview/widget/ConcatAdapter

[PagingDataAdapter]: https://developer.android.google.cn/reference/androidx/paging/PagingDataAdapter