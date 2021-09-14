# new
* new: The set\*ClickListener series methods of ItemFactory, ExpandableChildItemFactory, PagerItemFactory add kotlin function overloading methods
* new: All Assembly\*Adapter add getItemFactoryByData(Any) and getItemFactoryByItemFactoryClass(KClass) methods
* new: ItemDataStorage.onDataListChanged function adds `oldList: List<DATA>` and `newList: List<DATA>` parameters
* improve: Improve the implementation of AssemblySingleData\*Adapter

# v4.0.0-rc04
* restore: Restore ArrayFragmentStateAdapter, ArrayPagerAdapter, ArrayFragmentStatePagerAdapter

# v4.0.0-rc03

recycler:
* fix: Fix the bug that the list size exceeds 1 but no exception is thrown when submitting data through submitList of AssemblySingleDataRecyclerListAdapter

pager2:
* fix: Fix the bug that the list size exceeds 1 but no exception is thrown when submitting data through submitList of AssemblySingleDataFragmentStateListAdapter

# v4.0.0-rc02

recycler:

* improve: [AssemblySingleDataRecyclerAdapter] now uses notifyItem\* method to update data
* change:
  [AssemblyRecyclerListAdapter] and [AssemblySingleDataRecyclerListAdapter] now do not support
  setHasStableIds and getItemId
* new: Added [IntactItemFactory]

pager:

* new: Assembly\*PagerAdapter adds getPageTitle support
* change: Added [IntactPagerItemFactory] and
  [IntactFragmentItemFactory] to replace ArrayPagerAdapter, ArrayFragmentStatePagerAdapter,
  ArrayFragmentStateAdapter

pager2:

* improve: AssemblySingleDataFragmentStateAdapter now uses notifyItem\* method to update data
* new:
  Added [FragmentStateListAdapter], [AssemblyFragmentStateListAdapter]
  , [AssemblySingleDataFragmentStateListAdapter]

# v4.0.0-rc01

list:

* change: ItemId move to common-core module
* change: hasStableIds changed from constructor attribute to setHasStableIds(Boolean) method
* change: AssemblyExpandableListAdapter, AssemblySingleDataExpandableListAdapter isChildSelectable
  rename to childSelectable
* new: ExpandableChildItemFactory added setOnChildViewClickListener,
  setOnChildViewLongClickListener, setOnChildItemClickListener, setOnChildItemLongClickListener
  method
* new: AssemblyListAdapter, AssemblySingleDataListAdapter, ConcatListAdapter added itemCount
  property

recycler:

* fix: Fix the bug that the disableByPosition, disableBySpanIndex, personaliseByPosition,
  personaliseBySpanIndex functions of Assembly\*DividerItemDecoration do not work
* new: Recycler related Adapter now rewrites getItemId() method to support obtaining stable itemId
  through ItemId interface or hashCode
* new: AssemblyRecyclerAdapter, AssemblyRecyclerListAdapter, AssemblySingleDataRecyclerAdapter,
  AssemblySingleDataRecyclerListAdapter added getItem(Int) method

recycler-paging:

* new: AssemblyPagingDataAdapter added currentList property

pager2-paging:

* new: AssemblyPagingDataAdapter added currentList property

pager:

* fix: Fix the bug that the value of the isDisableItemRefreshWhenDataSetChanged property of the
  refreshable PagerAdapter is wrong

all:

* new: Unified increase itemCount property and getItemData method

# v4.0.0-beta02

recycler:

* fix: Fix the bug of AbsoluteAdapterPosition error returned by ConcatAdapterAbsoluteHelper
* fix: ConcatAdapterLocalHelper now throws IndexOutOfBoundsException when position is out of range
* change: AnyAdapterDataObserver change to SimpleAdapterDataObserver
* change: Assembly\*RecyclerAdapter no longer rewrite the getItemId method

recycler-divider:

* fix: Fix the bug that the Insets.topAndBottomOf() method reverses start and top
* fix: Fix the bug that GridDividerItemDecoration encounters an item with a spanSize greater than 1
  and less than spanCount that its isLastSpan is calculated incorrectly
* change: The position priority of DividerConfig is now higher than spanIndex
* change: headerSide rename to sideHeader, footerSide rename to sideFooter

list:

* change: The expandable related classes in the list module are moved to the expandable directory
* new: Assembly\*ExpandableListAdapter added getItemFactoryByChildPosition method
* new: AssemblyListAdapter, AssemblySingleDataListAdapter, AssemblyExpandableListAdapter,
  AssemblySingleDataExpandableListAdapter now support hasStableIds

pager:

* fix: Fix the bug that \PagerAdapter trigger refresh even if the data is the same

all:

* change: When the position parameter of AssemblySingleData\Adapter related methods exceeds the
  range of 0 to count, an IndexOutOfBoundsException will be thrown.

# v4.0.0-beta01

全新版本，全新出发，4.0 版本 和 3.\* 版本完全不兼容，但两者可以共存

新版本使用方法请参考 [README.md](README.md)


[AssemblySingleDataRecyclerAdapter]: assemblyadapter-recycler/src/main/java/com/github/panpf/assemblyadapter/recycler/AssemblySingleDataRecyclerAdapter.kt

[AssemblyRecyclerListAdapter]: assemblyadapter-recycler/src/main/java/com/github/panpf/assemblyadapter/recycler/AssemblyRecyclerListAdapter.kt

[AssemblySingleDataRecyclerListAdapter]: assemblyadapter-recycler/src/main/java/com/github/panpf/assemblyadapter/recycler/AssemblySingleDataRecyclerListAdapter.kt

[IntactPagerItemFactory]: assemblyadapter-pager/src/main/java/com/github/panpf/assemblyadapter/pager/IntactPagerItemFactory.kt

[IntactFragmentItemFactory]: assemblyadapter-common-pager/src/main/java/com/github/panpf/assemblyadapter/pager/IntactFragmentItemFactory.kt

[IntactItemFactory]: assemblyadapter-common-item/src/main/java/com/github/panpf/assemblyadapter/IntactItemFactory.kt

[FragmentStateListAdapter]: assemblyadapter-pager2/src/main/java/com/github/panpf/assemblyadapter/pager2/FragmentStateListAdapter.kt

[AssemblyFragmentStateListAdapter]: assemblyadapter-pager2/src/main/java/com/github/panpf/assemblyadapter/pager2/AssemblyFragmentStateListAdapter.kt

[AssemblySingleDataFragmentStateListAdapter]: assemblyadapter-pager2/src/main/java/com/github/panpf/assemblyadapter/pager2/AssemblySingleDataFragmentStateListAdapter.kt