# AssemblyAdapter

![Platform][platform_image]
[![API][api_image]][api_link]
[![Release][release_icon]][release_link]
[![License][license_image]][license_link]

AssemblyAdapter 是 Android 上的一个为各种 Adapter 提供开箱即用实现的库。

## 特性

* `Item 复用`. 只需为你的 item 写一个 [ItemFactory]，然后就可以到处使用了. [了解更多][docs_item_factory]
* `支持多类型`. 只需给 Adapter 添加多个 [ItemFactory] 即可轻松实现多类型 Adapter
* `支持全部 Adapter`. 支持 [BaseAdapter]、[RecyclerView.Adapter] 等常用 Adapter. [了解更多](#support_adapters)
* `更多 ConcatAdapter 支持`. 为 BaseAdapter 等更多 Adapter 提供了 Concat 支持. [了解更多][docs_concat_adapter]
* `支持 Paging`. 为 Paging 提供了多类型支持. [了解更多][docs_paging]
* `支持 ViewPager 和 ViewPager2`. 为 ViewPager 和 ViewPager2 提供了多类型及 Paging 分页支持. [了解更多][docs_pager]
* `支持 spanSize 和 fullSpan`. 提供了专用的 LayoutManager，可以根据 ItemFactory 设置 spanSize 和
  fullSpan. [了解更多][docs_grid_span]
* `支持 divider`. 为 RecyclerView 提供了强大的 divider 支持，还可以根据 position/spanIndex/ItemFactory 个性化或禁用
  divider. [了解更多][docs_recycler_divider]
* `支持占位符 Placeholder`. 通过固定的占位符数据类型支持占位符. [了解更多][docs_placeholder]

## 导入

`该库已发布到 mavenCentral`

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

*每个模块包含哪些 Adapter，可以参考后面 '支持的 Adapter' 部分*

`${LAST_VERSION}`：[![Release Version][release_icon]][release_link] (no include 'v')

## 使用指南

在传统的自定义 Adapter 的过程中我们一般需要以下几个步骤（以 RecyclerView.Adapter 为例，其它 Adapter 大同小异）：

1. 定义 data 列表
2. 重写 getItemCount、getItemId 方法
3. 重写 getItemViewType、onCreateViewHolder、onBindViewHolder 方法根据不同的 data 提供不同的结果或实现

AssemblyAdapter 将这一传统定义过程拆分为两个组件，其职责分别如下：

1. Adapter：
    2. 定义 data 列表
    3. 重写 getItemCount、getItemId 方法
    4. 根据不同的 data 匹配不同的 ItemFactory
    5. 使用匹配的 ItemFactory 重写 getItemViewType、onCreateViewHolder、onBindViewHolder 方法
2. ItemFactory
    3. 定义目标 data 的 class
    4. 创建 item view
    5. 绑定 data

### <span id="support_adapters"> 支持的 Adapter </span>

[AssemblyAdapter] 只是一个接口，不可以直接使用，你需要针对不同的 Adapter 使用具体的实现类，如下所示：

* [assemblyadapter-list]
    * [BaseAdapter]
        * [AssemblyListAdapter]：多类型 Adapter 实现
        * [AssemblySingleDataListAdapter]：单数据实现
        * [ConcatListAdapter]：连接 Adapter 实现
    * [BaseExpandableListAdapter]
        * [AssemblyExpandableListAdapter]：多类型 Adapter 实现
        * [AssemblySingleDataExpandableListAdapter]：单数据实现
        * [ConcatExpandableListAdapter]：连接 Adapter 实现
* [assemblyadapter-pager]
    * [PagerAdapter]
        * [AssemblyPagerAdapter]：多类型 Adapter 实现
        * [AssemblySingleDataPagerAdapter]：单数据实现
        * [ConcatPagerAdapter]：连接 Adapter 实现
    * [FragmentStatePagerAdapter]
        * [AssemblyFragmentStatePagerAdapter]：多类型 Adapter 实现
        * [AssemblySingleDataFragmentStatePagerAdapter]：单数据实现
        * [ConcatFragmentStatePagerAdapter]：连接 Adapter 实现
* [assemblyadapter-pager2]
    * [FragmentStateAdapter]
        * [AssemblyFragmentStateAdapter]：多类型 Adapter 实现
        * [AssemblySingleDataFragmentStateAdapter]：单数据实现
* [assemblyadapter-pager2-paging]
    * [FragmentStateAdapter]
        * [PagingDataFragmentStateAdapter]：Paging 实现
        * [LoadStateFragmentStateAdapter]：LoadState 实现
        * [AssemblyPagingDataFragmentStateAdapter]：多类型 + Paging Adapter 实现
        * [AssemblyLoadStateFragmentStateAdapter]：多类型 + Paging 加载状态 Adapter 实现
* [assemblyadapter-recycler]
    * [RecyclerView.Adapter]
        * [AssemblyRecyclerAdapter]：多类型 Adapter 实现
        * [AssemblySingleDataRecyclerAdapter]：单数据实现
    * [ListAdapter]
        * [AssemblyRecyclerListAdapter]：多类型 Adapter 实现
        * [AssemblySingleDataRecyclerListAdapter]：单数据实现
* [assemblyadapter-recycler-paging]
    * [PagingDataAdapter]
        * [AssemblyPagingDataAdapter]：多类型 Adapter 实现
    * [LoadStateAdapter]
        * [AssemblyLoadStateAdapter]：Paging 加载状态 Adapter 实现

### 定义 ItemFactory

下面演示继承 [BindingItemFactory] 来定义我们的 [ItemFactory]

*更多自定义 [ItemFactory] 详细内容请参考 [ItemFactory 自定义详解][docs_item_factory]*

item 布局定义如下 (item_app_info.xml)：

```xml

<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android" android:layout_width="match_parent"
    android:layout_height="wrap_content">
    <TextView android:id="@+id/appItemNameText" />
    <TextView android:id="@+id/appItemVersionText" />
    <TextView android:id="@+id/appItemSizeText" />
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
         * 在此处初始化 item 并绑定 click 事件。这个方法只执行一次
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

### 使用 ItemFactory 创建多类型 Adapter

只需在创建 Adapter 时通过构造函数传入 ItemFactory 即可，传入多个 ItemFactory 就可以实现多类型 Adapter，如下：

```kotlin
// ListSeparatorItemFactory 是一个列表分割符 ItemFactory 具体实现就不写了
val appAdapter = AssemblyRecyclerAdapter(
    listOf(AppInfoItemFactory(), ListSeparatorItemFactory())
)

appAdapter.submitList(
    listOf(
        ListSeparator("A"),
        AppInfo("AirPortal", "cn.airportal", "4.21", 1258291L),
        AppInfo("Apex Legends Mobile", "com.ea.gp.apex", "1.2", 100258291L),
        AppInfo("APKPure", "com.apkpure.aegon", "3.17.23", 157879798L),
        ListSeparator("B"),
        AppInfo("Block Earth", "com.craft.earth", "2.42", 57879798L),
        AppInfo("Bluestack", "app.bluestack", "1.0.0", 41534523L),
        ListSeparator("C"),
        AppInfo("Craft Pixel Art Rain", "com.lucky.fairy", "15", 4247204L),
        AppInfo("Cutting Edge!", "com.cuttingedge", "0.16", 4289472412L),
        AppInfo("Cyber Knights", "com..cyberknightselite", "2.9.4", 6174924L),
        AppInfo("Guardians", "com.emagroups.cs", "1.2.3", 7782423L),
    )
)

RecyclerView(activity).adapter = appAdapter
```

### 更多功能

* [自定义 ItemFactory][docs_item_factory]
* [通过 ConcatAdapter 实现 header 和 footer][docs_header_and_footer]
* [为 BaseAdapter 等更多 Adapter 提供 Concat 支持][docs_concat_adapter]
* [给 RecyclerView 配置 divider][docs_recycler_divider]
* [设置 spanSize 和 fullSpan][docs_grid_span]
* [支持 Paging][docs_paging]
* [支持 ViewPager 和 ViewPager2][docs_pager]
* [BaseExpandableListAdapter 支持][docs_expandable_list_adapter]
* [通过 AssemblySingleData*Adapter 实现只有一条数据的 Adapter][docs_single_data_adapter]
* [使用占位符 Placeholder][docs_placeholder]
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

[assemblyadapter-list]: assemblyadapter-list

[assemblyadapter-pager]: assemblyadapter-pager

[assemblyadapter-pager2]: assemblyadapter-pager2

[assemblyadapter-pager2-paging]: assemblyadapter-pager2-paging

[assemblyadapter-recycler]: assemblyadapter-recycler

[assemblyadapter-recycler-paging]: assemblyadapter-recycler-paging

[docs_expandable_list_adapter]: docs/wiki/expandable_list_adapter.md

[docs_grid_span]: docs/wiki/grid_span.md

[docs_paging]: docs/wiki/paging.md

[docs_item_factory]: docs/wiki/item_factory.md

[docs_single_data_adapter]: docs/wiki/single_data_adapter.md

[docs_old_api_compat]: docs/wiki/old_api_compat.md

[docs_concat_adapter]: docs/wiki/concat_adapter.md

[docs_pager]: docs/wiki/pager.md

[docs_recycler_divider]: docs/wiki/recycler_divider.md

[docs_header_and_footer]: docs/wiki/header_and_footer.md

[docs_placeholder]: docs/wiki/placeholder.md

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

[AssemblySingleDataRecyclerListAdapter]: assemblyadapter-recycler/src/main/java/com/github/panpf/assemblyadapter/recycler/AssemblySingleDataRecyclerListAdapter.kt

[AssemblySingleDataRecyclerAdapter]: assemblyadapter-recycler/src/main/java/com/github/panpf/assemblyadapter/recycler/AssemblySingleDataRecyclerAdapter.kt

[AssemblyGridLayoutManager]: assemblyadapter-recycler/src/main/java/com/github/panpf/assemblyadapter/recycler/AssemblyGridLayoutManager.kt

[AssemblyStaggeredGridLayoutManager]: assemblyadapter-recycler/src/main/java/com/github/panpf/assemblyadapter/recycler/AssemblyStaggeredGridLayoutManager.kt

[AssemblyPagingDataAdapter]: assemblyadapter-recycler-paging/src/main/java/com/github/panpf/assemblyadapter/recycler/paging/AssemblyPagingDataAdapter.kt

[AssemblyLoadStateAdapter]: assemblyadapter-recycler-paging/src/main/java/com/github/panpf/assemblyadapter/recycler/paging/AssemblyLoadStateAdapter.kt

[AssemblyPagerAdapter]: assemblyadapter-pager/src/main/java/com/github/panpf/assemblyadapter/pager/AssemblyPagerAdapter.kt

[AssemblyFragmentStatePagerAdapter]: assemblyadapter-pager/src/main/java/com/github/panpf/assemblyadapter/pager/AssemblyFragmentStatePagerAdapter.kt

[AssemblySingleDataPagerAdapter]: assemblyadapter-pager/src/main/java/com/github/panpf/assemblyadapter/pager/AssemblySingleDataPagerAdapter.kt

[AssemblySingleDataFragmentStatePagerAdapter]: assemblyadapter-pager/src/main/java/com/github/panpf/assemblyadapter/pager/AssemblySingleDataFragmentStatePagerAdapter.kt

[ConcatPagerAdapter]: assemblyadapter-pager/src/main/java/com/github/panpf/assemblyadapter/pager/ConcatPagerAdapter.kt

[ConcatFragmentStatePagerAdapter]: assemblyadapter-pager/src/main/java/com/github/panpf/assemblyadapter/pager/ConcatFragmentStatePagerAdapter.kt

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
