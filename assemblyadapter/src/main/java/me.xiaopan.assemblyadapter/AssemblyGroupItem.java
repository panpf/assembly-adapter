package me.xiaopan.assemblyadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class AssemblyGroupItem<BEAN> {
    private View itemView;
    private int groupPosition;
    private boolean isExpanded;
    private BEAN data;

    public AssemblyGroupItem(int itemLayoutId, ViewGroup parent) {
        this(LayoutInflater.from(parent.getContext()).inflate(itemLayoutId, parent, false));
    }

    public AssemblyGroupItem(View itemView) {
        if (itemView == null) {
            throw new IllegalArgumentException("itemView may not be null");
        }
        this.itemView = itemView;
        this.itemView.setTag(this);
        onFindViews();
        onConfigViews(itemView.getContext());
    }

    public void setData(int groupPosition, boolean isExpanded, BEAN bean) {
        this.groupPosition = groupPosition;
        this.isExpanded = isExpanded;
        this.data = bean;
        onSetData(groupPosition, isExpanded, bean);
    }

    public View findViewById(int id) {
        return itemView.findViewById(id);
    }

    public View findViewWithTag(Object tag) {
        return itemView.findViewWithTag(tag);
    }

    protected abstract void onFindViews();

    protected abstract void onConfigViews(Context context);

    protected abstract void onSetData(int groupPosition, boolean isExpanded, BEAN bean);

    public final View getItemView() {
        return this.itemView;
    }

    public BEAN getData() {
        return data;
    }

    @SuppressWarnings("unused")
    public int getGroupPosition() {
        return groupPosition;
    }

    @SuppressWarnings("unused")
    public boolean isExpanded() {
        return isExpanded;
    }
}