package me.panpf.adapter.recycler;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import me.panpf.adapter.Item;
import me.panpf.adapter.more.AssemblyLoadMoreItem;
import me.panpf.adapter.more.LoadMoreItemBridle;

@SuppressWarnings("WeakerAccess")
public class RecyclerLoadMoreItemWrapper<DATA> extends RecyclerView.ViewHolder implements LoadMoreItemBridle<DATA> {

    @NonNull
    private AssemblyLoadMoreItem<DATA> item;

    public RecyclerLoadMoreItemWrapper(@NonNull AssemblyLoadMoreItem<DATA> item) {
        super(item.getItemView());
        this.item = item;
    }

    @Override
    public void setWrapper(@Nullable Item wrapper) {
        // 不可以重复 wrapper
    }

    @NonNull
    @Override
    public DATA getData() {
        return item.getData();
    }

    @Override
    public void setData(int position, @NonNull DATA data) {
        item.setData(position, data);
    }

    @NonNull
    @Override
    public View getItemView() {
        return item.getItemView();
    }

    @Override
    public boolean isExpanded() {
        return item.isExpanded();
    }

    @Override
    public void setExpanded(boolean expanded) {
        item.setExpanded(expanded);
    }

    @Override
    public int getGroupPosition() {
        return item.getGroupPosition();
    }

    @Override
    public void setGroupPosition(int groupPosition) {
        item.setGroupPosition(groupPosition);
    }

    @Override
    public boolean isLastChild() {
        return item.isLastChild();
    }

    @Override
    public void setLastChild(boolean lastChild) {
        item.setLastChild(lastChild);
    }

    @NonNull
    @Override
    public View getErrorRetryView() {
        return item.getErrorRetryView();
    }

    @Override
    public void showLoading() {
        item.showLoading();
    }

    @Override
    public void showErrorRetry() {
        item.showErrorRetry();
    }

    @Override
    public void showEnd() {
        item.showEnd();
    }
}
