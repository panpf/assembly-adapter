package com.github.panpf.assemblyadapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class AssemblyItem<DATA> implements Item<DATA> {

    @NonNull
    public final Context context;
    @NonNull
    public final Resources resources;
    @NonNull
    public final View itemView;

    @Nullable
    private DATA data;
    private int position = -1;

    public AssemblyItem(@NonNull View itemView) {
        this.itemView = itemView;
        this.context = itemView.getContext();
        this.resources = itemView.getResources();
    }

    public AssemblyItem(int itemLayoutId, @NonNull ViewGroup parent) {
        this(LayoutInflater.from(parent.getContext()).inflate(itemLayoutId, parent, false));
    }

    @Override
    public void dispatchBindData(int position, @Nullable DATA data) {
        this.position = position;
        this.data = data;
        bindData(position, data);
    }

    protected abstract void bindData(int position, @Nullable DATA data);

    @NonNull
    @Override
    public final View getItemView() {
        return this.itemView;
    }

    @Nullable
    @Override
    public DATA getData() {
        return data;
    }

    @Override
    public int getPosition() {
        return position;
    }
}
