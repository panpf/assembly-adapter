使用加载更多功能

首先你需要定义一个继承自 AssemblyLoadMoreItemFactory 的 ItemFactory， AssemblyLoadMoreItemFactory 已经将加载更多相关逻辑部分的代码写好了，你只需关心界面即可，如下：

```java
public class LoadMoreItemFactory extends AssemblyLoadMoreListItemFactory {

    public LoadMoreListItemFactory(OnLoadMoreListener eventListener) {
        super(eventListener);
    }

    @Override
    public AssemblyLoadMoreItem createAssemblyItem(ViewGroup parent) {
        return new LoadMoreListItem(R.layout.list_item_load_more, parent);
    }

    public class LoadMoreItem extends AssemblyLoadMoreListItem {
        private View loadingView;
        private View errorView;
        private View endView;

        public LoadMoreListItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews(View itemView) {
            loadingView = findViewById(R.id.text_loadMoreListItem_loading);
            errorView = findViewById(R.id.text_loadMoreListItem_error);
            endView = findViewById(R.id.text_loadMoreListItem_end);
        }

        @Override
        public View getErrorRetryView() {
            return errorView;
        }

        @Override
        public void showLoading() {
            loadingView.setVisibility(View.VISIBLE);
            errorView.setVisibility(View.INVISIBLE);
            endView.setVisibility(View.INVISIBLE);
        }

        @Override
        public void showErrorRetry() {
            loadingView.setVisibility(View.INVISIBLE);
            errorView.setVisibility(View.VISIBLE);
            endView.setVisibility(View.INVISIBLE);
        }

        @Override
        public void showEnd() {
            loadingView.setVisibility(View.INVISIBLE);
            errorView.setVisibility(View.INVISIBLE);
            endView.setVisibility(View.VISIBLE);
        }
    }
}
```

然后调用 Adapter 的 `setLoadMoreItem(AssemblyLoadMoreListItemFactory)` 方法设置加载更多 ItemFactory 即可，如下：

```java
AssemblyListAdapter adapter = ...;
adapter.setLoadMoreItem(new LoadMoreItemFactory(new OnLoadMoreListener(){
    @Override
    public void onLoadMore(AssemblyAdapter adapter) {
        // 访问网络加载数据
        ...

        boolean loadSuccess = ...;
        if (loadSuccess) {
            // 加载成功时判断是否已经全部加载完毕，然后调用Adapter的loadMoreFinished(boolean)方法设置加载更多是否结束
            boolean loadMoreEnd = ...;
            adapter.loadMoreFinished(loadMoreEnd);
        } else {
            // 加载失败时调用Adapter的loadMoreFailed()方法显示加载失败提示，用户点击失败提示则会重新触发加载更多
            adapter.loadMoreFailed();
        }
    }
}));
```

你还可以通过`setDisableLoadMore(boolean)`方法替代 setLoadMoreEnd(boolean) 来控制是否禁用加载更多功能，两者的区别在于 setLoadMoreEnd(boolean) 为 true 时会在列表尾部显示 end 提示，而 setDisableLoadMore(boolean) 则是完全不显示加载更多尾巴