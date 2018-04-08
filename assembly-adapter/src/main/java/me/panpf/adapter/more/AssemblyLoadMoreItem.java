package me.panpf.adapter.more;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import me.panpf.adapter.AssemblyAdapter;
import me.panpf.adapter.AssemblyItem;

@SuppressWarnings("unused")
public abstract class AssemblyLoadMoreItem<DATA> extends AssemblyItem<DATA> implements LoadMoreItemBridle<DATA> {
    private AssemblyLoadMoreItemFactory assemblyLoadMoreItemFactory;

    public AssemblyLoadMoreItem(AssemblyLoadMoreItemFactory assemblyLoadMoreItemFactory, int itemLayoutId, ViewGroup parent) {
        super(itemLayoutId, parent);
        this.assemblyLoadMoreItemFactory = assemblyLoadMoreItemFactory;
        assemblyLoadMoreItemFactory.assemblyLoadMoreItem = this;
    }

    public AssemblyLoadMoreItem(AssemblyLoadMoreItemFactory assemblyLoadMoreItemFactory, View convertView) {
        super(convertView);
        this.assemblyLoadMoreItemFactory = assemblyLoadMoreItemFactory;
        assemblyLoadMoreItemFactory.assemblyLoadMoreItem = this;
    }

    @Override
    public void onConfigViews(@NonNull Context context) {
        View errorView = getErrorRetryView();
        //noinspection ConstantConditions
        if (errorView != null) {
            errorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (assemblyLoadMoreItemFactory.eventListener != null) {
                        assemblyLoadMoreItemFactory.paused = false;
                        setData(getPosition(), getData());
                    }
                }
            });
        }
    }

    @Override
    public void onSetData(int position, @Nullable DATA DATA) {
        final AssemblyAdapter adapter = assemblyLoadMoreItemFactory.getAdapter();
        if (assemblyLoadMoreItemFactory.end) {
            showEnd();
        } else if (adapter != null) {
            showLoading();
            if (assemblyLoadMoreItemFactory.eventListener != null && !assemblyLoadMoreItemFactory.paused) {
                assemblyLoadMoreItemFactory.paused = true;
                assemblyLoadMoreItemFactory.eventListener.onLoadMore(adapter);
            }
        }
    }
}
