package me.panpf.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

public interface Item<DATA> {
    void setWrapper(@Nullable Item<DATA> wrapper);

    /**
     * 获取当前 item 的数据
     */
    @Nullable
    DATA getData();

    /**
     * 设置数据，此方法由 {@link me.panpf.adapter.AssemblyAdapter} 调用
     *
     * @param position 位置
     * @param data     数据
     */
    void setData(int position, @Nullable DATA data);

    /**
     * 获取当前 item 的 View
     */
    @NonNull
    View getItemView();

    /**
     * 获取当前 item 的位置
     */
    int getPosition();

    /**
     * 获取当前 item 的布局位置，为 {@link android.support.v7.widget.RecyclerView} 预留的方法
     */
    int getLayoutPosition();

    /**
     * 获取当前 item 在 adapter 中的位置，为 {@link android.support.v7.widget.RecyclerView} 预留的方法
     */
    int getAdapterPosition();
}