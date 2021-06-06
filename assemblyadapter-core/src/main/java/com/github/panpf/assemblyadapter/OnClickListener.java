package com.github.panpf.assemblyadapter;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface OnClickListener<DATA> {
    void onViewClick(@NonNull Context context, @NonNull View view, int position, @Nullable DATA data);
}
