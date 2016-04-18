package me.xiaopan.assemblyadapter;

import android.content.Context;
import android.view.View;

public abstract class AssemblyItem<BEAN> {
    private View convertView;
    private int position;
    private BEAN data;

    protected AssemblyItem(View convertView) {
        if(convertView == null){
            throw new IllegalArgumentException("param convertView is null");
        }
        this.convertView = convertView;
        this.convertView.setTag(this);
        onFindViews(convertView);
        onConfigViews(convertView.getContext());
    }

    public void setData(int position, BEAN bean){
        this.position = position;
        this.data = bean;
        onSetData(position, bean);
    }

    protected abstract void onFindViews(View convertView);

    protected abstract void onConfigViews(Context context);

    protected abstract void onSetData(int position, BEAN bean);

    public final View getConvertView(){
        return this.convertView;
    }

    public BEAN getData() {
        return data;
    }

    public int getPosition() {
        return position;
    }
}