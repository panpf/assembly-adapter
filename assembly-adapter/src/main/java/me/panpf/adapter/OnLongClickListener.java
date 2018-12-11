package me.panpf.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;

public interface OnLongClickListener<DATA> {
    boolean onViewLongClick(@NonNull Context context, @NonNull View view, int position, int positionInPart, @Nullable DATA data);
}
