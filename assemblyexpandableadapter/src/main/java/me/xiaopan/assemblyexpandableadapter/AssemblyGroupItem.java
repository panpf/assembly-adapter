package me.xiaopan.assemblyexpandableadapter;

import android.content.Context;
import android.view.View;

public abstract class AssemblyGroupItem<BEAN, ITEM_FACTORY extends AssemblyGroupItemFactory> {
    private View convertView;
    private ITEM_FACTORY itemFactory;
    private int groupPosition;
    private boolean isExpanded;
    private BEAN data;

    protected AssemblyGroupItem(View convertView, ITEM_FACTORY itemFactory) {
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

    public void setData(int groupPosition, boolean isExpanded, BEAN bean){
        this.groupPosition = groupPosition;
        this.isExpanded = isExpanded;
        this.data = bean;
        onSetData(groupPosition, isExpanded, bean);
    }

    protected abstract void onFindViews(View convertView);

    protected abstract void onConfigViews(Context context);

    protected abstract void onSetData(int groupPosition, boolean isExpanded, BEAN bean);

    public final View getConvertView(){
        return this.convertView;
    }

    public ITEM_FACTORY getItemFactory() {
        return itemFactory;
    }

    public BEAN getData() {
        return data;
    }

    public int getGroupPosition() {
        return groupPosition;
    }

    public boolean isExpanded() {
        return isExpanded;
    }
}