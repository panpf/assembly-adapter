package me.panpf.adapter;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import me.panpf.adapter.recycler.RecyclerItemWrapper;

@SuppressWarnings("unused")
public abstract class AssemblyItemFactory<DATA> implements ItemFactory<DATA> {

    private int itemType;
    @Nullable
    private AssemblyAdapter adapter;

    private int spanSize = 1;
    private boolean fullSpanInStaggeredGrid;
    private boolean inRecycler;
    @Nullable
    private ClickListenerManager<DATA> clickListenerManager;

    @NonNull
    @Override
    public ItemFactory<DATA> getWrappedItemFactory() {
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
    public AssemblyItemFactory<DATA> setSpanSize(int spanSize) {
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
    public AssemblyItemFactory<DATA> setInRecycler(boolean inRecycler) {
        this.inRecycler = inRecycler;
        return this;
    }

    @Override
    public AssemblyItemFactory<DATA> setOnViewClickListener(@IdRes int viewId, @NonNull OnClickListener<DATA> onClickListener) {
        if (clickListenerManager == null) {
            clickListenerManager = new ClickListenerManager<DATA>();
        }
        clickListenerManager.add(viewId, onClickListener);
        return this;
    }

    @Override
    public AssemblyItemFactory<DATA> setOnItemClickListener(@NonNull OnClickListener<DATA> onClickListener) {
        if (clickListenerManager == null) {
            clickListenerManager = new ClickListenerManager<DATA>();
        }
        clickListenerManager.add(onClickListener);
        return this;
    }

    @Override
    public AssemblyItemFactory<DATA> setOnViewLongClickListener(@IdRes int viewId, @NonNull OnLongClickListener<DATA> onClickListener) {
        if (clickListenerManager == null) {
            clickListenerManager = new ClickListenerManager<DATA>();
        }
        clickListenerManager.add(viewId, onClickListener);
        return this;
    }

    @Override
    public AssemblyItemFactory<DATA> setOnItemLongClickListener(@NonNull OnLongClickListener<DATA> onClickListener) {
        if (clickListenerManager == null) {
            clickListenerManager = new ClickListenerManager<DATA>();
        }
        clickListenerManager.add(onClickListener);
        return this;
    }

    @NonNull
    @Override
    public AssemblyItemFactory<DATA> fullSpan(@NonNull RecyclerView recyclerView) {
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
    public Item<DATA> dispatchCreateItem(@NonNull ViewGroup parent) {
        @NonNull
        Item<DATA> item = createAssemblyItem(parent);

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

        Item<DATA> finalItem = inRecycler ? new RecyclerItemWrapper<DATA>(item) : item;

        registerListeners(finalItem);

        return finalItem;
    }

    private void registerListeners(Item<DATA> item) {
        if (clickListenerManager != null) {
            clickListenerManager.register(this, item, item.getItemView());
        }
    }

    /**
     * 创建 {@link AssemblyItem}
     */
    @NonNull
    public abstract AssemblyItem<DATA> createAssemblyItem(@NonNull ViewGroup parent);
}
