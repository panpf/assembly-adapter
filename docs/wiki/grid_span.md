在 RecyclerView 的 GridLayoutManager 中一个 Item 独占一行或任意列

通过 RecyclerView 的 GridLayoutManager 我们可以很轻松的实现网格列表，同时 GridLayoutManager 还为我们提供了 SpanSizeLookup 接口，可以让我们指定哪一个 Item 需要独占一行或多列，AssemblyRecyclerAdapter 自然对如此重要的功能也做了封装，让你可以更方便的使用它

首先注册 SpanSizeLookup，并通过 AssemblyRecyclerAdapter 的 getSpanSize(int) 方法获取每一个位置的 Item 的 SpanSize：

```java
GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
    @Override
    public int getSpanSize(int position) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null || !(adapter instanceof AssemblyRecyclerAdapter)) {
            return 1;
        }
        return ((AssemblyRecyclerAdapter) adapter).getSpanSize(position);
    }
});
recyclerView.setLayoutManager(gridLayoutManager);
```

然后创建 AssemblyRecyclerAdapter、添加 ItemFactory 并设置 SpanSize

```java
AssemblyRecyclerAdapter adapter = new AssemblyRecyclerAdapter(dataList);
adapter.addItemFactory(new AppListHeaderItemFactory().setSpanSize(4));

recyclerView.setAdapter(adapter);
```

AppListHeaderItemFactory 继承自 AssemblyRecyclerItemFactory 因此其拥有 setSpanSize(int) 方法

你也可以直接使用 fullSpan(RecyclerView) 方法设置独占一行，fullSpan 方法会通过 RecyclerView 取出其GridLayoutManager 的 SpanCount 作为 SpanSize

```java
AssemblyRecyclerAdapter adapter = new AssemblyRecyclerAdapter(dataList);
adapter.addItemFactory(new AppListHeaderItemFactory().fullSpan(recyclerView));

recyclerView.setAdapter(adapter);
```

fullSpan() 方法如果检测到 RecyclerView 的 LayoutManager 是 StaggeredGridLayoutManager 的话，还会自动为 Item 设置 setFullSpan(true)，好让 Item 在 StaggeredGridLayoutManager 中可以独占一行