package me.xiaopan.assemblyadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class AssemblyRecyclerItem<BEAN> extends RecyclerView.ViewHolder{
    private BEAN data;

    public AssemblyRecyclerItem(int itemLayoutId, ViewGroup parent) {
        this(LayoutInflater.from(parent.getContext()).inflate(itemLayoutId, parent, false));
    }

    public AssemblyRecyclerItem(View convertView) {
        super(convertView);
        onFindViews(convertView);
        onConfigViews(convertView.getContext());
    }

    public void setData(int position, BEAN bean){
        this.data = bean;
        onSetData(position, bean);
    }

    public View findViewById(int id){
        return itemView.findViewById(id);
    }

    public View findViewWithTag(Object tag){
        return itemView.findViewWithTag(tag);
    }

    protected abstract void onFindViews(View itemView);

    protected abstract void onConfigViews(Context context);

    protected abstract void onSetData(int position, BEAN bean);

    public final View getItemView(){
        return itemView;
    }

    public BEAN getData() {
        return data;
    }
}