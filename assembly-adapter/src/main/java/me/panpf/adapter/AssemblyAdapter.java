package me.panpf.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import me.panpf.adapter.more.AssemblyLoadMoreItem;
import me.panpf.adapter.more.AssemblyLoadMoreItemFactory;
import me.panpf.adapter.more.LoadMoreFixedItemInfo;

/**
 * 组合式 Adapter，支持多 item 和加载更多功能。故名思议可以通过组合不同的 {@link ItemFactory} 来快捷的支持多 item
 */
@SuppressWarnings("unused")
public interface AssemblyAdapter {

    /* ************************ 数据 ItemFactory *************************** */

    /**
     * 添加一个用来处理并显示 dataList 中的数据的 {@link AssemblyItemFactory}
     *
     * @param itemFactory 用来匹配并根据数据创建 item view
     */
    <ITEM extends AssemblyItem> void addItemFactory(@NonNull AssemblyItemFactory<ITEM> itemFactory);

    /**
     * 获取数据  {@link ItemFactory} 列表
     */
    @Nullable
    List<ItemFactory> getItemFactoryList();

    /**
     * 获取数据  {@link ItemFactory} 的个数
     */
    int getItemFactoryCount();


    /* ************************ 头部 ItemFactory *************************** */

    /**
     * 添加一个将按添加顺序显示在列表头部的 {@link AssemblyItemFactory}
     */
    @NonNull
    <ITEM extends AssemblyItem> FixedItemInfo addHeaderItem(@NonNull AssemblyItemFactory<ITEM> headerFactory, @Nullable Object data);

    /**
     * header 状态变化处理，不可用时从 header 列表中移除，可用时加回 header 列表中，并根据 position 排序来恢复其原本所在的位置
     */
    void headerEnabledChanged(@NonNull FixedItemInfo headerItemInfo);

    /**
     * 获取 header 列表
     */
    @Nullable
    List<FixedItemInfo> getHeaderItemList();

    /**
     * 获取列表头的个数
     */
    int getHeaderItemCount();

    /**
     * 获取头部指定位置的数据
     *
     * @param positionInHeaderList 在头部的位置
     */
    @Nullable
    Object getHeaderData(int positionInHeaderList);


    /* ************************ 尾巴 ItemFactory *************************** */

    /**
     * 添加一个将按添加顺序显示在列表尾部的 {@link AssemblyItemFactory}
     */
    @NonNull
    <ITEM extends AssemblyItem> FixedItemInfo addFooterItem(@NonNull AssemblyItemFactory<ITEM> footerFactory, @Nullable Object data);

    /**
     * footer 状态变化处理，不可用时从 footer 列表中移除，可用时加回 footer 列表中，并根据 position 排序来恢复其原本所在的位置
     */
    void footerEnabledChanged(@NonNull FixedItemInfo footerItemInfo);

    /**
     * 获取 footer 列表
     */
    @Nullable
    List<FixedItemInfo> getFooterItemList();

    /**
     * 获取列表头的个数
     */
    int getFooterItemCount();

    /**
     * 获取尾部指定位置的数据
     *
     * @param positionInFooterList 在尾部的位置
     */
    @Nullable
    Object getFooterData(int positionInFooterList);


    /* ************************ 加载更多 *************************** */

    /**
     * 设置一个将显示在列表最后（在 footer 的后面）的加载更多尾巴
     */
    @NonNull
    <ITEM extends AssemblyLoadMoreItem> LoadMoreFixedItemInfo setLoadMoreItem(@NonNull AssemblyLoadMoreItemFactory<ITEM> assemblyLoadMoreItemFactory, @Nullable Object data);

    /**
     * 设置一个将显示在列表最后（在 footer 的后面）的加载更多尾巴
     */
    @NonNull
    <ITEM extends AssemblyLoadMoreItem> LoadMoreFixedItemInfo setLoadMoreItem(@NonNull AssemblyLoadMoreItemFactory<ITEM> assemblyLoadMoreItemFactory);

    /**
     * 设置禁用加载更多
     */
    void setDisableLoadMore(boolean disableLoadMore);

    /**
     * 是否有加载更多尾巴
     */
    boolean hasLoadMoreFooter();

    /**
     * 加载更多完成时调用
     *
     * @param loadMoreEnd 全部加载完毕，为 true 会显示结束的文案并且不再触发加载更多
     */
    void loadMoreFinished(boolean loadMoreEnd);

    /**
     * 加载更多失败的时候调用此方法显示错误提示，并可点击重新加载
     */
    void loadMoreFailed();


    /* ************************ 数据列表 *************************** */

    /**
     * 获取数据列表
     */
    @Nullable
    List getDataList();

    /**
     * 设置数据列表
     */
    void setDataList(@Nullable List dataList);

    /**
     * 批量添加数据
     */
    void addAll(@NonNull Collection collection);

    /**
     * 批量添加数据
     */
    void addAll(@NonNull Object... items);

    /**
     * 插入一条数据
     */
    void insert(@NonNull Object object, int index);

    /**
     * 删除一条数据
     */
    void remove(@NonNull Object object);

    /**
     * 清空数据
     */
    void clear();

    /**
     * 对数据排序
     */
    void sort(@NonNull Comparator comparator);

    /**
     * 获取数据列表的长度
     */
    int getDataCount();

    /**
     * 获取数据列表中指定位置的数据
     *
     * @param positionInDataList 数据列表中的位置
     */
    @Nullable
    Object getData(int positionInDataList);


    /* ************************ 完整列表 *************************** */

    /**
     * 获取列表的长度
     */
    int getItemCount();

    /**
     * 获取指定位置的数据
     *
     * @param position 位置
     */
    @Nullable
    Object getItem(int position);

    /**
     * 获取在各自区域的位置
     */
    int getPositionInPart(int position);


    /* ************************ 其它 *************************** */

    /**
     * 数据变更时是否立即刷新列表
     */
    boolean isNotifyOnChange();

    /**
     * 设置当数据发生改变时是否立即调用 notifyDataSetChanged() 刷新列表，默认 true。
     * 当你需要连续多次修改数据的时候，你应该将 notifyOnChange 设为 false，然后在最后主动调用 notifyDataSetChanged() 刷新列表，最后再将 notifyOnChange 设为 true
     */
    void setNotifyOnChange(boolean notifyOnChange);

    /**
     * 刷新列表
     */
    void notifyDataSetChanged();

    /**
     * 获取指定位置占几列，专为 {@link android.support.v7.widget.RecyclerView} 预留
     */
    int getSpanSize(int position);
}