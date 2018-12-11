package me.panpf.adapter;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

public class ClickListenerHolder<DATA> {
    @IdRes
    private int viewId;
    @NonNull
    private OnClickListener<DATA> listener;

    public ClickListenerHolder(@IdRes int viewId, @NonNull OnClickListener<DATA> listener) {
        this.viewId = viewId;
        this.listener = listener;
    }

    public ClickListenerHolder(@NonNull OnClickListener<DATA> listener) {
        this.listener = listener;
    }

    @IdRes
    public int getViewId() {
        return viewId;
    }

    @NonNull
    public OnClickListener<DATA> getListener() {
        return listener;
    }
}
