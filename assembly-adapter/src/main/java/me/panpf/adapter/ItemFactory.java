package me.panpf.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface ItemFactory<ITEM extends Item> {
    /**
     * 获取 item 类型
     */
    int getItemType();

    /**
     * 设置 item 类型，此方法由 {@link AssemblyAdapter} 调用
     */
    void setItemType(int itemType);

    /**
     * 获取 {@link AssemblyAdapter}
     */
    @Nullable
    AssemblyAdapter getAdapter();

    /**
     * 设置 {@link AssemblyAdapter}，此方法由 {@link AssemblyAdapter} 调用
     */
    void setAdapter(@NonNull AssemblyAdapter adapter);

    /**
     * 获取在 {@link GridLayoutManager} 里所占的列数
     */
    int getSpanSize();

    /**
     * 设置在 {@link GridLayoutManager} 里所占的列数，不能小于 1
     */
    @NonNull
    ItemFactory<ITEM> setSpanSize(int spanSize);

    /**
     * 在 {@link GridLayoutManager} 里占满一行
     *
     * @param recyclerView 需要从 {@link RecyclerView} 中取出 {@link GridLayoutManager} 再取出 SpanCount
     */
    @NonNull
    ItemFactory<ITEM> fullSpan(@NonNull RecyclerView recyclerView);

    /**
     * 匹配数据
     *
     * @param data 待匹配的数据，通常是使用 instanceof 关键字匹配类型
     * @return 如果返回 true，{@link AssemblyAdapter} 将会使用此 {@link ItemFactory} 来处理当前这条数据
     */
    boolean isTarget(@NonNull Object data);

    @NonNull
    ITEM dispatchCreateItem(@NonNull ViewGroup parent);
}