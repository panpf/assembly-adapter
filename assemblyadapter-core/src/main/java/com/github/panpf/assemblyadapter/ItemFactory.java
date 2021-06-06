package com.github.panpf.assemblyadapter;

import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface ItemFactory<DATA> {

    boolean match(@Nullable Object data);

    @NonNull
    Item<DATA> dispatchCreateItem(@NonNull ViewGroup parent);

    @NonNull
    ItemFactory<DATA> setOnViewClickListener(@IdRes int viewId, @NonNull OnClickListener<DATA> onClickListener);

    @NonNull
    ItemFactory<DATA> setOnViewLongClickListener(@IdRes int viewId, @NonNull OnLongClickListener<DATA> onClickListener);

    @NonNull
    ItemFactory<DATA> setOnItemClickListener(@NonNull OnClickListener<DATA> onClickListener);

    @NonNull
    ItemFactory<DATA> setOnItemLongClickListener(@NonNull OnLongClickListener<DATA> onClickListener);
}
