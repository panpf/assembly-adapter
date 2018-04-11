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
     * 获取当前 item 的布局位置，{@link android.support.v7.widget.RecyclerView} 专用方法
     */
    int getLayoutPosition();

    /**
     * 获取当前 item 在 adapter 中的位置，{@link android.support.v7.widget.RecyclerView} 专用方法
     */
    int getAdapterPosition();

    /**
     * 是否已展开，{@link android.widget.ExpandableListView} 专用方法
     */
    boolean isExpanded();

    /**
     * 设置是否已展开，{@link android.widget.ExpandableListView} 专用方法
     */
    void setExpanded(boolean expanded);

    /**
     * 获取其 group 的位置，{@link android.widget.ExpandableListView} 专用方法
     */
    int getGroupPosition();

    /**
     * 设置其 group 的位置，{@link android.widget.ExpandableListView} 专用方法
     */
    void setGroupPosition(int groupPosition);

    /**
     * 是否是最后一个 child item，{@link android.widget.ExpandableListView} 专用方法
     */
    boolean isLastChild();

    /**
     * 设置是否是最后一个 child item，{@link android.widget.ExpandableListView} 专用方法
     */
    void setLastChild(boolean lastChild);
}