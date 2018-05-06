*LoadMore* 重命名为 *More*
*LoadMore* 重命名为 *More*
*FixedItemInfo 重命名为 *ItemHolder

Adapter 中删除 setDisableLoadMore(boolean)、loadMoreFinished(boolean)、loadMoreFailed() 方法，请通过 getMoreFixedItemInfo() 获取 MoreFixedItemInfo 后，通过 MoreFixedItemInfo 使用相关替代方法

AssemblyItem.onConfigViews(Context) 现在是非 abstract 的

AssemblyItemFactory 和 AssemblyPagerItemFactory 新增四种点击监听设置方法：
* setOnViewClickListener(@IdRes int, @NonNull OnClickListener)
* setOnItemClickListener(@NonNull OnClickListener onClickListener)
* setOnViewLongClickListener(@IdRes int, @NonNull OnLongClickListener)
* setOnItemLongClickListener(@NonNull OnLongClickListener)

升级指南：
* 全局搜索 `AssemblyRecyclerItemFactory` 替换为 `AssemblyItemFactory`（记得勾选 'Match case' 和 'Words'）
* 全局搜索 `AssemblyRecyclerItem` 替换为 `AssemblyItem`（记得勾选 'Match case' 和 'Words'）
* 全局搜索 `FixedRecyclerItemInfo` 替换为 `FixedItemInfo`（记得勾选 'Match case' 和 'Words'）
* 全局搜索 `OnRecyclerLoadMoreListener` 替换为 `OnLoadMoreListener`
* 全局搜索 `import me.panpf.adapter.OnLoadMoreListener` 替换为 `import me.panpf.adapter.more.OnLoadMoreListener`
* 全局搜索 `onLoadMore(AssemblyRecyclerAdapter` 替换为 `onLoadMore(AssemblyAdapter`
* 全局搜索 `AssemblyLoadMoreRecyclerItemFactory` 替换为 `AssemblyLoadMoreItemFactory`
* 全局搜索 `import me.panpf.adapter.AssemblyLoadMoreItemFactory` 替换为 `import me.panpf.adapter.more.AssemblyLoadMoreItemFactory`
* 全局搜索 `import me.panpf.adapter.LoadMoreFixedItemInfo` 替换为 `import me.panpf.adapter.more.LoadMoreFixedItemInfo`
* 一一打开所有使用了 `OnRecyclerLoadMoreListener` 接口的类，导入 `AssemblyAdapter` 类
* 全局搜索 `.setLoadMoreEnd(` 替换为 `.loadMoreFinished(`
* 全局搜索 `import me.panpf.adapter.AssemblyFragmentStatePagerAdapter` 替换为 `import me.panpf.adapter.pager.AssemblyFragmentStatePagerAdapter`
* 全局搜索 `import me.panpf.adapter.AssemblyRecyclerLinerDivider` 替换为 `import me.panpf.adapter.recycler.AssemblyRecyclerLinerDivider`
* 全局搜索 `import me.panpf.adapter.AssemblyGroup` 替换为 `import me.panpf.adapter.expandable.AssemblyGroup`
* 全局搜索 `AssemblyGroupItemFactory` 替换为 `AssemblyItemFactory`（记得勾选 'Match case' 和 'Words'）
* 全局搜索 `AssemblyGroupItem` 替换为 `AssemblyItem`（记得勾选 'Match case' 和 'Words'）
* 全局搜索 `import me.panpf.adapter.AssemblyFragmentItemFactory` 替换为 `import me.panpf.adapter.pager.AssemblyFragmentItemFactory`（记得勾选 'Match case' 和 'Words'）
* 全局搜索 `import me.panpf.adapter.AssemblyPagerItemFactory` 替换为 `import me.panpf.adapter.pager.AssemblyPagerItemFactory`（记得勾选 'Match case' 和 'Words'）
* 全局搜索 `LoadMoreFixedRecyclerItemInfo` 替换为 `LoadMoreFixedItemInfo`（记得勾选 'Match case' 和 'Words'）
* 全局搜索 `import me.panpf.adapter.LoadMoreFixedItemInfo` 替换为 `import me.panpf.adapter.more.LoadMoreFixedItemInfo`（记得勾选 'Match case' 和 'Words'）
* 全局搜索 `import me.panpf.adapter.FragmentArrayPagerAdapter` 替换为 `import me.panpf.adapter.pager.FragmentArrayPagerAdapter`（记得勾选 'Match case' 和 'Words'）
* 全局搜索 `import me.panpf.adapter.AssemblyPagerAdapter` 替换为 `import me.panpf.adapter.pager.AssemblyPagerAdapter`（记得勾选 'Match case' 和 'Words'）
* 全局搜索 `import me.panpf.adapter.AssemblyFragmentPagerAdapter` 替换为 `import me.panpf.adapter.pager.AssemblyFragmentPagerAdapter`（记得勾选 'Match case' 和 'Words'）
* 全局搜索 `private AssemblyAdapter` 替换为 `private AssemblyListAdapter`（记得勾选 'Match case'）
* 全局搜索 `new AssemblyAdapter(` 替换为 `new AssemblyListAdapter(`（记得勾选 'Match case'）
