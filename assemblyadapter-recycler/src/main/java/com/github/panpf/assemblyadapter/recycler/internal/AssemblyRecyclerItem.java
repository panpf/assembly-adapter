package com.github.panpf.assemblyadapter.recycler.internal;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.github.panpf.assemblyadapter.Item;

public class AssemblyRecyclerItem<DATA> extends RecyclerView.ViewHolder implements Item<DATA> {

    @NonNull
    private final Item<DATA> wrappedItem;

    public AssemblyRecyclerItem(@NonNull Item<DATA> wrappedItem) {
        super(wrappedItem.getItemView());
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

//    @Override
//    public int getPosition() {
//        return wrappedItem.getPosition();
//    }
}
