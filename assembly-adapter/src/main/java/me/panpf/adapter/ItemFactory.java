package me.panpf.adapter;

import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public interface ItemFactory<DATA> {

    /**
     * This method is called by {@link AssemblyAdapter}
     */
    void attachToAdapter(@NonNull AssemblyAdapter adapter, int viewType);

    /**
     * 获取被包装的 {@link ItemFactory}
     */
    @NonNull
    ItemFactory<DATA> getWrappedItemFactory();

    /**
     * @deprecated Use {@link #getViewType()} instead
     */
    @Deprecated
    int getItemType();

    int getViewType();

    /**
     * 获取 {@link AssemblyAdapter}
     */
    @Nullable
    AssemblyAdapter getAdapter();

    /**
     * 获取在 {@link GridLayoutManager} 里所占的列数
     */
    int getSpanSize();

    /**
     * 设置在 {@link GridLayoutManager} 里所占的列数，不能小于 1
     */
    @NonNull
    ItemFactory<DATA> setSpanSize(int spanSize);

    /**
     * 在 {@link GridLayoutManager} 里占满一行
     *
     * @param recyclerView 需要从 {@link RecyclerView} 中取出 {@link GridLayoutManager} 再取出 SpanCount
     */
    @NonNull
    ItemFactory<DATA> fullSpan(@NonNull RecyclerView recyclerView);

    /**
     * 是否将要用在 {@link RecyclerView} 中
     */
    boolean isInRecycler();

    /**
     * 设置是否将要用在 {@link RecyclerView} 中
     */
    ItemFactory<DATA> setInRecycler(boolean inRecycler);

    /**
     * 监听指定 id 的 view 的点击事件
     *
     * @param viewId          view 的 id
     * @param onClickListener 点击监听
     */
    ItemFactory<DATA> setOnViewClickListener(@IdRes int viewId, @NonNull OnClickListener<DATA> onClickListener);

    /**
     * 监听 item 的点击事件
     *
     * @param onClickListener 点击监听
     */
    ItemFactory<DATA> setOnItemClickListener(@NonNull OnClickListener<DATA> onClickListener);

    /**
     * 监听指定 id 的 view 的长按事件
     *
     * @param viewId          view 的 id
     * @param onClickListener 长按监听
     */
    ItemFactory<DATA> setOnViewLongClickListener(@IdRes int viewId, @NonNull OnLongClickListener<DATA> onClickListener);

    /**
     * 监听 item 的长按事件
     *
     * @param onClickListener 长按监听
     */
    ItemFactory<DATA> setOnItemLongClickListener(@NonNull OnLongClickListener<DATA> onClickListener);

    /**
     * 匹配数据
     *
     * @param data 待匹配的数据，通常是使用 instanceof 关键字匹配类型
     * @return 如果返回 true，{@link AssemblyAdapter} 将会使用此 {@link ItemFactory} 来处理当前这条数据
     */
    boolean match(@Nullable Object data);

    @NonNull
    Item<DATA> dispatchCreateItem(@NonNull ViewGroup parent);
}