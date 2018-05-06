package me.panpf.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import me.panpf.adapter.recycler.RecyclerItemWrapper;

@SuppressWarnings("unused")
public abstract class AssemblyItemFactory<ITEM extends Item> implements ItemFactory<ITEM> {

    private int itemType;
    private AssemblyAdapter adapter;

    private int spanSize = 1;
    private boolean fullSpanInStaggeredGrid;
    private boolean inRecycler;

    @NonNull
    @Override
    public ItemFactory<ITEM> getWrappedItemFactory() {
        return this;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    @Override
    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    @Nullable
    public AssemblyAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void setAdapter(@NonNull AssemblyAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getSpanSize() {
        return spanSize;
    }

    @NonNull
    @Override
    public AssemblyItemFactory<ITEM> setSpanSize(int spanSize) {
        if (spanSize > 0) {
            this.spanSize = spanSize;
        }
        return this;
    }

    @Override
    public boolean isInRecycler() {
        return inRecycler;
    }

    @Override
    public AssemblyItemFactory<ITEM> setInRecycler(boolean inRecycler) {
        this.inRecycler = inRecycler;
        return this;
    }

    @NonNull
    @Override
    public AssemblyItemFactory<ITEM> fullSpan(@NonNull RecyclerView recyclerView) {
        setSpanSize(1);
        fullSpanInStaggeredGrid = false;

        //noinspection ConstantConditions
        if (recyclerView != null) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                setSpanSize(gridLayoutManager.getSpanCount());
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                fullSpanInStaggeredGrid = true;
            }
        }
        return this;
    }

    @NonNull
    @Override
    public ITEM dispatchCreateItem(@NonNull ViewGroup parent) {
        @NonNull
        ITEM item = createAssemblyItem(parent);

        if (fullSpanInStaggeredGrid) {
            ViewGroup.LayoutParams layoutParams = item.getItemView().getLayoutParams();
            if (layoutParams != null && layoutParams instanceof RecyclerView.LayoutParams) {
                RecyclerView.LayoutParams recyclerLayoutParams = (RecyclerView.LayoutParams) item.getItemView().getLayoutParams();
                if (recyclerLayoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                    StaggeredGridLayoutManager.LayoutParams staggeredGridLayoutParams = (StaggeredGridLayoutManager.LayoutParams) recyclerLayoutParams;
                    staggeredGridLayoutParams.setFullSpan(true);
                    item.getItemView().setLayoutParams(recyclerLayoutParams);
                }
            }
        }
        item.onInit(parent.getContext());
        return inRecycler ? (ITEM) new RecyclerItemWrapper(item) : item;
    }

    private void registerListeners() {

    }

    /**
     * 创建 {@link AssemblyItem}
     */
    @NonNull
    public abstract ITEM createAssemblyItem(@NonNull ViewGroup parent);
}
