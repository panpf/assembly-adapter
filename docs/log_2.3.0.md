BUG：
>* 修复AssemblyLoadMoreItem的getErrorRetryView()方法返回null时崩溃的BUG

新增：
>* AssemblyRecyclerItemFactory新增getSpanSize()方法，AssemblyRecyclerAdapter新增getSpanSize(int position)方法，配合GridLayoutManager.SpanSizeLookup可让Item横跨多列
>* AssemblyRecyclerItem新增fullSpan(RecyclerView)方法，可让Item在GridLayoutManager或StaggeredGridLayoutManager里独占一行
>* 支持加载更多的Adapter新增loadMoreFinished(boolean)方法，用来替代setLoadMoreEnd(boolean)方法

删除：
>* 删除ContentSetter