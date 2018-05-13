# 加载更多

加载更多 [Item] 会始终固定显示在列表的最底部，不会受到 footer 的影响

### 定义

首先需要继承 [AssemblyMoreItem] 定义一个 [LoadMoreItem]，如下：

```kotlin
class LoadMoreItem(itemFactory: Factory, itemLayoutId: Int, parent: ViewGroup)
    : AssemblyMoreItem<Int>(itemFactory, itemLayoutId, parent) {
    private val loadingView: View by bindView(R.id.text_loadMoreListItem_loading)
    private val errorView: View by bindView(R.id.text_loadMoreListItem_error)
    private val endView: View by bindView(R.id.text_loadMoreListItem_end)

    override fun getErrorRetryView(): View {
        return errorView
    }

    override fun showLoading() {
        loadingView.visibility = View.VISIBLE
        errorView.visibility = View.INVISIBLE
        endView.visibility = View.INVISIBLE
    }

    override fun showErrorRetry() {
        loadingView.visibility = View.INVISIBLE
        errorView.visibility = View.VISIBLE
        endView.visibility = View.INVISIBLE
    }

    override fun showEnd() {
        loadingView.visibility = View.INVISIBLE
        errorView.visibility = View.INVISIBLE
        endView.visibility = View.VISIBLE
    }
}
```

然后继承 [AssemblyMoreItemFactory] 定义一个 [LoadMoreItemFactory]，如下：

```kotlin
class LoadMoreItemFactory(listener: OnLoadMoreListener) : AssemblyMoreItemFactory<Int>(listener) {

    override fun createAssemblyItem(parent: ViewGroup): AssemblyMoreItem<Int> {
        return LoadMoreItem(this, R.layout.list_item_load_more, parent)
    }
}
```

[AssemblyMoreItem] 和 [AssemblyMoreItemFactory] 已经将加载更多相关逻辑部分的代码写好了，你只需关心界面即可

### 使用

通过 [AssemblyAdapter] 的 setMoreItem(MoreItemFactory) 方法设置 [ItemFactory] 即可，如下：

```kotlin
val adapter = AssemblyRecyclerAdapter()
adapter.setMoreItem(LoadMoreItemFactory(OnLoadMoreListener { adapter ->
  // 加载数据
  ...

  val loadSuccess: Boolean = ...
  if (loadSuccess) {
      val loadMoreEnd: Boolean = ...
      adapter.loadMoreFinished(loadMoreEnd)
  } else {
      adapter.loadMoreFailed()
  }
}))
```

### 禁用

还可以通过 [AssemblyAdapter] 的 setEnabledMoreItem(boolean) 方法禁用加载更多功能，与 loadMoreFinished(boolean) 的区别在于后者 为 true 时会在列表尾部显示 end 提示，而前者则是完全不显示加载更多尾巴


[AssemblyAdapter]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/AssemblyAdapter.java

[ItemFactory]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/ItemFactory.java
[Item]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/Item.java

[AssemblyMoreItemFactory]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/more/AssemblyMoreItemFactory.java
[AssemblyMoreItem]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/more/AssemblyMoreItem.java

[LoadMoreItemFactory]: https://github.com/panpf/assembly-adapter/blob/develop/sample/src/main/java/me/panpf/adapter/sample/itemfactory/LoadMoreItemFactory.kt
[LoadMoreItem]: https://github.com/panpf/assembly-adapter/blob/develop/sample/src/main/java/me/panpf/adapter/sample/itemfactory/LoadMoreItem.kt
