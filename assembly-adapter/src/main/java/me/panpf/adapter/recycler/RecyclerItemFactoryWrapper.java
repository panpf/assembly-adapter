package me.panpf.adapter.recycler;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import me.panpf.adapter.AssemblyAdapter;
import me.panpf.adapter.ItemFactory;
import me.panpf.adapter.WrapperItemFactory;

public class RecyclerItemFactoryWrapper implements ItemFactory<RecyclerItemWrapper>, WrapperItemFactory {

    @NonNull
    private ItemFactory itemFactory;

    public RecyclerItemFactoryWrapper(@NonNull ItemFactory itemFactory) {
        this.itemFactory = itemFactory;
    }

    @Override
    public int getItemType() {
        return itemFactory.getItemType();
    }

    @Override
    public void setItemType(int itemType) {
        itemFactory.setItemType(itemType);
    }

    @Override
    @Nullable
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
    public RecyclerItemFactoryWrapper setSpanSize(int spanSize) {
        itemFactory.setSpanSize(spanSize);
        return this;
    }

    @NonNull
    @Override
    public RecyclerItemFactoryWrapper fullSpan(@NonNull RecyclerView recyclerView) {
        itemFactory.fullSpan(recyclerView);
        return this;
    }

    @Override
    public boolean isTarget(@Nullable Object data) {
        return itemFactory.isTarget(data);
    }

    @NonNull
    @Override
    public RecyclerItemWrapper dispatchCreateItem(@NonNull ViewGroup parent) {
        //noinspection unchecked
        return new RecyclerItemWrapper(itemFactory.dispatchCreateItem(parent));
    }

    @NonNull
    @Override
    public ItemFactory getWrappedItemFactory() {
        return itemFactory;
    }
}
