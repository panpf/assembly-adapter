在 Kotlin 中使用并兼容 Kotterknife

从 2.3.2 开始 AssemblyAdapter 支持在 Kotlin 中使用 [Kotterknife] 来注入 View，之前的版本不行，如果你用的还是旧版本请尽快升级

AssemblyRecyclerItem 继承自 RecyclerView.ViewHolder 因此通过 [Kotterknife] 其可以使用 bindView

AssemblyItem、AssemblyGroupItem、AssemblyChildItem 就需要自己动手扩展 [Kotterknife] 了，将如下代码加入 [Kotterknife] 的 [ButterKnife.kt] 文件即可

```kotlin
public fun <V : View> AssemblyListItem<*>.bindView(id: Int)
        : ReadOnlyProperty<AssemblyListItem<*>, V> = required(id, viewFinder)

private val AssemblyListItem<*>.viewFinder: AssemblyListItem<*>.(Int) -> View?
    get() = { itemView.findViewById(it) }

public fun <V : View> AssemblyGroupItem<*>.bindView(id: Int)
        : ReadOnlyProperty<AssemblyGroupItem<*>, V> = required(id, viewFinder)

private val AssemblyGroupItem<*>.viewFinder: AssemblyGroupItem<*>.(Int) -> View?
    get() = { itemView.findViewById(it) }

public fun <V : View> AssemblyChildItem<*>.bindView(id: Int)
        : ReadOnlyProperty<AssemblyChildItem<*>, V> = required(id, viewFinder)

private val AssemblyChildItem<*>.viewFinder: AssemblyChildItem<*>.(Int) -> View?
    get() = { itemView.findViewById(it) }
```

详情可参考示例 app 中的 [Sample ButterKnife.kt] 文件