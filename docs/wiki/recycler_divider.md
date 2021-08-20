# 给 RecyclerView 配置 divider

[RecyclerView] 默认没有提供 divider 支持，但其提供的 [ItemDecoration] 可以实现 divider

AssemblyAdapter 的 [assemblyadapter-common-recycler-divider] 模块提供了一整套强大的 divider 支持，如下：

* [LinearDividerItemDecoration]：为 [LinearLayoutManager] 提供 Divider
* [GridDividerItemDecoration]：为 [GridLayoutManager] 提供 Divider
* [StaggeredGridDividerItemDecoration]：为 [StaggeredGridLayoutManager] 提供 Divider

支持特性有：

* 支持横向、竖向
* 支持 RTL
* 支持 side divider：如果当前是竖向滑动，那么横向上的 divider 就是 side divider，横向滑动时反之
* divider 支持按 position 或 spanIndex 个性化或禁用

示例

```kotlin
val linearDividerItemDecoration = LinearDividerItemDecoration.Builder(context).apply {
    // divider 为红色 5 个像素
    divider(Divider.color(Color.RED, 5)) {
        // 但是 position 为 10 的 item 的 divider 为 蓝色 5 个像素
        personaliseByPosition(10, Divider.color(Color.BLUE, 5))
        // 同时 position 为 5 的 item 没有 divider
        disableByPosition(5)
    }

    // 头部和尾巴 divider 为空白 20 个像素（默认不显示头部和尾巴 divider）
    headerAndFooterDivider(Divider.space(20))

    // 头部和尾巴 side divider 为绿色 5 个像素
    headerAndFooterSideDivider(Divider.color(Color.GREEN, 5))
}.build()
recyclerView.addItemDecoration(linearDividerItemDecoration)
```

支持设置六种 divider，分别如下（以垂直滑动方向为例，横向滑动时换位即可）：

* divider：显示在滑动方向上每个 item（最后一个 item 除外）的 bottom 边
* headerDivider：显示在滑动方向上第一个 item 的 top 边
* footerDivider：显示在滑动方向上最后一个 item 的 bottom 边
* sideDivider：显示在非滑动方向上每个 item（最后一个 item 除外）的 right 边。`LinearDividerItemDecoration 不支持`
* headerSideDivider：显示在非滑动方向上第一个 item 的 left 边
* footerSideDivider：显示在非滑动方向上最后一个 item 的 right 边

### ItemFactory 支持

[assemblyadapter-recycler] 模块还提供了支持通过 [ItemFactory] 个性化或禁用 divider 的 ItemDecoration，如下：

* [AssemblyLinearDividerItemDecoration]：在 [LinearDividerItemDecoration] 的基础上支持通过 ItemFactory 个性化或禁用
  divider
* [AssemblyGridDividerItemDecoration]：在 [GridDividerItemDecoration] 的基础上支持通过 ItemFactory 个性化或禁用
  divider
* [AssemblyStaggeredGridDividerItemDecoration]：在 [StaggeredGridDividerItemDecoration] 的基础上支持通过
  ItemFactory 个性化或禁用 divider

示例

```kotlin
// ListSeparatorItemFactory 是一个列表分割符 ItemFactory 具体实现就不写了
val linearDividerItemDecoration = AssemblyLinearDividerItemDecoration.Builder(context).apply {
    // divider 为红色 5 个像素
    divider(Divider.color(Color.RED, 5)) {
        personaliseByItemFactoryClass(
            ListSeparatorItemFactory::class,
            Divider.color(Color.BLUE, 5)
        )
    }
}.build()
recyclerView.addItemDecoration(linearDividerItemDecoration)
```

### 扩展支持

[assemblyadapter-common-recycler-divider] 模块还提供了一套扩展支持位于 [DividerExtensions.kt]，如下：

* RecyclerView.newLinearDividerItemDecoration(): LinearDividerItemDecoration
* RecyclerView.newGridDividerItemDecoration(): GridDividerItemDecoration
* RecyclerView.newStaggeredGridDividerItemDecoration(): StaggeredGridDividerItemDecoration
* RecyclerView.addLinearDividerItemDecoration()
* RecyclerView.addGridDividerItemDecoration()
* RecyclerView.addStaggeredGridDividerItemDecoration()

[assemblyadapter-recycler] 模块也提供了一套扩展支持位于 [AssemblyDividerExtensions.kt]，如下：

* RecyclerView.newAssemblyLinearDividerItemDecoration(): AssemblyLinearDividerItemDecoration
* RecyclerView.newAssemblyGridDividerItemDecoration(): AssemblyGridDividerItemDecoration
* RecyclerView.newAssemblyStaggeredGridDividerItemDecoration():
  AssemblyStaggeredGridDividerItemDecoration
* RecyclerView.addAssemblyLinearDividerItemDecoration()
* RecyclerView.addAssemblyGridDividerItemDecoration()
* RecyclerView.addAssemblyStaggeredGridDividerItemDecoration()

### 更多示例

* [RecyclerLinearDividerHorFragment]
* [RecyclerLinearDividerVerFragment]
* [RecyclerStaggeredGridDividerHorFragment]
* [RecyclerStaggeredGridDividerVerFragment]

[RecyclerLinearDividerHorFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/recycler/RecyclerLinearDividerHorFragment.kt

[RecyclerLinearDividerVerFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/recycler/RecyclerLinearDividerVerFragment.kt

[RecyclerStaggeredGridDividerHorFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/recycler/RecyclerStaggeredGridDividerHorFragment.kt

[RecyclerStaggeredGridDividerVerFragment]: ../../sample/src/main/java/com/github/panpf/assemblyadapter/sample/ui/recycler/RecyclerStaggeredGridDividerVerFragment.kt

[ItemFactory]: ../../assemblyadapter-common-item/src/main/java/com/github/panpf/assemblyadapter/ItemFactory.kt

[assemblyadapter-common-recycler-divider]: ../../assemblyadapter-common-recycler-divider

[LinearDividerItemDecoration]: ../../assemblyadapter-common-recycler-divider/src/main/java/com/github/panpf/assemblyadapter/recycler/divider/LinearDividerItemDecoration.kt

[GridDividerItemDecoration]: ../../assemblyadapter-common-recycler-divider/src/main/java/com/github/panpf/assemblyadapter/recycler/divider/GridDividerItemDecoration.kt

[StaggeredGridDividerItemDecoration]: ../../assemblyadapter-common-recycler-divider/src/main/java/com/github/panpf/assemblyadapter/recycler/divider/StaggeredGridDividerItemDecoration.kt

[DividerExtensions.kt]: ../../assemblyadapter-common-recycler-divider/src/main/java/com/github/panpf/assemblyadapter/recycler/divider/DividerExtensions.kt

[assemblyadapter-recycler]: ../../assemblyadapter-recycler

[AssemblyLinearDividerItemDecoration]: ../../assemblyadapter-recycler/src/main/java/com/github/panpf/assemblyadapter/recycler/divider/AssemblyLinearDividerItemDecoration.kt

[AssemblyGridDividerItemDecoration]: ../../assemblyadapter-recycler/src/main/java/com/github/panpf/assemblyadapter/recycler/divider/AssemblyGridDividerItemDecoration.kt

[AssemblyStaggeredGridDividerItemDecoration]: ../../assemblyadapter-recycler/src/main/java/com/github/panpf/assemblyadapter/recycler/divider/AssemblyStaggeredGridDividerItemDecoration.kt

[AssemblyDividerExtensions.kt]: ../../assemblyadapter-recycler/src/main/java/com/github/panpf/assemblyadapter/recycler/divider/AssemblyDividerExtensions.kt

[RecyclerView]: https://developer.android.google.cn/reference/androidx/recyclerview/widget/RecyclerView

[LinearLayoutManager]: https://developer.android.google.cn/reference/androidx/recyclerview/widget/LinearLayoutManager

[GridLayoutManager]: https://developer.android.google.cn/reference/androidx/recyclerview/widget/LinearLayoutManager

[StaggeredGridLayoutManager]: https://developer.android.google.cn/reference/androidx/recyclerview/widget/StaggeredGridLayoutManager

[ItemDecoration]: https://developer.android.google.cn/reference/androidx/recyclerview/widget/RecyclerView.ItemDecoration
