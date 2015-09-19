package me.xiaopan.assemblyadapter;

import android.content.Context;
import android.view.View;

public abstract class AssemblyChildItem<BEAN, ITEM_FACTORY extends AssemblyChildItemFactory> {
    private View convertView;
    private ITEM_FACTORY itemFactory;
    private int groupPosition;
    private int childPosition;
    private boolean isLastChild;
    private BEAN data;

    protected AssemblyChildItem(View convertView, ITEM_FACTORY itemFactory) {
        if(convertView == null){
            throw new IllegalArgumentException("param convertView is null");
        }
        if(itemFactory == null){
            throw new IllegalArgumentException("param itemFactory is null");
        }
        this.convertView = convertView;
        this.itemFactory = itemFactory;
        this.convertView.setTag(this);
        onFindViews(convertView);
        onConfigViews(convertView.getContext());
    }

    public void setData(int groupPosition, int childPosition, boolean isLastChild, BEAN bean){
        this.groupPosition = groupPosition;
        this.childPosition = childPosition;
        this.isLastChild = isLastChild;
        this.data = bean;
        onSetData(groupPosition, childPosition, isLastChild, bean);
    }

    protected abstract void onFindViews(View convertView);

    protected abstract void onConfigViews(Context context);

    protected abstract void onSetData(int groupPosition, int childPosition, boolean isLastChild, BEAN bean);

    public final View getConvertView(){
        return this.convertView;
    }

    public ITEM_FACTORY getItemFactory() {
        return itemFactory;
    }

    public int getChildPosition() {
        return childPosition;
    }

    public BEAN getData() {
        return data;
    }

    public int getGroupPosition() {
        return groupPosition;
    }

    public boolean isLastChild() {
        return isLastChild;
    }
}