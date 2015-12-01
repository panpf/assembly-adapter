package me.xiaopan.assemblyadapter;

import android.content.Context;
import android.view.View;

public abstract class AssemblyItem<BEAN, ITEM_FACTORY extends AssemblyItemFactory> {
    protected View convertView;
    protected ITEM_FACTORY itemFactory;
    protected int position;
    protected BEAN data;

    protected AssemblyItem(View convertView, ITEM_FACTORY itemFactory) {
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

    public ITEM_FACTORY getItemFactory() {
        return itemFactory;
    }

    public BEAN getData() {
        return data;
    }

    public int getPosition() {
        return position;
    }
}