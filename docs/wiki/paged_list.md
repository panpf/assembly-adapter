# 支持 Paging

首先需要额外导入 assemblyadapeter-paing，模块，请参考首页 README 

然后用 [AssemblyPagedListAdapter] 替代 [PagedListAdapter] 即可，另外你可以使用自带的 [DiffableDiffCallback] 实现数据对比（详情查看源码了解）

[AssemblyPagedListAdapter]: ../../assembly-paged-list-adapter/src/main/java/me/panpf/adapter/paged/AssemblyPagedListAdapter.java
[DiffableDiffCallback]: ../../assembly-paged-list-adapter/src/main/java/me/panpf/adapter/paged/DiffableDiffCallback.java
[PagedListAdapter]: https://developer.android.com/reference/android/arch/paging/PagedListAdapter