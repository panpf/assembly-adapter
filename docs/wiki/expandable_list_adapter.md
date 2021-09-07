# BaseExpandableListAdapter 支持

AssemblyAdapter 也为 [BaseExpandableListAdapter] 提供了支持，如下：

* [AssemblyExpandableListAdapter]：多类型 Adapter 实现
* [AssemblySingleDataExpandableListAdapter]：单数据实现
* [ConcatExpandableListAdapter]：连接 Adapter 实现

### 注意

1. group 数据必须要实现 [ExpandableGroup] 接口才能正确的获取 child count 和 child
2. 如果想要获取 [BaseExpandableListAdapter] 独有的 isExpanded、groupPosition、childPosition、isLastChild
   数据请使用 [ExpandableGroupItemFactory] 和 [ExpandableChildItemFactory]
3. [ExpandableGroupItemFactory] 和 [ExpandableChildItemFactory] 也有 Binding、Simple、View 版本

### 更多示例

* [AppChildItemFactory]
* [AppGroupItemFactory]
* [AppGroupPlaceholderItemFactory]
* [ExpandableListFragment]
* [ExpandableListPlaceholderFragment]

[AppChildItemFactory]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/item/AppChildItemFactory.kt

[AppGroupItemFactory]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/item/AppGroupItemFactory.kt

[AppGroupPlaceholderItemFactory]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/item/AppGroupPlaceholderItemFactory.kt

[ExpandableGroup]: ../../assemblyadapter-list/src/main/java/com/github/panpf/assemblyadapter/list/expandable/ExpandableGroup.kt

[ExpandableGroupItemFactory]: ../../assemblyadapter-list/src/main/java/com/github/panpf/assemblyadapter/list/expandable/ExpandableGroupItemFactory.kt

[ExpandableChildItemFactory]: ../../assemblyadapter-list/src/main/java/com/github/panpf/assemblyadapter/list/expandable/ExpandableChildItemFactory.kt

[AssemblyExpandableListAdapter]: ../../assemblyadapter-list/src/main/java/com/github/panpf/assemblyadapter/list/expandable/AssemblyExpandableListAdapter.kt

[AssemblySingleDataExpandableListAdapter]: ../../assemblyadapter-list/src/main/java/com/github/panpf/assemblyadapter/list/expandable/AssemblySingleDataExpandableListAdapter.kt

[ConcatExpandableListAdapter]: ../../assemblyadapter-list/src/main/java/com/github/panpf/assemblyadapter/list/expandable/ConcatExpandableListAdapter.kt

[ExpandableListFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/list/ExpandableListFragment.kt

[ExpandableListPlaceholderFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/list/ExpandableListPlaceholderFragment.kt

[BaseExpandableListAdapter]: https://developer.android.google.cn/reference/android/widget/BaseExpandableListAdapter