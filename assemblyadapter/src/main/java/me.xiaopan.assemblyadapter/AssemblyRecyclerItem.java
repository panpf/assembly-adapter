package me.xiaopan.assemblyadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class AssemblyRecyclerItem<BEAN> extends RecyclerView.ViewHolder {
    private BEAN data;
    private int positionInAdapter;
    private ContentSetter setter;

    public AssemblyRecyclerItem(int itemLayoutId, ViewGroup parent) {
        this(LayoutInflater.from(parent.getContext()).inflate(itemLayoutId, parent, false));
    }

    public AssemblyRecyclerItem(View itemView) {
        super(itemView);
        onFindViews();
        onConfigViews(itemView.getContext());
    }

    void setPositionInAdapter(int positionInAdapter) {
        this.positionInAdapter = positionInAdapter;
    }

    public void setData(int position, BEAN bean) {
        this.data = bean;
        onSetData(position, bean);
    }

    public View findViewById(int id) {
        return itemView.findViewById(id);
    }

    @SuppressWarnings("unused")
    public View findViewWithTag(Object tag) {
        return itemView.findViewWithTag(tag);
    }

    protected abstract void onFindViews();

    protected abstract void onConfigViews(Context context);

    protected abstract void onSetData(int position, BEAN bean);

    @SuppressWarnings("unused")
    public final View getItemView() {
        return itemView;
    }

    public BEAN getData() {
        return data;
    }

    @SuppressWarnings("unused")
    public int getPositionInAdapter() {
        return positionInAdapter;
    }

    @SuppressWarnings("unused")
    public ContentSetter getSetter() {
        if (setter == null) {
            setter = new ContentSetter(itemView);
        }
        return setter;
    }
}