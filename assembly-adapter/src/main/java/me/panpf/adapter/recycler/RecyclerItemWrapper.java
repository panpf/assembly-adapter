package me.panpf.adapter.recycler;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import me.panpf.adapter.Item;

@SuppressWarnings({"WeakerAccess"})
public class RecyclerItemWrapper<DATA> extends RecyclerView.ViewHolder implements Item<DATA> {
    @NonNull
    private Item<DATA> item;

    public RecyclerItemWrapper(@NonNull Item<DATA> item) {
        super(item.getItemView());

        this.item = item;
        this.item.setWrapper(this);
    }

    @Override
    public void setWrapper(@Nullable Item<DATA> wrapper) {
        // 不可以重复 wrapper
    }

    @Override
    @Nullable
    public DATA getData() {
        return item.getData();
    }

    @Override
    public void setData(int position, @Nullable DATA data) {
        item.setData(position, data);
    }

    @Override
    @NonNull
    public final View getItemView() {
        return item.getItemView();
    }
}