package me.panpf.adapter.more;

import androidx.annotation.NonNull;

import me.panpf.adapter.AssemblyAdapter;

@SuppressWarnings("unused")
public interface OnLoadMoreListener {
    void onLoadMore(@NonNull AssemblyAdapter adapter);
}
