# AssemblyAdapter

![Platform][platform_image]
[![API][api_image]][api_link]
[![Release][release_icon]][release_link]
[![License][license_image]][license_link]

AssemblyAdapter 是 Android 上的一个为各种 Adapter 提供开箱即用实现的库。

## 特性

* `Item 复用`. 只需为你的 item 写一个 [ItemFactory]，然后就可以到处使用了. [详情][docs_item_factory]
* `支持多类型`. 只需给 [AssemblyAdapter] 添加多个 [ItemFactory] 即可轻松实现多类型 Adapter. [详情][docs_multi_type_adapter]
* `支持全部 Adapter`. 支持 [BaseAdapter]、[BaseExpandableListAdapter]、[RecyclerView.Adapter]、[ListAdapter]、[PagingDataAdapter]、[PagerAdapter]、[FragmentStatePagerAdapter]、[FragmentStateAdapter] 等全部常用 Adapter
* `为更多 Adapter 提供 Concat 支持`. 提供了 [ConcatListAdapter]、[ConcatExpandableListAdapter]、[ConcatPagerAdapter]、[ConcatFragmentStatePagerAdapter] 为更多的 Adapter 提供 Concat 支持. [详情][docs_concat_adapter]
* `支持 Paging 3.0`. 提供了 [AssemblyPagingDataAdapter] 和 [AssemblyPagingDataFragmentStateAdapter] 来支持 Paging 3.0. [详情][docs_paging3]
* `支持 ViewPager2`. 提供了 [AssemblyFragmentStateAdapter] 来支持 ViewPager2. [详情][docs_pager2]
* `支持 spanSize 和 fullSpan`. 提供了 [AssemblyGridLayoutManager] 和 [AssemblyStaggeredGridLayoutManager] 可以轻松的实现横跨多列功能. [详情][docs_grid_span]
* `提供 divider 支持`. [assemblyadapter-common-recycler-divider] 模块提供了一套强大的 DividerItemDecoration 可以轻松实现炫酷的 divider. [详情][docs_recycler_divider]

## 支持的 Adapter

[AssemblyAdapter] 只是一个接口，不可以直接使用，你需要针对不同的 Adapter 使用具体的实现类，如下表格所示：

* [assemblyadapter-list]:
    * [BaseAdapter]
        * [AssemblyListAdapter]: 多类型 Adapter 实现
        * [AssemblySingleDataListAdapter]：单数据实现
        * [ConcatListAdapter]: 连接 Adapter 实现
    * [BaseExpandableListAdapter]
        * [AssemblyExpandableListAdapter]: 多类型 Adapter 实现
        * [AssemblySingleDataExpandableListAdapter]：单数据实现
        * [ConcatExpandableListAdapter]: 连接 Adapter 实现
* [assemblyadapter-pager]:
    * [PagerAdapter]
        * [AssemblyPagerAdapter]: 多类型 Adapter 实现
        * [AssemblySingleDataPagerAdapter]：单数据实现
        * [ConcatPagerAdapter]: 连接 Adapter 实现
        * [ArrayPagerAdapter]: View 数组实现
    * [FragmentStatePagerAdapter]:
        * [AssemblyFragmentStatePagerAdapter]: 多类型 Adapter 实现
        * [AssemblySingleDataFragmentStatePagerAdapter]：单数据实现
        * [ConcatFragmentStatePagerAdapter]: 连接 Adapter 实现
        * [ArrayFragmentStatePagerAdapter]: Fragment 数组实现
* [assemblyadapter-pager2]:
    * [FragmentStateAdapter]
        * [AssemblyFragmentStateAdapter]: 多类型 Adapter 实现
        * [AssemblySingleDataFragmentStateAdapter]：单数据实现
        * [ArrayFragmentStateAdapter]: Fragment 数组实现
* [assemblyadapter-pager2-paging]:
    * [PagingDataFragmentStateAdapter]
        * [AssemblyPagingDataFragmentStateAdapter]: 多类型 Adapter 实现
    * [LoadStateFragmentStateAdapter]
        * [AssemblyLoadStateFragmentStateAdapter]: Paging 加载状态 Adapter 实现
* [assemblyadapter-recycler]:
    * [RecyclerView.Adapter]
        * [AssemblyRecyclerAdapter]: 多类型 Adapter 实现
        * [AssemblySingleDataRecyclerAdapter]：单数据实现
    * [ListAdapter]
        * [AssemblyRecyclerListAdapter]: 多类型 Adapter 实现
* [assemblyadapter-recycler-paging]:
    * [PagingDataAdapter]
        * [AssemblyPagingDataAdapter]: 多类型 Adapter 实现
    * [LoadStateAdapter]
        * [AssemblyLoadStateAdapter]: Paging 加载状态 Adapter 实现

## 导入

### 1. 从 mavenCentral 导入

你可以直接导入所有模块，如下：
```kotlin
dependencies {
    implementation("io.github.panpf.assemblyadapter4:assemblyadapter:${LAST_VERSION}")
}
```

你还可以按需导入所需模块，如下：
```kotlin
dependencies {
    implementation("io.github.panpf.assemblyadapter4:assemblyadapter-list:${LAST_VERSION}")
    implementation("io.github.panpf.assemblyadapter4:assemblyadapter-pager:${LAST_VERSION}")
    implementation("io.github.panpf.assemblyadapter4:assemblyadapter-pager2:${LAST_VERSION}")
    implementation("io.github.panpf.assemblyadapter4:assemblyadapter-pager2-paging:${LAST_VERSION}")
    implementation("io.github.panpf.assemblyadapter4:assemblyadapter-recycler:${LAST_VERSION}")
    implementation("io.github.panpf.assemblyadapter4:assemblyadapter-recycler-paging:${LAST_VERSION}")
}
```
*每个模块包含哪些 Adapter，可以参考前面的表格*

`${LAST_VERSION}`：[![Release Version][release_icon]][release_link] (no include 'v')

## 使用指南

### 1. 定义 ItemFactory

Adapter 负责管理数据和为数据匹配 [ItemFactory]，[ItemFactory] 负责创建 item 的 view 以及绑定数据。

通常不建议直接继承 [ItemFactory] 来定义自己的 [ItemFactory]，因为实现 [ItemFactory] 需要再额外定义一个 [Item]，这样写起来会稍显繁琐。库中提供了几种简化版的不用定义 [Item] 的子类来简化定义的 [ItemFactory] 流程，如下：
* [SimpleItemFactory]：只需实现 createItemView() 方法创建 item view 以及实现 initItem()、bindItemData() 方法来初始化 item 和绑定数据即可
* [BindingItemFactory]：支持 ViewBinding. 只需实现 createItemViewBinding() 方法创建 ViewBinding 以及实现 initItem()、bindItemData() 方法来初始化 item 和绑定数据即可
* [ViewItemFactory]：外部提供创建好的 item view 或者布局 id，即可直接使用

下面演示继承 [BindingItemFactory] 来创建我们的 [ItemFactory]

item 布局定义如下 (item_app_info.xml)：
```xml
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">
    <TextView
        android:id="@+id/appItemNameText"
        />
    <TextView
        android:id="@+id/appItemVersionText"
        />
    <TextView
        android:id="@+id/appItemSizeText"
        />
</androidx.constraintlayout.widget.ConstraintLayout>
```

数据类定义如下：
```kotlin
data class AppInfo(
    val name: String,
    val packageName: String,
    val versionName: String,
    val apkSize: Long,
)
```

```kotlin
class AppInfoItemFactory : BindingItemFactory<AppInfo, ItemAppInfoBinding>(AppInfo::class) {

    override fun createItemViewBinding(
        context: Context, inflater: LayoutInflater, parent: ViewGroup
    ): ItemAppInfoBinding {
        /*
         * 在此处创建 ViewBinding。这个方法只执行一次
         */
        return ItemAppInfoBinding.inflate(inflater, parent, false)
    }

    override fun initItem(
        context: Context, binding: ItemAppInfoBinding, item: BindingItem<AppInfo, ItemAppBinding>
    ) {
        /*
         * 您可以在此处初始化 item 并绑定 click 事件。这个方法只执行一次
         */
        binding.root.setOnClickListener {
            // 事件发生时从 item 获取 position 和 数据
            val data: AppInfo = item.dataOrThrow
            val bindingAdapterPosition: Int = item.bindingAdapterPosition
            val absoluteAdapterPosition: Int = item.absoluteAdapterPosition
            val launchIntent =
                context.packageManager.getLaunchIntentForPackage(data.packageName)
            if (launchIntent != null) {
                context.startActivity(launchIntent)
            }
        }
    }

    override fun bindItemData(
        context: Context,
        binding: ItemAppInfoBinding,
        item: BindingItem<AppInfo, ItemAppInfoBinding>,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: AppInfo
    ) {
        /*
         * 在此处绑定项 item view 的数据。这个方法会经常执行
         */
        binding.appItemNameText.text = data.name
        binding.appItemVersionText.text = "v${data.versionName}"
        binding.appItemSizeText.text = Formatter.formatFileSize(context, data.apkSize)
    }
}
```

更多自定义 [ItemFactory] 详细内容请参考 [ItemFactory 自定义详解][docs_item_factory]

### 2. 使用 ItemFactory 创建 Adapter

只需在创建 Adapter 时通过构造参数传入 ItemFactory 即可，如下：

```kotlin
val appAdapter = AssemblyRecyclerAdapter(
    listOf(AppInfoItemFactory())
)

appAdapter.submitDataList(listOf(
    AppInfo("AirPortal", "cn.airportal", "4.21", 1258291L),
    AppInfo("Apex Legends Mobile", "com.ea.gp.apex", "1.2", 100258291L),
    AppInfo("APKPure", "com.apkpure.aegon", "3.17.23", 157879798L),
    AppInfo("Block Earth", "com.craft.earth", "2.42", 57879798L),
    AppInfo("Bluestack", "app.bluestack", "1.0.0", 41534523L),
    AppInfo("Craft Pixel Art Rain", "com.lucky.fairy", "15", 4247204L),
    AppInfo("Cutting Edge!", "com.cuttingedge", "0.16", 4289472412L),
    AppInfo("Cyber Knights", "com..cyberknightselite", "2.9.4", 6174924L),
    AppInfo("Guardians", "com.emagroups.cs", "1.2.3", 7782423L),
))

RecyclerView(activity).adapter = appAdapter
```

### 3. 更多功能

* [自定义 ItemFactory][docs_item_factory]
* [实现多类型 Adapter][docs_multi_type_adapter]
* [体验为 BaseAdapter 或 PagerAdapter 提供的 Concat*Adapter][docs_single_data_adapter]
* [通过 ConcatAdapter 实现 header 和 footer][docs_header_and_footer]
* [给 RecyclerView 配置 divider][docs_recycler_divider]
* [使用 GridLayoutManager 或 StaggeredGridLayoutManager 时配置 Item 横跨多列][docs_grid_span]
* [Paging 3.0 支持][docs_paging3]
* [Pager2 支持][docs_pager2]
* [AssemblyExpandableListAdapter 使用详解][docs_expandable_list_adapter]
* [通过 AssemblySingleData*Adapter 实现只有一条数据的 Adapter][docs_single_data_adapter]
* [使用新版 4.* API 兼容旧版 3.* API][docs_old_api_compat]

## 更新日志

Please view the [CHANGELOG.md] file

## License
    Copyright (C) 2021 panpf <panpfpanpf@outlook.com>

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
[api_image]: https://img.shields.io/badge/API-16%2B-orange.svg
[api_link]: https://android-arsenal.com/api?level=16
[release_icon]: https://img.shields.io/maven-central/v/io.github.panpf.assemblyadapter4/assemblyadapter
[release_link]: https://repo1.maven.org/maven2/io/github/panpf/assemblyadapter4/
[license_image]: https://img.shields.io/badge/License-Apache%202-blue.svg
[license_link]: https://www.apache.org/licenses/LICENSE-2.0

[CHANGELOG.md]: CHANGELOG.md

[assemblyadapter-common-recycler-divider]: assemblyadapter-common-recycler-divider
[assemblyadapter-list]: assemblyadapter-list
[assemblyadapter-pager]: assemblyadapter-pager
[assemblyadapter-pager2]: assemblyadapter-pager2
[assemblyadapter-pager2-paging]: assemblyadapter-pager2-paging
[assemblyadapter-recycler]: assemblyadapter-recycler
[assemblyadapter-recycler-paging]: assemblyadapter-recycler-paging

[docs_expandable_list_adapter]: docs/wiki/expandable_list_adapter.md
[docs_grid_span]: docs/wiki/grid_span.md
[docs_paging3]: ../../raw/master/docs/wiki/paging.md
[docs_item_factory]: docs/wiki/item_factory.md
[docs_single_data_adapter]: docs/wiki/single_data_adapter.md
[docs_old_api_compat]: docs/wiki/old_api_compat.md
[docs_multi_type_adapter]: docs/wiki/multi_type_adapter.md
[docs_concat_adapter]: docs/wiki/concat_adapter.md
[docs_pager2]: docs/wiki/pager2.md
[docs_recycler_divider]: docs/wiki/recycler_divider.md
[docs_header_and_footer]: docs/wiki/header_and_footer.md

[AssemblyAdapter]: assemblyadapter-common-core/src/main/java/com/github/panpf/assemblyadapter/AssemblyAdapter.kt

[ItemFactory]: assemblyadapter-common-item/src/main/java/com/github/panpf/assemblyadapter/ItemFactory.kt
[Item]: assemblyadapter-common-item/src/main/java/com/github/panpf/assemblyadapter/Item.kt
[SimpleItemFactory]: assemblyadapter-common-item/src/main/java/com/github/panpf/assemblyadapter/SimpleItemFactory.kt
[BindingItemFactory]: assemblyadapter-common-item/src/main/java/com/github/panpf/assemblyadapter/BindingItemFactory.kt
[ViewItemFactory]: assemblyadapter-common-item/src/main/java/com/github/panpf/assemblyadapter/ViewItemFactory.kt

[AssemblyListAdapter]: assemblyadapter-list/src/main/java/com/github/panpf/assemblyadapter/list/AssemblyListAdapter.kt
[AssemblyExpandableListAdapter]: assemblyadapter-list/src/main/java/com/github/panpf/assemblyadapter/list/AssemblyExpandableListAdapter.kt
[AssemblySingleDataListAdapter]: assemblyadapter-list/src/main/java/com/github/panpf/assemblyadapter/list/AssemblySingleDataListAdapter.kt
[AssemblySingleDataExpandableListAdapter]: assemblyadapter-list/src/main/java/com/github/panpf/assemblyadapter/list/AssemblySingleDataExpandableListAdapter.kt
[ConcatListAdapter]: assemblyadapter-list/src/main/java/com/github/panpf/assemblyadapter/list/ConcatListAdapter.kt
[ConcatExpandableListAdapter]: assemblyadapter-list/src/main/java/com/github/panpf/assemblyadapter/list/ConcatExpandableListAdapter.kt

[AssemblyRecyclerAdapter]: assemblyadapter-recycler/src/main/java/com/github/panpf/assemblyadapter/recycler/AssemblyRecyclerAdapter.kt
[AssemblyRecyclerListAdapter]: assemblyadapter-recycler/src/main/java/com/github/panpf/assemblyadapter/recycler/AssemblyRecyclerListAdapter.kt
[AssemblySingleDataRecyclerAdapter]: assemblyadapter-recycler/src/main/java/com/github/panpf/assemblyadapter/recycler/AssemblySingleDataRecyclerAdapter.kt
[AssemblyGridLayoutManager]: assemblyadapter-recycler/src/main/java/com/github/panpf/assemblyadapter/recycler/AssemblyGridLayoutManager.kt
[AssemblyStaggeredGridLayoutManager]: assemblyadapter-recycler/src/main/java/com/github/panpf/assemblyadapter/recycler/AssemblyStaggeredGridLayoutManager.kt

[AssemblyPagingDataAdapter]: assemblyadapter-recycler-paging/src/main/java/com/github/panpf/assemblyadapter/recycler/paging/AssemblyPagingDataAdapter.kt
[AssemblyLoadStateAdapter]: assemblyadapter-recycler-paging/src/main/java/com/github/panpf/assemblyadapter/recycler/paging/AssemblyLoadStateAdapter.kt

[ArrayPagerAdapter]: assemblyadapter-pager/src/main/java/com/github/panpf/assemblyadapter/pager/ArrayPagerAdapter.kt
[ArrayFragmentStatePagerAdapter]: assemblyadapter-pager/src/main/java/com/github/panpf/assemblyadapter/pager/ArrayFragmentStatePagerAdapter.kt
[AssemblyPagerAdapter]: assemblyadapter-pager/src/main/java/com/github/panpf/assemblyadapter/pager/AssemblyPagerAdapter.kt
[AssemblyFragmentStatePagerAdapter]: assemblyadapter-pager/src/main/java/com/github/panpf/assemblyadapter/pager/AssemblyFragmentStatePagerAdapter.kt
[AssemblySingleDataPagerAdapter]: assemblyadapter-pager/src/main/java/com/github/panpf/assemblyadapter/pager/AssemblySingleDataPagerAdapter.kt
[AssemblySingleDataFragmentStatePagerAdapter]: assemblyadapter-pager/src/main/java/com/github/panpf/assemblyadapter/pager/AssemblySingleDataFragmentStatePagerAdapter.kt
[ConcatPagerAdapter]: assemblyadapter-pager/src/main/java/com/github/panpf/assemblyadapter/pager/ConcatPagerAdapter.kt
[ConcatFragmentStatePagerAdapter]: assemblyadapter-pager/src/main/java/com/github/panpf/assemblyadapter/pager/ConcatFragmentStatePagerAdapter.kt

[ArrayFragmentStateAdapter]: assemblyadapter-pager2/src/main/java/com/github/panpf/assemblyadapter/pager2/ArrayFragmentStateAdapter.kt
[AssemblyFragmentStateAdapter]: assemblyadapter-pager2/src/main/java/com/github/panpf/assemblyadapter/pager2/AssemblyFragmentStateAdapter.kt
[AssemblySingleDataFragmentStateAdapter]: assemblyadapter-pager2/src/main/java/com/github/panpf/assemblyadapter/pager2/AssemblySingleDataFragmentStateAdapter.kt

[AssemblyPagingDataFragmentStateAdapter]: assemblyadapter-pager2-paging/src/main/java/com/github/panpf/assemblyadapter/pager2/paging/AssemblyPagingDataFragmentStateAdapter.kt
[AssemblyLoadStateFragmentStateAdapter]: assemblyadapter-pager2-paging/src/main/java/com/github/panpf/assemblyadapter/pager2/paging/AssemblyLoadStateFragmentStateAdapter.kt
[PagingDataFragmentStateAdapter]: assemblyadapter-pager2-paging/src/main/java/com/github/panpf/assemblyadapter/pager2/paging/PagingDataFragmentStateAdapter.kt
[LoadStateFragmentStateAdapter]: assemblyadapter-pager2-paging/src/main/java/com/github/panpf/assemblyadapter/pager2/paging/LoadStateFragmentStateAdapter.kt

[BaseAdapter]: https://developer.android.google.cn/reference/android/widget/BaseAdapter
[RecyclerView.Adapter]: https://developer.android.google.cn/reference/androidx/recyclerview/widget/RecyclerView.Adapter
[ListAdapter]: https://developer.android.google.cn/reference/androidx/recyclerview/widget/ListAdapter
[BaseExpandableListAdapter]: https://developer.android.google.cn/reference/android/widget/BaseExpandableListAdapter
[PagerAdapter]: https://developer.android.google.cn/reference/androidx/viewpager/widget/PagerAdapter
[PagingDataAdapter]: https://developer.android.google.cn/reference/androidx/paging/PagingDataAdapter
[LoadStateAdapter]: https://developer.android.google.cn/reference/androidx/paging/LoadStateAdapter
[FragmentStatePagerAdapter]: https://developer.android.google.cn/reference/androidx/fragment/app/FragmentStatePagerAdapter
[FragmentStateAdapter]: https://developer.android.google.cn/reference/androidx/viewpager2/adapter/FragmentStateAdapter