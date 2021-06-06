package com.github.panpf.assemblyadapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AssemblyWrapperItem<DATA> implements Item<DATA> {

    @NonNull
    private final Item<DATA> wrappedItem;

    public AssemblyWrapperItem(@NonNull Item<DATA> wrappedItem) {
        this.wrappedItem = wrappedItem;
    }

    @Override
    public void dispatchBindData(int position, @Nullable DATA data) {
        wrappedItem.dispatchBindData(position, data);
    }

    @NonNull
    @Override
    public final View getItemView() {
        return wrappedItem.getItemView();
    }

    @Nullable
    @Override
    public DATA getData() {
        return wrappedItem.getData();
    }

    @Override
    public int getPosition() {
        return wrappedItem.getPosition();
    }
}
