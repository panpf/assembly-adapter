# 自定义 ItemFactory

通常不建议直接继承 [ItemFactory] 来定义自己的 [ItemFactory]，因为实现 [ItemFactory] 需要再额外定义一个 [Item]，这样写起来会稍显繁琐

AssemblyAdapter 为 [ItemFactory] 提供了几种不用定义 [Item] 的实现来简化自定义 [ItemFactory] 的流程，如下：

* [BindingItemFactory]：支持 ViewBinding. 只需实现 createItemViewBinding() 方法创建 ViewBinding 以及实现 initItem()
  、bindItemData() 方法来初始化 item 和绑定数据即可
* [SimpleItemFactory]：只需实现 createItemView() 方法创建 item view 以及实现 initItem()、bindItemData() 方法来初始化
  item 和绑定数据即可
* [ViewItemFactory]：外部提供创建好的 item view 或者布局 id，即可直接使用

### 示例

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

#### BindingItemFactory 自定义示例

```kotlin
class AppInfoBindingItemFactory : BindingItemFactory<AppInfo, ItemAppInfoBinding>(AppInfo::class) {

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

#### SimpleItemFactory 自定义示例

```kotlin
class AppInfoSimpleItemFactory : SimpleItemFactory<AppInfo>(AppInfo::class) {

    override fun createItemView(
        context: Context, inflater: LayoutInflater, parent: ViewGroup
    ): ItemAppInfoBinding {
        /*
         * 在此处创建 View。这个方法只执行一次
         */
        return inflater.inflate(R.layout.item_app_info, parent, false)
    }

    override fun initItem(
        context: Context, itemView: View, item: SimpleItem<AppInfo>
    ) {
        /*
         * 此处初始化 item 并绑定 click 事件。这个方法只执行一次
         */
        itemView.setOnClickListener {
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
        itemView: View,
        item: SimpleItem<AppInfo>,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: AppInfo
    ) {
        /*
         * 在此处绑定项 item view 的数据。这个方法会经常执行
         */
        itemView.findViewById<TextView>(R.id.appItemNameText).text = data.name
        itemView.findViewById<TextView>(R.id.appItemVersionText).text = "v${data.versionName}"
        itemView.findViewById<TextView>(R.id.appItemSizeText).text =
            Formatter.formatFileSize(context, data.apkSize)
    }
}
```

#### ViewItemFactory 使用示例

```kotlin
// 显示一个无需填充数据的布局
ViewItemFactory<String>(String::class, R.layout.item_app_info)

// 显示一个创建好的 View
val textView = TextView(context).apply {
    text = "Test ViewItemFactory"
}
ViewItemFactory<String>(String::class, textView)

// 显示一个延迟创建的 View
ViewItemFactory<String>(String::class) { context, inflater, parent ->
    TextView(context).apply {
        text = "Test ViewItemFactory"
    }
}
```

### 精确匹配数据

默认的只要给定 data 是 dataClass 的实例就会认为是匹配的，可有时候我们需要根据 data 的某些属性来判定是否匹配，这时候可以重写 exactMatchData 方法来实现精确匹配

如下我们根据 apk 大小是否大于 1GB 来分为大 App 和小 App：

```kotlin
class BigAppInfoBindingItemFactory : BindingItemFactory<AppInfo, ItemAppInfoBinding>(AppInfo::class) {

  override fun exactMatchData(data: AppInfo): Boolean {
      return data.apkSize >= 1024 * 1024 * 1024
  }

  // ...
}

class SmallAppInfoBindingItemFactory : BindingItemFactory<AppInfo, ItemAppInfoBinding>(AppInfo::class) {

  override fun exactMatchData(data: AppInfo): Boolean {
      return data.apkSize < 1024 * 1024 * 1024
  }

  // ...
}
```

### 在 initItem 方法中获取 data 或 position

在 initItem 方法中可以通过 item 来获取 data 和 position，如下：

* item.dataOrThrow：获取 data，如果 data 为 null 则抛出异常
* item.dataOrNull：获取 data，如果 data 为 null 则返回 null
* item.bindingAdapterPosition：获取 item 在其直接绑定的 Adapter 中的位置
* item.absoluteAdapterPosition：获取 item 在 RecyclerView.adapter 中的位置

#### 获取时机

由于 initItem 方法只会在创建 item 的时候执行一次，所以执行的时候 data 和 position 并没有赋值，因此只有在发生事件的时候才能获取到真实有效的值，例如点击事件

### bindingAdapterPosition 和 absoluteAdapterPosition 的区别

bindingAdapterPosition 表示 item 在其直接绑定的 Adapter 中的位置
<br>
absoluteAdapterPosition 表示 item 在 RecyclerView.adapter 中的位置

这两个值只有在使用 ConcatAdapter 时才会不一样，更多解释可以参考 [ViewHolder] 中的相关解释

### 绑定点击事件

ListView 有 setOnItemClickListener 方法可以方便的设置 item 的点击监听，而 RecyclerView 却没有类似的方法

于是 ItemFactory 就提供了以下方法用于设置 item 或 item 中 view 的监听

```kotlin
ViewItemFactory<String>(String::class, android.R.layout.activity_list_item).apply {
    // 设置 item 的点击监听
    setOnItemClickListener { context, view, bindingAdapterPosition, absoluteAdapterPosition, data ->

    }

    // 设置 item 的长按监听
    setOnItemLongClickListener { context, view, bindingAdapterPosition, absoluteAdapterPosition, data ->
        false
    }

    // 设置 item 中指定 id 的 view 的点击监听
    setOnViewClickListener(R.id.aa_tag_clickBindItem) { context, view, bindingAdapterPosition, absoluteAdapterPosition, data ->

    }

    // 设置 item 中指定 id 的 view 的长按监听
    setOnViewLongClickListener(R.id.aa_tag_clickBindItem) { context, view, bindingAdapterPosition, absoluteAdapterPosition, data ->
        false
    }
}
```

### 保存变量到 Item 中

有时候需要为每个 Item 定义单独的变量，当直接继承 [ItemFactory] 并实现自定义 Item 时这个需求很好实现，将变量定义在 Item 中即可，如下：

```kotlin
class TestItemFactory : ItemFactory<String>(String::class) {

    override fun createItem(parent: ViewGroup): Item<String> {
        return TestItem(TextView(parent.context))
    }

    class TestItem(itemView: TextView) : Item<String>(itemView) {

        private val createNanoTimeString: String = System.nanoTime().toString()

        override fun bindData(
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: String
        ) {
            (itemView as TextView).text = createNanoTimeString
        }
    }
}
```

但使用 BindingItemFactory 或 SimpleItemFactory 的时候不需要定义 Item 了，createNanoTimeString 就没有地方可以存放了，放在
ItemFactory 中的话所有 Item 就都使用同一个 createNanoTimeString 了，这样显然不可以

于是 BindingItem 和 SimpleItem 专门提供了 putExtra(String, Any?) 和 getExtra(String) 方法用来解决此问题，如下所示：

```kotlin
class TestSimpleItemFactory : SimpleItemFactory<String>(String::class) {

    override fun createItemView(
        context: Context,
        inflater: LayoutInflater,
        parent: ViewGroup
    ): View {
        return TextView(parent.context)
    }

    override fun initItem(context: Context, itemView: View, item: SimpleItem<String>) {
        val createNanoTimeString: String = System.nanoTime().toString()
        item.putExtra("createNanoTimeString", createNanoTimeString)
    }

    override fun bindItemData(
        context: Context,
        itemView: View,
        item: SimpleItem<String>,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: String
    ) {
        (itemView as TextView).text = item.getExtraOrThrow<String>("createNanoTimeString")
    }
}
```

### 更多示例

* [AppItemFactory]
* [LinkItemFactory]
* [AppsOverviewItemFactory]
* [ListSeparatorItemFactory]
* [ListFragment]
* [RecyclerLinearFragment]

[ListFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/list/ListFragment.kt

[RecyclerLinearFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/recycler/RecyclerLinearFragment.kt

[AppItemFactory]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/item/AppItemFactory.kt

[LinkItemFactory]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/item/LinkItemFactory.kt

[AppsOverviewItemFactory]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/item/AppsOverviewItemFactory.kt

[ListSeparatorItemFactory]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/item/ListSeparatorItemFactory.kt

[Item]: ../../assemblyadapter-common-item/src/main/java/com/github/panpf/assemblyadapter/Item.kt

[ItemFactory]: ../../assemblyadapter-common-item/src/main/java/com/github/panpf/assemblyadapter/ItemFactory.kt

[ViewItemFactory]: ../../assemblyadapter-common-item/src/main/java/com/github/panpf/assemblyadapter/ViewItemFactory.kt

[BindingItemFactory]: ../../assemblyadapter-common-item/src/main/java/com/github/panpf/assemblyadapter/BindingItemFactory.kt

[SimpleItemFactory]: ../../assemblyadapter-common-item/src/main/java/com/github/panpf/assemblyadapter/SimpleItemFactory.kt

[ViewHolder]: https://developer.android.google.cn/reference/androidx/recyclerview/widget/RecyclerView.ViewHolder
