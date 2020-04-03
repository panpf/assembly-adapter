package me.panpf.adapter;

import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import me.panpf.adapter.recycler.RecyclerItemWrapper;

public abstract class AssemblyItemFactory<DATA> implements ItemFactory<DATA> {

    private int viewType;
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
    public void attachToAdapter(@NonNull AssemblyAdapter adapter, int viewType) {
        this.adapter = adapter;
        this.viewType = viewType;
    }

    /**
     * @deprecated Use {@link #getViewType()} instead
     */
    @Deprecated
    public int getItemType() {
        return viewType;
    }

    @Override
    public int getViewType() {
        return viewType;
    }

    @Nullable
    @Override
    public AssemblyAdapter getAdapter() {
        return adapter;
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
            clickListenerManager = new ClickListenerManager<>();
        }
        clickListenerManager.add(viewId, onClickListener);
        return this;
    }

    @Override
    public AssemblyItemFactory<DATA> setOnItemClickListener(@NonNull OnClickListener<DATA> onClickListener) {
        if (clickListenerManager == null) {
            clickListenerManager = new ClickListenerManager<>();
        }
        clickListenerManager.add(onClickListener);
        return this;
    }

    @Override
    public AssemblyItemFactory<DATA> setOnViewLongClickListener(@IdRes int viewId, @NonNull OnLongClickListener<DATA> onClickListener) {
        if (clickListenerManager == null) {
            clickListenerManager = new ClickListenerManager<>();
        }
        clickListenerManager.add(viewId, onClickListener);
        return this;
    }

    @Override
    public AssemblyItemFactory<DATA> setOnItemLongClickListener(@NonNull OnLongClickListener<DATA> onClickListener) {
        if (clickListenerManager == null) {
            clickListenerManager = new ClickListenerManager<>();
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
            if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
                item.getItemView().setLayoutParams(layoutParams);
            }
        }

        item.onInit(parent.getContext());

        Item<DATA> finalItem = inRecycler ? new RecyclerItemWrapper<>(item) : item;

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
