package com.github.panpf.assemblyadapter;

import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AssemblyWrapperItemFactory<DATA> implements ItemFactory<DATA> {

    @NonNull
    private final ItemFactory<DATA> wrappedItemFactory;

    public AssemblyWrapperItemFactory(@NonNull ItemFactory<DATA> wrappedItemFactory) {
        this.wrappedItemFactory = wrappedItemFactory;
    }

    @Override
    public boolean match(@Nullable Object data) {
        return wrappedItemFactory.match(data);
    }

    @NonNull
    @Override
    public Item<DATA> dispatchCreateItem(@NonNull ViewGroup parent) {
        return wrappedItemFactory.dispatchCreateItem(parent);
    }

    @NonNull
    @Override
    public AssemblyWrapperItemFactory<DATA> setOnViewClickListener(@IdRes int viewId, @NonNull OnClickListener<DATA> onClickListener) {
        wrappedItemFactory.setOnViewClickListener(viewId, onClickListener);
        return this;
    }

    @NonNull
    @Override
    public AssemblyWrapperItemFactory<DATA> setOnViewLongClickListener(@IdRes int viewId, @NonNull OnLongClickListener<DATA> onClickListener) {
        wrappedItemFactory.setOnViewLongClickListener(viewId, onClickListener);
        return this;
    }

    @NonNull
    @Override
    public AssemblyWrapperItemFactory<DATA> setOnItemClickListener(@NonNull OnClickListener<DATA> onClickListener) {
        wrappedItemFactory.setOnItemClickListener(onClickListener);
        return this;
    }

    @NonNull
    @Override
    public AssemblyWrapperItemFactory<DATA> setOnItemLongClickListener(@NonNull OnLongClickListener<DATA> onClickListener) {
        wrappedItemFactory.setOnItemLongClickListener(onClickListener);
        return this;
    }
}
