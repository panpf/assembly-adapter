package me.panpf.adapter;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;

public class ClickListenerHolder {
    @IdRes
    private int viewId;
    @NonNull
    private OnClickListener listener;

    public ClickListenerHolder(@IdRes int viewId, @NonNull OnClickListener listener) {
        this.viewId = viewId;
        this.listener = listener;
    }

    public ClickListenerHolder(@NonNull OnClickListener listener) {
        this.listener = listener;
    }

    @IdRes
    public int getViewId() {
        return viewId;
    }

    @NonNull
    public OnClickListener getListener() {
        return listener;
    }
}
