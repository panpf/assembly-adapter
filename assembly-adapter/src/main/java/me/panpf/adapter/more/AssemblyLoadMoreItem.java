package me.panpf.adapter.more;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import me.panpf.adapter.AssemblyAdapter;
import me.panpf.adapter.AssemblyItem;

@SuppressWarnings("unused")
public abstract class AssemblyLoadMoreItem<DATA> extends AssemblyItem<DATA> implements MoreItem<DATA> {

    @NonNull
    private AssemblyLoadMoreItemFactory itemFactory;

    public AssemblyLoadMoreItem(@NonNull AssemblyLoadMoreItemFactory itemFactory, int itemLayoutId, @NonNull ViewGroup parent) {
        super(itemLayoutId, parent);
        this.itemFactory = itemFactory;
        this.itemFactory.item = this;
    }

    public AssemblyLoadMoreItem(@NonNull AssemblyLoadMoreItemFactory itemFactory, @NonNull View convertView) {
        super(convertView);
        this.itemFactory = itemFactory;
        this.itemFactory.item = this;
    }

    @Override
    public void onConfigViews(@NonNull Context context) {
        View errorView = getErrorRetryView();
        //noinspection ConstantConditions
        if (errorView != null) {
            errorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //noinspection ConstantConditions
                    if (itemFactory.listener != null) {
                        itemFactory.paused = false;
                        setData(getPosition(), getData());
                    }
                }
            });
        }
    }

    @Override
    public void onSetData(int position, @Nullable DATA data) {
        final AssemblyAdapter adapter = itemFactory.getAdapter();
        if (itemFactory.end) {
            showEnd();
        } else if (adapter != null) {
            showLoading();
            //noinspection ConstantConditions
            if (itemFactory.listener != null && !itemFactory.paused) {
                itemFactory.paused = true;
                itemFactory.listener.onLoadMore(adapter);
            }
        }
    }
}
