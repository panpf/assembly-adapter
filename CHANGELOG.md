# new

list:
* new: ExpandableChildItemFactory added setOnChildViewClickListener, setOnChildViewLongClickListener, setOnChildItemClickListener, setOnChildItemLongClickListener method
* change: ItemId move to common-core module

# v4.0.0-beta02

recycler:
* fix: Fix the bug of AbsoluteAdapterPosition error returned by ConcatAdapterAbsoluteHelper 
* fix: ConcatAdapterLocalHelper now throws IndexOutOfBoundsException when position is out of range
* change: AnyAdapterDataObserver change to SimpleAdapterDataObserver
* change: Assembly\*RecyclerAdapter no longer rewrite the getItemId method
  
recycler-divider:
* fix: Fix the bug that the Insets.topAndBottomOf() method reverses start and top
* fix: Fix the bug that GridDividerItemDecoration encounters an item with a spanSize greater than 1 and less than spanCount that its isLastSpan is calculated incorrectly
* change: The position priority of DividerConfig is now higher than spanIndex
* change: headerSide rename to sideHeader, footerSide rename to sideFooter

list:
* change: The expandable related classes in the list module are moved to the expandable directory
* new: Assembly\*ExpandableListAdapter added getItemFactoryByChildPosition method
* new: AssemblyListAdapter, AssemblySingleDataListAdapter, AssemblyExpandableListAdapter, AssemblySingleDataExpandableListAdapter now support hasStableIds

pager:
* fix: Fix the bug that \PagerAdapter trigger refresh even if the data is the same

all:
* change: When the position parameter of AssemblySingleData\Adapter related methods exceeds the range of 0 to count, an IndexOutOfBoundsException will be thrown.


# v4.0.0-beta01

全新版本，全新出发，4.0 版本 和 3.\* 版本完全不兼容，但两者可以共存

新版本使用方法请参考 [README.md](README.md)

同时提供了使用 4.0 API 实现的兼容 3.\* API 的一些类，请参考 [使用新版 4.* API 兼容旧版 3.* API](docs/wiki/old_api_compat.md)