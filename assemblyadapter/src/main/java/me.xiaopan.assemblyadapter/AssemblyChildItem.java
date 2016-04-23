package me.xiaopan.assemblyadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class AssemblyChildItem<BEAN> {
    private View itemView;
    private int groupPosition;
    private int childPosition;
    private boolean isLastChild;
    private BEAN data;

    public AssemblyChildItem(int itemLayoutId, ViewGroup parent) {
        this(LayoutInflater.from(parent.getContext()).inflate(itemLayoutId, parent, false));
    }

    public AssemblyChildItem(View itemView) {
        if(itemView == null){
            throw new IllegalArgumentException("itemView may not be null");
        }
        this.itemView = itemView;
        this.itemView.setTag(this);
        onFindViews(itemView);
        onConfigViews(itemView.getContext());
    }

    public void setData(int groupPosition, int childPosition, boolean isLastChild, BEAN bean){
        this.groupPosition = groupPosition;
        this.childPosition = childPosition;
        this.isLastChild = isLastChild;
        this.data = bean;
        onSetData(groupPosition, childPosition, isLastChild, bean);
    }

    public View findViewById(int id){
        return itemView.findViewById(id);
    }

    public View findViewWithTag(Object tag){
        return itemView.findViewWithTag(tag);
    }

    protected abstract void onFindViews(View itemView);

    protected abstract void onConfigViews(Context context);

    protected abstract void onSetData(int groupPosition, int childPosition, boolean isLastChild, BEAN bean);

    public final View getItemView(){
        return this.itemView;
    }

    public int getChildPosition() {
        return childPosition;
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
}