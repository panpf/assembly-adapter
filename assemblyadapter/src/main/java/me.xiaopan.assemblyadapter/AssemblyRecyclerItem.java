package me.xiaopan.assemblyadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class AssemblyRecyclerItem<BEAN> extends RecyclerView.ViewHolder{
    protected BEAN data;

    protected AssemblyRecyclerItem(View convertView) {
        super(convertView);
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

    public BEAN getData() {
        return data;
    }
}