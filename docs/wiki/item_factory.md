# 自定义 ItemFactory

通常不建议直接继承 [ItemFactory] 来定义自己的 [ItemFactory]，因为实现 [ItemFactory] 需要再额外定义一个 [Item]，这样写起来会稍显繁琐

AssemblyAdapter 提供了几种简化版的不用定义 [Item] 的子类来简化定义 [ItemFactory] 的流程，如下：

* [BindingItemFactory]：支持 ViewBinding. 只需实现 createItemViewBinding() 方法创建 ViewBinding 以及实现 initItem()
  、bindItemData() 方法来初始化 item 和绑定数据即可
* [SimpleItemFactory]：只需实现 createItemView() 方法创建 item view 以及实现 initItem()、bindItemData() 方法来初始化
  item 和绑定数据即可
* [ViewItemFactory]：外部提供创建好的 item view 或者布局 id，即可直接使用

## 示例

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

### BindingItemFactory 自定义示例

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

### SimpleItemFactory 自定义示例

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

### ViewItemFactory 使用示例

```kotlin
// 显示一个无需填充数据的布局
ViewItemFactory(R.layout.item_app_info)

// 显示一个创建好的 View
val textView = TextView(context).apply {
    text = "Test ViewItemFactory"
}
ViewItemFactory(textView)

// 显示一个延迟创建的 View
ViewItemFactory { context, inflater, parent ->
    TextView(context).apply {
        text = "Test ViewItemFactory"
    }
}
```

### 在 initItem 方法中获取 data 或 position

在 initItem 方法中可以通过 item 来获取 data 和 position，如下：

* item.dataOrThrow：获取 data，如果 data 为 null 则抛出异常
* item.dataOrNull：获取 data，如果 data 为 null 则返回 null
* item.bindingAdapterPosition：获取 item 在其直接绑定的 Adapter 中的位置
* item.absoluteAdapterPosition：获取 item 在 RecyclerView.adapter 中的位置

#### 获取时机

由于 initItem 方法只会在创建 item 的时候执行一次，所以执行的时候 data 和 position 并没有赋值，因此只能在发生事件的时候才能获取到真实有效的值，例如点击事件

#### bindingAdapterPosition 和 absoluteAdapterPosition 的区别

bindingAdapterPosition 表示 item 在其直接绑定的 Adapter 中的位置
<br>
absoluteAdapterPosition 表示 item 在 RecyclerView.adapter 中的位置

这两个值只有在使用 ConcatAdapter 时才会不一样，更多解释可以参考 [ViewHolder] 中的相关解释

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