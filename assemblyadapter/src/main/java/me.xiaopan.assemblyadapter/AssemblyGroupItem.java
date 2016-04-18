package me.xiaopan.assemblyadapter;

import android.content.Context;
import android.view.View;

public abstract class AssemblyGroupItem<BEAN> {
    protected View convertView;
    protected int groupPosition;
    protected boolean isExpanded;
    protected BEAN data;

    protected AssemblyGroupItem(View convertView) {
        if(convertView == null){
            throw new IllegalArgumentException("param convertView is null");
        }
        this.convertView = convertView;
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