package com.github.panpf.assemblyadapter.recycler;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class AssemblyStaggeredGridLayoutManager extends StaggeredGridLayoutManager {

    public AssemblyStaggeredGridLayoutManager(@NonNull Context context, @Nullable AttributeSet attrs,
                                              int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public AssemblyStaggeredGridLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    public AssemblyStaggeredGridLayoutManager(int spanCount) {
        super(spanCount, StaggeredGridLayoutManager.VERTICAL);
    }
}
