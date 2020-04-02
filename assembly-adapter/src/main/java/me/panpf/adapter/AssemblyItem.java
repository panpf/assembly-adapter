package me.panpf.adapter;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

// todo 搞一个不为空的 Item 和一个可空的 item
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class AssemblyItem<DATA> implements Item<DATA> {

    @NonNull
    protected Context context;
    @NonNull
    protected Application application;
    @NonNull
    protected Resources resources;

    @Nullable
    private Item<DATA> wrapper;
    @NonNull
    private View itemView;
    @Nullable
    private DATA data;
    private int position;

    private boolean expanded; // ExpandableListView 专用字段
    private int groupPosition; // ExpandableListView 专用字段
    private boolean lastChild; // ExpandableListView 专用字段

    public AssemblyItem(@NonNull View itemView) {
        //noinspection ConstantConditions
        if (itemView == null) {
            throw new IllegalArgumentException("itemView may not be null");
        }
        this.itemView = itemView;
        this.context = itemView.getContext();
        this.application = (Application) itemView.getContext().getApplicationContext();
        this.resources = itemView.getResources();
    }

    public AssemblyItem(int itemLayoutId, @NonNull ViewGroup parent) {
        this(LayoutInflater.from(parent.getContext()).inflate(itemLayoutId, parent, false));
    }

    @Nullable
    @Override
    public Item<DATA> getWrapper() {
        return wrapper;
    }

    @Override
    public void setWrapper(@Nullable Item<DATA> wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    public void onInit(@NonNull Context context) {
        onFindViews();
        onConfigViews(context);
    }

    @Nullable
    @Override
    public DATA getData() {
        return data;
    }

    @Override
    public void setData(int position, @Nullable DATA data) {
        this.position = position;
        this.data = data;
        onSetData(position, data);
    }

    @NonNull
    @Override
    public final View getItemView() {
        return this.itemView;
    }

    @Override
    public int getPosition() {
        return wrapper != null ? wrapper.getPosition() : position;
    }

    @Override
    public int getLayoutPosition() {
        return wrapper != null ? wrapper.getLayoutPosition() : position;
    }

    @Override
    public int getAdapterPosition() {
        return wrapper != null ? wrapper.getAdapterPosition() : position;
    }

    @Nullable
    public <T extends View> T findViewById(int id) {
        return itemView.findViewById(id);
    }

    @Nullable
    public <T extends View> T findViewWithTag(@NonNull Object tag) {
        return itemView.findViewWithTag(tag);
    }

    @Override
    public boolean isExpanded() {
        return expanded;
    }

    @Override
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public int getGroupPosition() {
        return groupPosition;
    }

    @Override
    public void setGroupPosition(int groupPosition) {
        this.groupPosition = groupPosition;
    }

    @Override
    public boolean isLastChild() {
        return lastChild;
    }

    @Override
    public void setLastChild(boolean lastChild) {
        this.lastChild = lastChild;
    }

    /**
     * 专门用来 find view，只会执行一次
     */
    protected void onFindViews() {

    }

    /**
     * 专门用来配置 View，你可在在这里设置 View 的样式以及尺寸，只会执行一次
     */
    protected void onConfigViews(@NonNull Context context) {

    }

    /**
     * 设置数据
     *
     * @param position 位置
     * @param data     数据
     */
    protected abstract void onSetData(int position, @Nullable DATA data);
}
