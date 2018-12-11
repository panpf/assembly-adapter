# 支持 Paging

首先需要从 JCenter 导入：

```groovy
dependencies {
    implementation 'me.panpf:assembly-paged-list-adapter:$lastVersionName'
}
```

`$lastVersionName`：[![Release Version][release_icon]][release_link]


然后用 [AssemblyPagedListAdapter] 替代 [PagedListAdapter] 即可

[AssemblyPagedListAdapter] 扩展了一个无参构造函数，默认使用通用的 [ObjectDiffCallback] 来作为 bean 对比工具

[release_icon]: https://api.bintray.com/packages/panpf/maven/assembly-paged-list-adapter/images/download.svg
[release_link]: https://bintray.com/panpf/maven/assembly-paged-list-adapter/_latestVersion#files

[AssemblyPagedListAdapter]: https://github.com/panpf/assembly-adapter/blob/master/assembly-paged-list-adapter/src/main/java/me/panpf/adapter/paged/AssemblyPagedListAdapter.java
[ObjectDiffCallback]: https://github.com/panpf/assembly-adapter/blob/master/assembly-paged-list-adapter/src/main/java/me/panpf/adapter/paged/ObjectDiffCallback.java
[PagedListAdapter]: https://developer.android.com/reference/android/arch/paging/PagedListAdapter