package me.panpf.adapter;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;

public class LongClickListenerHolder {
    @IdRes
    private int viewId;
    @NonNull
    private OnLongClickListener listener;

    public LongClickListenerHolder(@IdRes int viewId, @NonNull OnLongClickListener listener) {
        this.viewId = viewId;
        this.listener = listener;
    }

    public LongClickListenerHolder(@NonNull OnLongClickListener listener) {
        this.listener = listener;
    }

    @IdRes
    public int getViewId() {
        return viewId;
    }

    @NonNull
    public OnLongClickListener getListener() {
        return listener;
    }
}
