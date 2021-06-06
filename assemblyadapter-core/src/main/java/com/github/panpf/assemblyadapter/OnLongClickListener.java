package com.github.panpf.assemblyadapter;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface OnLongClickListener<DATA> {
    boolean onViewLongClick(@NonNull Context context, @NonNull View view, int position, @Nullable DATA data);
}
