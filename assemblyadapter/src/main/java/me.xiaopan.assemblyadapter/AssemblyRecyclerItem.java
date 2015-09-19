package me.xiaopan.assemblyadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class AssemblyRecyclerItem<BEAN, ITEM_FACTORY extends AssemblyRecyclerItemFactory> extends RecyclerView.ViewHolder{
    private ITEM_FACTORY itemFactory;
    private BEAN data;

    protected AssemblyRecyclerItem(View convertView, ITEM_FACTORY itemFactory) {
        super(convertView);
        if(itemFactory == null){
            throw new IllegalArgumentException("param itemFactory is null");
        }
        this.itemFactory = itemFactory;
        onFindViews(convertView);
        onConfigViews(convertView.getContext());
    }

    public void setData(int position, BEAN bean){
        this.data = bean;
        onSetData(position, bean);
    }

    protected abstract void onFindViews(View convertView);

    protected abstract void onConfigViews(Context context);

    protected abstract void onSetData(int position, BEAN bean);

    public final View getConvertView(){
        return itemView;
    }

    public ITEM_FACTORY getItemFactory() {
        return itemFactory;
    }

    public BEAN getData() {
        return data;
    }
}