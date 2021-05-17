# AssemblyAdapter

![Platform][platform_image]
[![API][api_image]][api_link]
[![Release][release_icon]][release_link]
[![Android Arsenal][android_arsenal_icon]][android_arsenal_link]
[![License][license_image]][license_link]

[AssemblyAdapter] 是一个 Adapter 库，有了它你就不用再写 Adapter 了，支持组合式多类型 [Item]、支持添加 header 和 footer、支持加载更多

## 特性

* `Item 复用`. 你只需为 item layout 写一个 [Item] 和 [ItemFactory]，然后就可以到处使用了
* `组合式多类型 Item`. 可以组合式使用多种 [Item]，每种 [Item] 代表一种 item type
* `支持 header 和 footer`. 通过 AssemblyAdapter 可以让 [ExpandableListView]、[GridView]、[RecyclerView]、[ViewPager] 也支持 header 和 footer
* `支持加载更多`. 自带滑动到列表底部触发加载更多功能，你只需定义一个专门用于加载更多的 [Item] 即可
* `支持全部 Adapter`. 支持 [BaseAdapter]、[RecyclerView.Adapter]、[BaseExpandableListAdapter]、[PagerAdapter]、[FragmentPagerAdapter] 和 [FragmentStatePagerAdapter]
* `支持 spanSize 和 fullSpan`. API 层级支持 [GridLayoutManager] 的 spanSize 和 [StaggeredGridLayoutManager] 的 fullSpan，可轻松实现横跨多列功能
* `Paging`. 支持 [Paging]，[了解如何使用][paged_list]
* `无性能损耗`. 没有使用任何反射相关的技术，无须担心性能问题

## 使用指南

### 1. 从 mavenCentral 导入

```kotlin
dependencies {
    implementation("io.github.panpf.assemblyadapter:assemblyadapter:${lastVersionName}")
    implementation("io.github.panpf.assemblyadapter:assemblyadapter-ktx:${lastVersionName}") // Optional
    implementation("io.github.panpf.assemblyadapter:assemblyadapter-paging:${lastVersionName}") // Optional
}
```

`${lastVersionName}`：[![Release Version][release_icon]][release_link] (no include 'v')

### 2. 简介

共有 6 种 Adapter：

|Adapter|父类|适用于|支持功能|
|:---|:---|:---|:---|
|[AssemblyListAdapter]|[BaseAdapter]|[ListView]<br>[GridView]<br>[Spinner]<br>[Gallery]|多 Item<br>header 和 footer<br>加载更多|
|[AssemblyRecyclerAdapter]|[RecyclerView.Adapter]|[RecyclerView]|多 Item<br>header 和 footer<br>加载更多|
|[AssemblyExpandableAdapter]|[BaseExpandableListAdapter]|[ExpandableListView]|多 Item<br>header 和 footer<br>加载更多|
|[AssemblyPagerAdapter]|[PagerAdapter]|[ViewPager] + [View]|多 Item<br>header 和 footer|
|[AssemblyFragmentPagerAdapter]|[FragmentPagerAdapter]|[ViewPager] + [Fragment]|多 Item<br>header 和 footer|
|[AssemblyFragmentStatePagerAdapter]|[FragmentStatePagerAdapter]|[ViewPager] + [Fragment]|多 Item<br>header 和 footer|

[AssemblyAdapter] 共分为四部分：

* Adapter：负责维护数据、viewType 以及加载更多，只需使用提供的几种 Adapter 即可
* [Item]：负责创建 itemView、设置数据，每个 item layout 都要有单独的 [Item]
* [ItemFactory]：负责匹配数据类型、创建 [Item] 以及监听并处理点击事件，每个 [Item] 都要有单独的 [ItemFactory]
* [FixedItem]：当 [Item] 用于 header 或 footer 的时候会返回一个对应的 [FixedItem]，你可以通过 [FixedItem] 控制 [Item] 或更新其数据

AssemblyAdapter 与其它万能 Adapter 最根本的不同就是其把 item 相关的处理全部定义在了一对 [Item] 和 [ItemFactory] 类里面，在使用的时候只需通过 Adapter 的 addItemFactory(ItemFactory) 方法将 [ItemFactory] 加到 Adapter 中即可

这样的好处就是真正做到了一处定义处处使用，并且可以方便的在一个页面通过多次调用 addItemFactory(ItemFactory) 方法使用多个 [Item]（每个 [Item] 就代表一种 item type），这正体现了 AssemblyAdapter 名字中 Assembly 所表达的意思

`由于支持多 Item，一个 Adapter 只有一个数据列表，所以数据列表的泛型就只能是 Object`

### 3. 定义 Item 和 ItemFactory

继承 [AssemblyItem] 定义 Item，继承 [AssemblyItemFactory] 定义 Factory 如下：

```kotlin
data class User(val name: String, val sex: String, val age: String, val job: String)

class UserItem(parent: ViewGroup) : AssemblyItem<User>(R.layout.list_item_user, parent) {

    private val nameTextView: TextView by bindView(R.id.text_userListItem_name)
    private val sexTextView: TextView by bindView(R.id.text_userListItem_sex)
    private val ageTextView: TextView by bindView(R.id.text_userListItem_age)
    private val jobTextView: TextView by bindView(R.id.text_userListItem_job)

//    override fun onFindViews() {
//        nameTextView = findViewById(R.id.text_userListItem_name)
//        sexTextView = findViewById(R.id.text_userListItem_sex)
//        ageTextView = findViewById(R.id.text_userListItem_age)
//        jobTextView = findViewById(R.id.text_userListItem_job)
//    }

    override fun onConfigViews(context: Context) {
        /*
         * Configure view properties and listeners...
         */
        nameTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30)
        nameTextView.setOnClickListener {
            // ...
        }
    }

    override fun onSetData(position: Int, user: User?) {
        user ?: return
        nameTextView.text = user.name
        sexTextView.text = user.sex
        ageTextView.text = user.age
        jobTextView.text = user.job
    }

    class Factory : AssemblyItemFactory<User>() {

        override fun match(data: Any?) = data is User

        override fun createAssemblyItem(parent: ViewGroup) = UserItem(parent)
    }
}
```

自定义 Item 详解：
* 泛型用来指定对应的数据类型
* onFindViews(View) 方法用来 find View（如示例中的注掉的那部分所示）只会在创建的时候执行一次。但在 Kotlin 里你可以使用 bindView 扩展方法来初始化 View，这样就不需要 onFindViews(View) 方法了
* onConfigViews(Context) 方法用老初始化 View，设置属性或监听，只会在创建的时候执行一次
* onSetData() 方法用来设置数据，在每次 getView() 的时候都会执行
* 通过 getPosition() 和 getData() 方法可获取当前 item 的位置和数据，因此你在处理 click 的时候不再需要通过 setTag() 来绑定位置和数据了，直接获取即可
* 通过 getItemView() 方法可获取当前的 itemView
* 在 Item 中使用 bindView 请参考 [在 Kotlin 中使用 bindView][ktx]

自定义 Factory 详解：
* 泛型用来指定对应的数据类型
* match(Object) 方法用来匹配数据列表中的数据，返回 true 表示使用当前 Factory 处理这条数据
* createAssemblyItem(ViewGroup) 方法用来创建 Item
* 还可以在 Factory 中通过 setOnItemClickListener()、setOnViewClickListener() 方法监听点击事件

### 4. 使用  Item 和 ItemFactory

使用的时候只需通过 [AssemblyAdapter] 的 addItemFactory(ItemFactory) 方法将 [ItemFactory] 添加到 [AssemblyAdapter] 中即可，如下：

```kotlin

val dataList = ArrayList<Any>().apply {
    add(User("隔离老王", "男", 45, "隔壁少妇杀手"))
    add(Game("英雄联盟"))
    add(User("楼上老李", "男", 38, "楼下大妈终结者"))
    add(Game("守望先锋"))
}

val adapter = AssemblyRecyclerAdapter(dataList).apply {
    addItemFactory(UserItem.Factory())
    addItemFactory(GameItem.Factory())
}

val recyclerView: RecyclerView = ...
recyclerView.adapter = adapter
```

`GameItem.Factory` 的实现跟 `UserItem.Factory` 类似，这里就不写示例了

### 5. 更多高级功能

* [使用 header 和 footer][header_footer]
* [使用加载更多功能][load_more]
* [在 RecyclerView 的 GridLayoutManager 中一个 Item 独占一行或任意列][grid_span]
* [在 Kotlin 中使用 bindView][ktx]
* [支持 Paging][paged_list]

## 更新日志

Please view the [CHANGELOG.md] file

## License
    Copyright (C) 2017 Peng fei Pan <sky@panpf.me>

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[platform_image]: https://img.shields.io/badge/Platform-Android-brightgreen.svg
[android_arsenal_icon]: https://img.shields.io/badge/Android%20Arsenal-AssemblyAdapter-green.svg?style=true
[android_arsenal_link]: https://android-arsenal.com/details/1/4152
[api_image]: https://img.shields.io/badge/API-14%2B-orange.svg
[api_link]: https://android-arsenal.com/api?level=14
[release_icon]: https://img.shields.io/maven-central/v/io.github.panpf.assemblyadapter/assemblyadapter
[release_link]: https://repo1.maven.org/maven2/io/github/panpf/assemblyadapter/
[license_image]: https://img.shields.io/badge/License-Apache%202-blue.svg
[license_link]: https://www.apache.org/licenses/LICENSE-2.0

[CHANGELOG.md]: CHANGELOG.md

[header_footer]: docs/wiki/header_footer.md
[grid_span]: docs/wiki/grid_span.md
[load_more]: docs/wiki/load_more.md
[ktx]: docs/wiki/ktx.md
[paged_list]: docs/wiki/paged_list.md

[AssemblyAdapter]: assembly-adapter/src/main/java/me/panpf/adapter/AssemblyAdapter.java
[AssemblyListAdapter]: assembly-adapter/src/main/java/me/panpf/adapter/AssemblyListAdapter.java
[AssemblyRecyclerAdapter]: assembly-adapter/src/main/java/me/panpf/adapter/AssemblyRecyclerAdapter.java
[AssemblyItemFactory]: assembly-adapter/src/main/java/me/panpf/adapter/AssemblyItemFactory.java
[AssemblyItem]: assembly-adapter/src/main/java/me/panpf/adapter/AssemblyItem.java

[ItemFactory]: assembly-adapter/src/main/java/me/panpf/adapter/ItemFactory.java
[Item]: assembly-adapter/src/main/java/me/panpf/adapter/Item.java
[FixedItem]: assembly-adapter/src/main/java/me/panpf/adapter/FixedItem.java

[AssemblyExpandableAdapter]: assembly-adapter/src/main/java/me/panpf/adapter/AssemblyExpandableAdapter.java
[AssemblyGroup]: assembly-adapter/src/main/java/me/panpf/adapter/expandable/AssemblyGroup.java

[AssemblyMoreItemFactory]: assembly-adapter/src/main/java/me/panpf/adapter/more/AssemblyMoreItemFactory.java
[AssemblyMoreItem]: assembly-adapter/src/main/java/me/panpf/adapter/more/AssemblyMoreItem.java
[MoreFixedItem]: assembly-adapter/src/main/java/me/panpf/adapter/more/MoreFixedItem.java
[OnLoadMoreListener]: assembly-adapter/src/main/java/me/panpf/adapter/more/OnLoadMoreListener.java

[AssemblyPagerItemFactory]: assembly-adapter/src/main/java/me/panpf/adapter/pager/AssemblyPagerItemFactory.java
[FragmentFixedItem]: assembly-adapter/src/main/java/me/panpf/adapter/pager/FragmentFixedItem.java
[PagerFixedItem]: assembly-adapter/src/main/java/me/panpf/adapter/pager/PagerFixedItem.java
[AssemblyPagerAdapter]: assembly-adapter/src/main/java/me/panpf/adapter/pager/AssemblyPagerAdapter.java
[AssemblyFragmentPagerAdapter]: assembly-adapter/src/main/java/me/panpf/adapter/pager/AssemblyFragmentPagerAdapter.java
[AssemblyFragmentStatePagerAdapter]: assembly-adapter/src/main/java/me/panpf/adapter/pager/AssemblyFragmentStatePagerAdapter.java

[AssemblyRecyclerLinerDivider]: assembly-adapter/src/main/java/me/panpf/adapter/recycler/AssemblyRecyclerLinerDivider.java
[AssemblyGridLayoutManager]: assembly-adapter/src/main/java/me/panpf/adapter/recycler/AssemblyGridLayoutManager.java

[ViewItemFactory]: assembly-adapter/src/main/java/me/panpf/adapter/ViewItemFactory.java

[BaseAdapter]: https://developer.android.google.cn/reference/android/widget/BaseAdapter
[RecyclerView.Adapter]: https://developer.android.google.cn/reference/androidx/recyclerview/widget/RecyclerView.Adapter
[BaseExpandableListAdapter]: https://developer.android.google.cn/reference/android/widget/BaseExpandableListAdapter
[PagerAdapter]: https://developer.android.google.cn/reference/androidx/viewpager/widget/PagerAdapter
[FragmentPagerAdapter]: https://developer.android.google.cn/reference/androidx/fragment/app/FragmentPagerAdapter
[FragmentStatePagerAdapter]: https://developer.android.google.cn/reference/androidx/fragment/app/FragmentStatePagerAdapter
[GridLayoutManager]: https://developer.android.google.cn/reference/androidx/recyclerview/widget/GridLayoutManager
[StaggeredGridLayoutManager]: https://developer.android.google.cn/reference/androidx/recyclerview/widget/StaggeredGridLayoutManager
[ExpandableListView]: https://developer.android.google.cn/reference/android/widget/ExpandableListView
[GridView]: https://developer.android.google.cn/reference/android/widget/GridView
[ListView]: https://developer.android.google.cn/reference/android/widget/ListView
[Spinner]: https://developer.android.google.cn/reference/android/widget/Spinner
[Gallery]: https://developer.android.google.cn/reference/android/widget/Gallery
[RecyclerView]: https://developer.android.google.cn/reference/androidx/recyclerview/widget/RecyclerView
[ViewPager]: https://developer.android.google.cn/reference/androidx/viewpager/widget/ViewPager
[View]: https://developer.android.google.cn/reference/android/view/View
[Fragment]: https://developer.android.google.cn/reference/androidx/fragment/app/Fragment
[Paging]: https://developer.android.google.cn/topic/libraries/architecture/paging/
