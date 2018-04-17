package me.panpf.adapter.recycler;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import me.panpf.adapter.AssemblyAdapter;
import me.panpf.adapter.more.AssemblyLoadMoreItem;
import me.panpf.adapter.more.AssemblyLoadMoreItemFactory;
import me.panpf.adapter.more.LoadMoreItemFactoryBridle;

public class RecyclerLoadMoreItemFactoryWrapper implements LoadMoreItemFactoryBridle<RecyclerLoadMoreItemWrapper> {

    @NonNull
    private AssemblyLoadMoreItemFactory itemFactory;

    public RecyclerLoadMoreItemFactoryWrapper(@NonNull AssemblyLoadMoreItemFactory itemFactory) {
        this.itemFactory = itemFactory;
    }

    @Override
    public void loadMoreFinished(boolean end) {
        itemFactory.loadMoreFinished(end);
    }

    @Override
    public void loadMoreFailed() {
        itemFactory.loadMoreFailed();
    }

    @Override
    public int getItemType() {
        return itemFactory.getItemType();
    }

    @Override
    public void setItemType(int itemType) {
        itemFactory.setItemType(itemType);
    }

    @Nullable
    @Override
    public AssemblyAdapter getAdapter() {
        return itemFactory.getAdapter();
    }

    @Override
    public void setAdapter(@NonNull AssemblyAdapter adapter) {
        itemFactory.setAdapter(adapter);
    }

    @Override
    public int getSpanSize() {
        return itemFactory.getSpanSize();
    }

    @NonNull
    @Override
    public RecyclerLoadMoreItemFactoryWrapper setSpanSize(int spanSize) {
        itemFactory.setSpanSize(spanSize);
        return this;
    }

    @NonNull
    @Override
    public RecyclerLoadMoreItemFactoryWrapper fullSpan(@NonNull RecyclerView recyclerView) {
        itemFactory.fullSpan(recyclerView);
        return this;
    }

    @Override
    public boolean isTarget(@NonNull Object data) {
        return itemFactory.isTarget(data);
    }

    @NonNull
    @Override
    public RecyclerLoadMoreItemWrapper dispatchCreateItem(@NonNull ViewGroup parent) {
        return new RecyclerLoadMoreItemWrapper((AssemblyLoadMoreItem) itemFactory.dispatchCreateItem(parent));
    }
}
