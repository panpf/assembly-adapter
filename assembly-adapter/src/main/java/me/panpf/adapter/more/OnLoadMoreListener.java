package me.panpf.adapter.more;

import android.support.annotation.NonNull;

import me.panpf.adapter.AssemblyAdapter;

@SuppressWarnings("unused")
public interface OnLoadMoreListener {
    void onLoadMore(@NonNull AssemblyAdapter adapter);
}
