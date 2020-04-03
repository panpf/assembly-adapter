package me.panpf.adapter;

import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ViewTypeManager {

    private int index = 0;
    private boolean locked;
    @NonNull
    private SparseArray<Object> binder = new SparseArray<>();

    public boolean isLocked() {
        return locked;
    }

    public void lock() {
        locked = true;
    }

    public int getCount() {
        return index > 0 ? index : 1;
    }

    public int add(@NonNull Object item) {
        int newViewType = index++;
        binder.put(newViewType, item);
        return newViewType;
    }

    @Nullable
    public Object get(int viewType) {
        return binder.get(viewType);
    }
}
