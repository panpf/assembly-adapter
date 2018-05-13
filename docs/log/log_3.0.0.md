3.0.0 是一个重大重构版本，不向下兼容 :fire::fire::fire::fire::fire:

### 1. 重构

#### 1.1 合并

* AssemblyRecyclerItemFactory、AssemblyGroupItemFactory、AssemblyChildItemFactory 合并到 [AssemblyItemFactory]
* AssemblyRecyclerItem、AssemblyGroupItem、AssemblyChildItem 合并到 [AssemblyItem]
* FixedRecyclerItemInfo、FixedGroupItemInfo 合并到 FixedItemInfo 并重命名为 [ItemHolder]
* AssemblyLoadMoreRecyclerItemFactory、AssemblyLoadMoreGroupItemFactory 合并到 AssemblyLoadMoreItemFactory 并重命名为 [AssemblyMoreItemFactory]
* AssemblyLoadMoreRecyclerItem、AssemblyLoadMoreGroupItem 合并到 AssemblyLoadMoreItem 并重命名为 [AssemblyMoreItem]
* LoadMoreFixedRecyclerItemInfo、LoadMoreFixedGroupItemInfo 合并到 LoadMoreFixedItemInfo 并重命名为 [MoreItemHolder]
* OnRecyclerLoadMoreListener、OnGroupLoadMoreListener 合并到 [OnLoadMoreListener]

#### 1.2 重命名

* 原 AssemblyAdapter 重命名为 [AssemblyListAdapter]，现 [AssemblyAdapter] 是一个标准接口
* FixedItemInfo 重命名为 [ItemHolder]
* FragmentFixedItemInfo 重命名为 [FragmentItemHolder]
* PagerFixedItemInfo 重命名为 [PagerItemHolder]
* AssemblyLoadMoreItemFactory 重命名为 [AssemblyMoreItemFactory]
* AssemblyLoadMoreItem 重命名为 [AssemblyMoreItem]
* [ItemFactory].isTarget(Object) 重命名为 match(Object)
* [AssemblyAdapter].setLoadMoreItem(MoreItemFactory) 重命名为 setMoreItem(MoreItemFactory)

#### 1.3 行为变更

* [AssemblyAdapter] 中删除 setDisableLoadMore(boolean) 方法，新增 setEnabledMoreItem(boolean) 方法代替，但是意义已经完全不一样了
* [AssemblyItem].onConfigViews(Context) 现在是非 abstract 的，默认不重写
* [ItemFactory] 的泛型现在是 DATA（跟 Item 一样），需要你手动修改所有子类的泛型
* 最低支持 API 升到 14

#### 1.4 挪动位置

* ViewPager 相关移动到 `me.panpf.adapter.pager` 目录下，[AssemblyPagerAdapter]、[AssemblyFragmentPagerAdapter] 等
* 加载更多相关移动到 `me.panpf.adapter.more` 目录下，[AssemblyMoreItemFactory]、[AssemblyMoreItem] 等
* expandable 相关移动到 `me.panpf.adapter.expandable` 目录下，[AssemblyGroup]
* RecyclerView 相关移动到 `me.panpf.adapter.recycler` 目录下，[AssemblyRecyclerLinerDivider] 等

#### 1.5 删除

* 删除 [AssemblyAdapter].setLoadMoreEnd(boolean) 方法，使用 loadMoreFinished(boolean) 方法代替

### 2. 新功能

#### 2.1 通用
* 新增通用的 view [ItemFactory]，可快速在列表中显示一个 view 或布局，省去自定义 [ItemFactory] 的成本，详情请参见
* [ItemFactory] 和 [AssemblyPagerItemFactory] 新增四种点击监听设置方法，可对 item 或某个 view 的点击设置监听，省去在 [ItemFactory] 中定义监听并执行回调的成本，详情请参见

### 2.2 RecyclerView
* 新增 [AssemblyRecyclerLinerDivider] 可定制头和尾巴不显示分割线
* 新增 [AssemblyGridLayoutManager] 类搭配 [ItemFactory] 的 setSpanSize(int) 和 fullSpan() 方法方便的控制占多少列

#### 2.3 Kotlin 支持
* 新增 assembly-adapter-ktx 模块，提供了 [Item] 的 bindview 功能，详情请参见

[AssemblyAdapter]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/AssemblyAdapter.java
[AssemblyListAdapter]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/AssemblyListAdapter.java
[AssemblyItemFactory]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/AssemblyItemFactory.java
[AssemblyItem]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/AssemblyItem.java

[ItemFactory]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/ItemFactory.java
[Item]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/Item.java
[ItemHolder]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/ItemHolder.java

[AssemblyGroup]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/expandable/AssemblyGroup.java

[AssemblyMoreItemFactory]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/more/AssemblyMoreItemFactory.java
[AssemblyMoreItem]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/more/AssemblyMoreItem.java
[MoreItemHolder]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/more/MoreItemHolder.java
[OnLoadMoreListener]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/more/OnLoadMoreListener.java

[AssemblyPagerItemFactory]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/pager/AssemblyPagerItemFactory.java
[FragmentItemHolder]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/pager/FragmentItemHolder.java
[PagerItemHolder]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/pager/PagerItemHolder.java
[AssemblyPagerAdapter]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/pager/AssemblyPagerAdapter.java
[AssemblyFragmentPagerAdapter]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/pager/AssemblyFragmentPagerAdapter.java

[AssemblyRecyclerLinerDivider]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/recycler/AssemblyRecyclerLinerDivider.java
[AssemblyGridLayoutManager]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/recycler/AssemblyGridLayoutManager.java

[ViewItemFactory]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/ViewItemFactory.java
