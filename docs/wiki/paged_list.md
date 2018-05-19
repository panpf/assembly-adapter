# 支持 Paging

首先需要从 JCenter 导入：

```groovy
dependencies {
    implementation 'me.panpf:assembly-paged-list-adapter:$lastVersionName'
}
```

`$lastVersionName`：[![Release Version][release_icon]][release_link]`（不带v）`


然后用 [AssemblyPagedListAdapter] 替代 [PagedListAdapter] 即可

[release_icon]: https://img.shields.io/github/release/panpf/assembly-adapter.svg
[release_link]: https://github.com/panpf/assembly-adapter/releases

[AssemblyPagedListAdapter]: https://github.com/panpf/assembly-adapter/blob/master/assembly-paged-list-adapter/src/main/java/me/panpf/adapter/paged/AssemblyPagedListAdapter.java
[PagedListAdapter]: https://developer.android.com/reference/android/arch/paging/PagedListAdapter
