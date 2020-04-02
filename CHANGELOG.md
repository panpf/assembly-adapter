# v...
* upgrade: Fragment upgraded to 1.2.0
* upgrade: RecyclerView upgraded to 1.1.0
* upgrade: Paging upgraded to 2.1.1

# v3.4.0

assembly-paged-list-adapter:
* :fire: AssemblyPagedListAdapter removes no-argument constructor
* :fire: Remove ObjectDiffCallback
* :sparkles: Add DiffableDiffCallback
* :sparkles: Support for creating ItemHolder first, then adding ItemHolder to AssemblyAdapter

# v3.4.0-beta1

assembly-paged-list-adapter:
* :fire: AssemblyPagedListAdapter removes no-argument constructor
* :fire: Remove ObjectDiffCallback
* :sparkles: Add DiffableDiffCallback
* :sparkles: Support for creating ItemHolder first, then adding ItemHolder to AssemblyAdapter

# v3.3.0

* :arrow_up: 升级编译版本和目标版本到 api 28
* :arrow_up: 升级到 Jetpack


# v3.2.1

新功能：
* 新增了 [ArrayPagerAdapter] 和 [FragmentArrayStatePagerAdapter]
* 所有 PagerAdapter 的子类支持调用 notifyDataSetChanged() 方法时 getItemPosition(Object) 方法返回 POSITION_NONE，只需通过 setEnabledPositionNoneOnNotifyDataSetChanged(boolean) 方法开启即可

[ArrayPagerAdapter]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/pager/ArrayPagerAdapter.java
[FragmentArrayStatePagerAdapter]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/pager/FragmentArrayStatePagerAdapter.java


# v3.1.0

新功能：
* 支持 [Paging]，[了解如何使用][paged_list]

[paged_list]: https://github.com/panpf/assembly-adapter/blob/master/docs/wiki/paged_list.md
[Paging]: https://developer.android.google.cn/topic/libraries/architecture/paging/


# v3.0.0

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
* 新增通用的 view [ItemFactory]，可快速在列表中显示一个 view 或布局，省去自定义 [ItemFactory] 的成本，详情请参见 [使用 ViewItemFactory][view_item_factory]
* [ItemFactory] 和 [AssemblyPagerItemFactory] 新增四种点击监听设置方法，可对 item 或某个 view 的点击设置监听，省去在 [ItemFactory] 中定义监听并执行回调的成本

### 2.2 RecyclerView
* 新增 [AssemblyRecyclerLinerDivider] 可定制头和尾巴不显示分割线
* 新增 [AssemblyGridLayoutManager] 类搭配 [ItemFactory] 的 setSpanSize(int) 和 fullSpan() 方法方便的控制占多少列，详情请参见 [在 RecyclerView 的 GridLayoutManager 中一个 Item 独占一行或任意列][grid_span]

#### 2.3 Kotlin 支持
* 新增 assembly-adapter-ktx 模块，提供了 [Item] 的 bindview 功能，详情请参见 [在 Kotlin 中使用 bindview][ktx]

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

[header_footer]: https://github.com/panpf/assembly-adapter/blob/master/docs/wiki/header_footer.md
[grid_span]: https://github.com/panpf/assembly-adapter/blob/master/docs/wiki/grid_span.md
[load_more]: https://github.com/panpf/assembly-adapter/blob/master/docs/wiki/load_more.md
[ktx]: https://github.com/panpf/assembly-adapter/blob/master/docs/wiki/ktx.md
[view_item_factory]: https://github.com/panpf/assembly-adapter/blob/master/docs/wiki/view_item_factory.md


# v2.5.1

:bug: 修复上传到 jcenter 的包里有重复垃圾代码的问题


# v2.5.0

重构：
:hammer: 包名改为 me.panpf.adapter


# v2.4.0

重构：
:hammer: 包名改为 me.panpf.assemblyadapter


# v2.3.2

优化：
>* 支持在 Kotlin 中使用 [Kotterknife]
>* 所有 AssemblyItem 的 onFindViews() 方法现在不是抽象的了

[Kotterknife]: https://github.com/JakeWharton/kotterknife


# v2.3.1

优化：
>* 允许itemFactoryList为空，不再会有 You need to configure AssemblyItemFactory use addItemFactory method 异常


# v2.3.0

BUG：
>* 修复AssemblyLoadMoreItem的getErrorRetryView()方法返回null时崩溃的BUG

新增：
>* AssemblyRecyclerItemFactory新增getSpanSize()方法，AssemblyRecyclerAdapter新增getSpanSize(int position)方法，配合GridLayoutManager.SpanSizeLookup可让Item横跨多列
>* AssemblyRecyclerItem新增fullSpan(RecyclerView)方法，可让Item在GridLayoutManager或StaggeredGridLayoutManager里独占一行
>* 支持加载更多的Adapter新增loadMoreFinished(boolean)方法，用来替代setLoadMoreEnd(boolean)方法

删除：
>* 删除ContentSetter


# v2.2.0

Adapter：
>* ``新增``. AssemblyAdapter、AssemblyRecyclerAdapter、AssemblyExpandableAdapter、AssemblyPagerAdapter等Adapter增加setNotifyOnChange(boolean)方法，可控制数据改变时是否立即刷新列表，默认为true。当你需要连续多次修改数据的时候，你应该将notifyOnChange设为false，然后在最后主动调用notifyDataSetChanged()刷新列表，最后再将notifyOnChange设为true
>* ``新增``. AssemblyPagerAdapter新增addHeaderItem()和addFooterItem()方法返回FixedPagerItemInfo，现在你可以控制头或者尾巴了
>* ``新增``. AssemblyPagerAdapter新增add()、remove()、clear()、setDataList()等方法，现在你可以更方便的控制数据了

header、footer：
>* ``新增``. FixedItemInfo、FixedRecyclerItemInfo、FixedGroupItemInfo、FixedPagerItemInfo等增加setEnabled(boolean)方法，现在你可以通过这个方法直接控制隐藏或显示header、footer


# v2.1.1

Adapter：
>* public AssemblyPagerAdapter(FragmentManager fm, Object[] dataArray) 去掉FragmentManager
>* 修复AssemblyFragmentStatePagerAdapter、AssemblyFragmentPagerAdapter、AssemblyPagerAdapter这三个类的addItemFactory()方法检查出异常时没有return的BUG

ItemFactory：
>* AssemblyFragmentItemFactory、AssemblyPagerItemFactory增加setAdapter(PagerAdapter)和getAdapter()方法
>* 所有ItemFactory的setAdapter方法的访问权限改为包级

FixedItemInfo：
>* 所有FixedItemInfo的setData(Object)方法增加刷新数据功能


# v2.1.0

Adapter：
>* ``新增``. 支持header和footer，如下:
    ```java
    AssemblyAdapter adapter = new AssemblyAdapter(objects);
    adapter.addHeaderItem(new LikeFooterItemFactory(), "我是小额头呀！");
    adapter.addItemFactory(new UserListItemFactory(getBaseContext()));
    adapter.addItemFactory(new GameListItemFactory(getBaseContext()));
    adapter.addFooterItem(new LikeFooterItemFactory(), "我是小尾巴呀！");
    adapter.setLoadMoreItem(new LoadMoreItemFactory(ListViewFragment.this));
    listView.setAdapter(adapter);
    ```
>* ``新增``. 由于支持header和footer因此不得不增加一个getPositionInPart(int position)方法用于获取position在其各自区域的位置
>* ``新增``. 新增AssemblyPagerAdapter、AssemblyFragmentPagerAdapter、AssemblyFragmentStatePagerAdapter以及FragmentArrayPagerAdapter用于支持ViewPager
>* ``修改``. enableLoadMore()方法改名为setLoadMoreItem()
>* ``修改``. setEnableLoadMore(boolean)方法去掉了，新增了一个setDisableLoadMore(boolean)用来关闭或开启加载更多功能
>* ``修改``. 不定长构造函数(Object... dataArray)改为(Object[] dataArray)

ItemFactory：
>* ``修改``. AbstractLoadMoreListItemFactory重命名为AssemblyLoadMoreItemFactory
>* ``修改``. AbstractLoadMoreRecyclerItemFactory重命名为AssemblyLoadMoreRecyclerItemFactory
>* ``修改``. AbstractLoadMoreGroupItemFactory重命名为AssemblyLoadMoreGroupItemFactory

Item：
>* ``新增``. 新增ContentSetter，可以更方便的设置各种内容，如下
    ```java
    @Override
    protected void onSetData(int position, Game game) {
        getSetter()
                .setImageResource(R.id.image_gameListItem_icon, game.iconResId)
                .setText(R.id.text_gameListItem_name, game.name)
                .setText(R.id.text_gameListItem_like, game.like);
    }
    ```
>* ``修改``. AssemblyGroupItem的onSetData(int groupPosition, boolean isExpanded, BEAN bean)方法的参数改为onSetData(int position, BEAN bean)
>* ``修改``. AssemblyGroupItem的getGroupPosition()方法名改为getPosition()
>* ``修改``. AssemblyChildItem的onSetData(int groupPosition, int childPosition, boolean isLastChild, BEAN bean)方法的参数改为onSetData(int position, BEAN bean)
>* ``修改``. AssemblyChildItem的getChildPosition()方法名改为getPosition()


# v2.0.0

Adapter：
>* 新增setDataList(List)方法，方便直接替换数据
>* 新增addAll(List), insert(int, Object), remove(Object), sort(Comparator)方法，去掉append(List)方法
>* loadMoreEnd()方法改为setLoadMoreEnd(true)，方便在不禁用的情况下恢复加载更多

Item：
>* AssemblyItem不再需要传入ItemFactor，因此去掉了getItemFactory()方法，现在你需要访问AssemblyItemFactory中的字段的话，只需把Item设为非静态的即可
>* 去掉了AssemblyItem的第二个泛型，因为不再需要ItemFactory了
>* 重构了AssemblyItem的构造函数，现在有AssemblyItem(int, ViewGroup)和AssemblyItem(View)两种，使用更加方便
>* AssemblyItem中的成员变量都改成private的了，你只能通过其get方法来获取
>* AssemblyItem新增了findViewById(int)和findViewWithTag(Object)方法
>* AssemblyItem.onFindViews(View)方法的View参数去掉了


# v1.1.0

更新日志如下：
>* 构造函数增加数组支持
>* ItemFactory增加getAdapter()方法
>* ItemFactory中新增isTarget(Object)方法替代isAssignableFrom(Object)
>* 加载更多尾巴支持显示THE END

使用1.10版本需要你对已有的代码做出修改：
>* AssembleItem的getBeanClass()方法已经删除，新增了isTarget()方法，可参考首页readme中的示例


# v1.0.0

first release
