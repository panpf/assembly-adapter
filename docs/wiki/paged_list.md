# 支持 Paging

首先需要从 JCenter 导入：

```groovy
dependencies {
    implementation 'me.panpf:assembly-paged-list-adapter:$lastVersionName'
}
```

`$lastVersionName`：[![Release Version][release_icon]][release_link]


然后用 [AssemblyPagedListAdapter] 替代 [PagedListAdapter] 即可，另外你可以使用自带的 [DiffableDiffCallback] 实现数据对比（详情查看源码了解）

[release_icon]: https://api.bintray.com/packages/panpf/maven/assembly-paged-list-adapter/images/download.svg
[release_link]: https://bintray.com/panpf/maven/assembly-paged-list-adapter/_latestVersion#files

[AssemblyPagedListAdapter]: ../../assembly-paged-list-adapter/src/main/java/me/panpf/adapter/paged/AssemblyPagedListAdapter.java
[DiffableDiffCallback]: ../../assembly-paged-list-adapter/src/main/java/me/panpf/adapter/paged/DiffableDiffCallback.java
[PagedListAdapter]: https://developer.android.com/reference/android/arch/paging/PagedListAdapter
