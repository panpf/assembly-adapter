package me.xiaopan.assemblyadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class AssemblyChildItem<BEAN> {
    private View itemView;
    private int position;
    private int groupPosition;
    private boolean isLastChild;
    private BEAN data;
    private ContentSetter setter;

    public AssemblyChildItem(int itemLayoutId, ViewGroup parent) {
        this(LayoutInflater.from(parent.getContext()).inflate(itemLayoutId, parent, false));
    }

    public AssemblyChildItem(View itemView) {
        if (itemView == null) {
            throw new IllegalArgumentException("itemView may not be null");
        }
        this.itemView = itemView;
        this.itemView.setTag(this);
        onFindViews();
        onConfigViews(itemView.getContext());
    }

    public void setData(int groupPosition, int childPosition, boolean isLastChild, BEAN bean) {
        this.groupPosition = groupPosition;
        this.position = childPosition;
        this.isLastChild = isLastChild;
        this.data = bean;
        onSetData(childPosition, bean);
    }

    public View findViewById(int id) {
        return itemView.findViewById(id);
    }

    @SuppressWarnings("unused")
    public View findViewWithTag(Object tag) {
        return itemView.findViewWithTag(tag);
    }

    protected abstract void onFindViews();

    protected abstract void onConfigViews(Context context);

    protected abstract void onSetData(int position, BEAN bean);

    public final View getItemView() {
        return this.itemView;
    }

    public int getPosition() {
        return position;
    }

    public BEAN getData() {
        return data;
    }

    @SuppressWarnings("unused")
    public int getGroupPosition() {
        return groupPosition;
    }

    @SuppressWarnings("unused")
    public boolean isLastChild() {
        return isLastChild;
    }

    @SuppressWarnings("unused")
    public ContentSetter getSetter() {
        if (setter == null) {
            setter = new ContentSetter(itemView);
        }
        return setter;
    }
}