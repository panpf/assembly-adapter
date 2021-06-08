package com.github.panpf.assemblyadapter.list;

import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.panpf.assemblyadapter.ClickListenerManager;
import com.github.panpf.assemblyadapter.ItemFactory;
import com.github.panpf.assemblyadapter.OnClickListener;
import com.github.panpf.assemblyadapter.OnLongClickListener;

public abstract class AssemblyExpandableItemFactory<DATA> implements ItemFactory<DATA> {

    @Nullable
    private ClickListenerManager<DATA> clickListenerManager;

    @NonNull
    @Override
    public AssemblyExpandableItem<DATA> dispatchCreateItem(@NonNull ViewGroup parent) {
        AssemblyExpandableItem<DATA> item = createItem(parent);

        if (clickListenerManager != null) {
            clickListenerManager.register(item, item.getItemView());
        }

        return item;
    }

    @NonNull
    public abstract AssemblyExpandableItem<DATA> createItem(@NonNull ViewGroup parent);


    @NonNull
    @Override
    public AssemblyExpandableItemFactory<DATA> setOnViewClickListener(@IdRes int viewId, @NonNull OnClickListener<DATA> onClickListener) {
        if (clickListenerManager == null) {
            clickListenerManager = new ClickListenerManager<>();
        }
        clickListenerManager.add(viewId, onClickListener);
        return this;
    }

    @NonNull
    @Override
    public AssemblyExpandableItemFactory<DATA> setOnViewLongClickListener(@IdRes int viewId, @NonNull OnLongClickListener<DATA> onClickListener) {
        if (clickListenerManager == null) {
            clickListenerManager = new ClickListenerManager<>();
        }
        clickListenerManager.add(viewId, onClickListener);
        return this;
    }

    @NonNull
    @Override
    public AssemblyExpandableItemFactory<DATA> setOnItemClickListener(@NonNull OnClickListener<DATA> onClickListener) {
        if (clickListenerManager == null) {
            clickListenerManager = new ClickListenerManager<>();
        }
        clickListenerManager.add(onClickListener);
        return this;
    }

    @NonNull
    @Override
    public AssemblyExpandableItemFactory<DATA> setOnItemLongClickListener(@NonNull OnLongClickListener<DATA> onClickListener) {
        if (clickListenerManager == null) {
            clickListenerManager = new ClickListenerManager<>();
        }
        clickListenerManager.add(onClickListener);
        return this;
    }
}
