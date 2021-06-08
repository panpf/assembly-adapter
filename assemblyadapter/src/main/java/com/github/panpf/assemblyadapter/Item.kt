package com.github.panpf.assemblyadapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface Item<DATA> {

    void dispatchBindData(int position, @Nullable DATA data);

    @Nullable
    DATA getData();

    @NonNull
    View getItemView();

    int getPosition();

    // todo getPosition 、getAdapterPosition、getLayoutPosition、getBindingAdapterPosition、getAbsoluteAdapterPosition
}