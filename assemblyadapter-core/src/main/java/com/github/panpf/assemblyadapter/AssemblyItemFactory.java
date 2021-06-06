package com.github.panpf.assemblyadapter;

import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class AssemblyItemFactory<DATA> implements ItemFactory<DATA> {

    @Nullable
    private ClickListenerManager<DATA> clickListenerManager;

    @NonNull
    @Override
    public AssemblyItem<DATA> dispatchCreateItem(@NonNull ViewGroup parent) {
        AssemblyItem<DATA> item = createItem(parent);

        if (clickListenerManager != null) {
            clickListenerManager.register(item, item.getItemView());
        }

        return item;
    }

    @NonNull
    public abstract AssemblyItem<DATA> createItem(@NonNull ViewGroup parent);


    @NonNull
    @Override
    public AssemblyItemFactory<DATA> setOnViewClickListener(@IdRes int viewId, @NonNull OnClickListener<DATA> onClickListener) {
        if (clickListenerManager == null) {
            clickListenerManager = new ClickListenerManager<>();
        }
        clickListenerManager.add(viewId, onClickListener);
        return this;
    }

    @NonNull
    @Override
    public AssemblyItemFactory<DATA> setOnViewLongClickListener(@IdRes int viewId, @NonNull OnLongClickListener<DATA> onClickListener) {
        if (clickListenerManager == null) {
            clickListenerManager = new ClickListenerManager<>();
        }
        clickListenerManager.add(viewId, onClickListener);
        return this;
    }

    @NonNull
    @Override
    public AssemblyItemFactory<DATA> setOnItemClickListener(@NonNull OnClickListener<DATA> onClickListener) {
        if (clickListenerManager == null) {
            clickListenerManager = new ClickListenerManager<>();
        }
        clickListenerManager.add(onClickListener);
        return this;
    }

    @NonNull
    @Override
    public AssemblyItemFactory<DATA> setOnItemLongClickListener(@NonNull OnLongClickListener<DATA> onClickListener) {
        if (clickListenerManager == null) {
            clickListenerManager = new ClickListenerManager<>();
        }
        clickListenerManager.add(onClickListener);
        return this;
    }
}
