package me.xiaopan.assemblyadapter;

import android.content.Context;
import android.view.View;

public abstract class AssemblyChildItem<BEAN> {
    private View convertView;
    private int groupPosition;
    private int childPosition;
    private boolean isLastChild;
    private BEAN data;

    protected AssemblyChildItem(View convertView) {
        if(convertView == null){
            throw new IllegalArgumentException("param convertView is null");
        }
        this.convertView = convertView;
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