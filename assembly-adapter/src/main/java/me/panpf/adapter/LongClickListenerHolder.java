package me.panpf.adapter;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

public class LongClickListenerHolder<DATA> {
    @IdRes
    private int viewId;
    @NonNull
    private OnLongClickListener<DATA> listener;

    public LongClickListenerHolder(@IdRes int viewId, @NonNull OnLongClickListener<DATA> listener) {
        this.viewId = viewId;
        this.listener = listener;
    }

    public LongClickListenerHolder(@NonNull OnLongClickListener<DATA> listener) {
        this.listener = listener;
    }

    @IdRes
    public int getViewId() {
        return viewId;
    }

    @NonNull
    public OnLongClickListener<DATA> getListener() {
        return listener;
    }
}
