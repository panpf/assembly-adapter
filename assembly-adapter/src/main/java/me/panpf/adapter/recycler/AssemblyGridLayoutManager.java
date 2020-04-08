package me.panpf.adapter.recycler;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AssemblyGridLayoutManager extends GridLayoutManager {

    public AssemblyGridLayoutManager(@NonNull Context context, @NonNull AttributeSet attrs,
                                     int defStyleAttr, int defStyleRes, @NonNull RecyclerView recyclerView) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setSpanSizeLookup(new AssemblySpanSizeLookup(recyclerView));
    }

    public AssemblyGridLayoutManager(@NonNull Context context, int spanCount, @NonNull RecyclerView recyclerView) {
        super(context, spanCount);
        setSpanSizeLookup(new AssemblySpanSizeLookup(recyclerView));
    }

    public AssemblyGridLayoutManager(@NonNull Context context, int spanCount, int orientation,
                                     boolean reverseLayout, @NonNull RecyclerView recyclerView) {
        super(context, spanCount, orientation, reverseLayout);
        setSpanSizeLookup(new AssemblySpanSizeLookup(recyclerView));
    }
}
