package com.github.panpf.assemblyadapter.recycler;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.panpf.assemblyadapter.ItemFactory;

import java.util.Map;

public class AssemblyGridLayoutManager extends GridLayoutManager {

    @Nullable
    private RecyclerView recyclerView;

    public AssemblyGridLayoutManager(@NonNull Context context, @Nullable AttributeSet attrs,
                                     int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setSpanSizeLookup(new AssemblySpanSizeLookup(this));
    }

    public AssemblyGridLayoutManager(@NonNull Context context, int spanCount, int orientation,
                                     boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
        setSpanSizeLookup(new AssemblySpanSizeLookup(this));
    }

    public AssemblyGridLayoutManager(@NonNull Context context, int spanCount) {
        super(context, spanCount);
        setSpanSizeLookup(new AssemblySpanSizeLookup(this));
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        this.recyclerView = view;
    }

    @Nullable
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public static class AssemblySpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

        @NonNull
        private final AssemblyGridLayoutManager assemblyGridLayoutManager;

        public AssemblySpanSizeLookup(@NonNull AssemblyGridLayoutManager assemblyGridLayoutManager) {
            this.assemblyGridLayoutManager = assemblyGridLayoutManager;
        }

        @Override
        public int getSpanSize(int position) {
            RecyclerView recyclerView = assemblyGridLayoutManager.getRecyclerView();
            if (recyclerView != null) {
                RecyclerView.Adapter<?> adapter = recyclerView.getAdapter();
                if (adapter instanceof AssemblyRecyclerAdapter<?>) {
                    AssemblyRecyclerAdapter<?> assemblyRecyclerAdapter = (AssemblyRecyclerAdapter<?>) adapter;
                    Map<Class<? extends ItemFactory<?>>, ItemSpan> itemSpanMap = assemblyRecyclerAdapter.getItemSpanMapInGridLayoutManager();
                    if (itemSpanMap != null && !itemSpanMap.isEmpty()) {
                        ItemFactory<?> itemFactory = assemblyRecyclerAdapter.getItemFactoryByPosition(position);
                        ItemSpan itemSpan = itemSpanMap.get(itemFactory.getClass());
                        int spanSize = itemSpan != null ? itemSpan.span : 1;
                        if (spanSize < 0) {
                            return assemblyGridLayoutManager.getSpanCount();
                        } else {
                            return Math.max(spanSize, 1);
                        }
                    }
                }
            }
            return 1;
        }
    }
}
