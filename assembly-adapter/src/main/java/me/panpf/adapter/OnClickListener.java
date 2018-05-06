package me.panpf.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

public interface OnClickListener {
    void onViewClick(@NonNull View view, int position, int positionInPart, @Nullable Object data);
}
