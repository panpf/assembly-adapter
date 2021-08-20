# 支持 Paging

### 导入

首先需要导入 `assemblyadapter-recycler-paging` 模块，请参考 [README] 中的 `导入` 部分

### AssemblyPagingDataAdapter

[AssemblyPagingDataAdapter] 用来代替 [PagingDataAdapter]，具体用法和 [AssemblyRecyclerAdapter] 一样

需要注意的是 [AssemblyPagingDataAdapter] 默认使用了 [KeyEqualsDiffItemCallback] 作为 [DiffUtil.ItemCallback]
的实现，所以会在构造函数中检查所有 [ItemFactory] 的 dataClass 必须实现 [DiffKey] 接口

如果你不想使用 [KeyEqualsDiffItemCallback] 可以在创建 [AssemblyPagingDataAdapter] 时通过构造函数的 `diffCallback`
属性设置别的 [DiffUtil.ItemCallback]

### KeyEqualsDiffItemCallback

[KeyEqualsDiffItemCallback] 使用 [DiffKey] 接口的 `diffKey` 属性来实现 areItemsTheSame 方法，然后使用 equals 来实现
areContentsTheSame 方法

### AssemblyLoadStateAdapter

[AssemblyLoadStateAdapter] 用来代替 [LoadStateAdapter]


[README]: ../../README.md

[AssemblyRecyclerAdapter]: ../../assemblyadapter-recycler/src/main/java/com/github/panpf/assemblyadapter/recycler/AssemblyRecyclerAdapter.kt

[AssemblyPagingDataAdapter]: ../../assemblyadapter-recycler-paging/src/main/java/com/github/panpf/assemblyadapter/recycler/paging/AssemblyPagingDataAdapter.kt

[AssemblyLoadStateAdapter]: ../../assemblyadapter-recycler-paging/src/main/java/com/github/panpf/assemblyadapter/recycler/paging/AssemblyLoadStateAdapter.kt

[KeyEqualsDiffItemCallback]: ../../assemblyadapter-common-recycler/src/main/java/com/github/panpf/assemblyadapter/recycler/KeyEqualsDiffItemCallback.kt

[DiffKey]: ../../assemblyadapter-common-recycler/src/main/java/com/github/panpf/assemblyadapter/recycler/DiffKey.kt

[ItemFactory]: ../../assemblyadapter-common-item/src/main/java/com/github/panpf/assemblyadapter/ItemFactory.kt

[PagingDataAdapter]: https://developer.android.google.cn/reference/androidx/paging/PagingDataAdapter

[LoadStateAdapter]: https://developer.android.google.cn/reference/androidx/paging/LoadStateAdapter

[DiffUtil.ItemCallback]: https://developer.android.google.cn/reference/androidx/recyclerview/widget/DiffUtil.ItemCallback?hl=en