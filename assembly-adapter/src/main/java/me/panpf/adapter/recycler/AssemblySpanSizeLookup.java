package me.panpf.adapter.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import me.panpf.adapter.AssemblyAdapter;

@SuppressWarnings("WeakerAccess")
public class AssemblySpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

    @NonNull
    private RecyclerView recyclerView;

    public AssemblySpanSizeLookup(@NonNull RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public int getSpanSize(int position) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter instanceof AssemblyAdapter) {
            return ((AssemblyAdapter) adapter).getSpanSize(position);
        }
        return 1;
    }
}
