package me.panpf.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

@SuppressWarnings({"unused", "WeakerAccess"})
public class ItemActor {
    /*
     * 为什么不把这里面的方法放在 ItemStorage 中，然后直接访问里面的字段，非要通过 AssemblyAdapter 来访问？
     * 因为模仿 PagingListAdapter 实现 AssemblyRecyclerPageListAdapter 时需要重写 getDataCount() 和 getData() 方法
     * 但是无法直接继承 ItemStorage 重写其方法，只能继承 AssemblyRecyclerAdapter 所以不得已才这样做
     */

    @NonNull
    private AssemblyAdapter adapter;

    public ItemActor(@NonNull AssemblyAdapter adapter) {
        this.adapter = adapter;
    }

    /* ************************ 完整列表 *************************** */

    public int getItemCount() {
        int headerItemCount = adapter.getHeaderItemCount();
        int dataCount = adapter.getDataCount();
        int footerItemCount = adapter.getFooterItemCount();

        if (dataCount > 0) {
            return headerItemCount + dataCount + footerItemCount + (adapter.hasMoreFooter() ? 1 : 0);
        } else {
            return headerItemCount + footerItemCount;
        }
    }

    @Nullable
    public Object getItem(int position) {
        // 头
        int headerItemCount = adapter.getHeaderItemCount();
        int headerStartPosition = 0;
        int headerEndPosition = headerItemCount - 1;
        if (position >= headerStartPosition && position <= headerEndPosition && headerItemCount > 0) {
            //noinspection UnnecessaryLocalVariable
            int positionInHeaderList = position;
            return adapter.getHeaderData(positionInHeaderList);
        }

        // 数据
        int dataCount = adapter.getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        if (position >= dataStartPosition && position <= dataEndPosition && dataCount > 0) {
            int positionInDataList = position - headerItemCount;
            return adapter.getData(positionInDataList);
        }

        // 尾巴
        int footerItemCount = adapter.getFooterItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        if (position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0) {
            int positionInFooterList = position - headerItemCount - dataCount;
            return adapter.getFooterData(positionInFooterList);
        }

        // 加载更多尾巴
        if (dataCount > 0 && adapter.hasMoreFooter() && position == getItemCount() - 1) {
            ItemHolder moreItemHolder = adapter.getMoreItemHolder();
            return moreItemHolder != null ? moreItemHolder.getData() : null;
        }

        return null;
    }

    /**
     * 获取在各自区域的位置
     */
    public int getPositionInPart(int position) {
        // 头
        int headerItemCount = adapter.getHeaderItemCount();
        int headerStartPosition = 0;
        int headerEndPosition = headerItemCount - 1;
        if (position >= headerStartPosition && position <= headerEndPosition && headerItemCount > 0) {
            return position;
        }

        // 数据
        int dataCount = adapter.getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        if (position >= dataStartPosition && position <= dataEndPosition && dataCount > 0) {
            return position - headerItemCount;
        }

        // 尾巴
        int footerItemCount = adapter.getFooterItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        if (position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0) {
            return position - headerItemCount - dataCount;
        }

        // 加载更多尾巴
        if (dataCount > 0 && adapter.hasMoreFooter() && position == adapter.getItemCount() - 1) {
            return 0;
        }

        throw new IllegalArgumentException("Illegal position: " + position);
    }


    /* ************************ 其它 *************************** */

    /**
     * 获取指定位置占几列
     */
    public int getSpanSize(int position) {
        // 头
        int headerItemCount = adapter.getHeaderItemCount();
        int headerStartPosition = 0;
        int headerEndPosition = headerItemCount - 1;
        List<ItemHolder> headerItemList = adapter.getHeaderItemList();
        if (headerItemList != null && position >= headerStartPosition && position <= headerEndPosition && headerItemCount > 0) {
            //noinspection UnnecessaryLocalVariable
            int positionInHeaderList = position;
            return headerItemList.get(positionInHeaderList).getItemFactory().getSpanSize();
        }

        // 数据
        int dataCount = adapter.getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        List<ItemFactory> itemFactoryList = adapter.getItemFactoryList();
        if (itemFactoryList != null && position >= dataStartPosition && position <= dataEndPosition && dataCount > 0) {
            int positionInDataList = position - headerItemCount;
            Object dataObject = adapter.getData(positionInDataList);

            ItemFactory itemFactory;
            for (int w = 0, size = itemFactoryList.size(); w < size; w++) {
                itemFactory = itemFactoryList.get(w);
                if (itemFactory.match(dataObject)) {
                    return itemFactory.getSpanSize();
                }
            }

            throw new IllegalStateException(String.format("Didn't find suitable ItemFactory. positionInDataList=%d, dataObject=%s",
                    positionInDataList, dataObject != null ? dataObject.getClass().getName() : null));
        }

        // 尾巴
        int footerItemCount = adapter.getFooterItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        List<ItemHolder> footerItemList = adapter.getHeaderItemList();
        if (footerItemList != null && position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0) {
            int positionInFooterList = position - headerItemCount - dataCount;
            return footerItemList.get(positionInFooterList).getItemFactory().getSpanSize();
        }

        // 加载更多尾巴
        ItemHolder moreItemHolder = adapter.getMoreItemHolder();
        if (moreItemHolder != null && dataCount > 0 && adapter.hasMoreFooter() && position == getItemCount() - 1) {
            return moreItemHolder.getItemFactory().getSpanSize();
        }

        return 1;
    }

    public int getItemViewType(int position) {
        int headerItemCount = adapter.getHeaderItemCount();
        int headerStartPosition = 0;
        int headerEndPosition = headerItemCount - 1;

        // 头
        List<ItemHolder> headerItemList = adapter.getHeaderItemList();
        if (headerItemList != null && position >= headerStartPosition && position <= headerEndPosition && headerItemCount > 0) {
            //noinspection UnnecessaryLocalVariable
            int positionInHeaderList = position;
            return headerItemList.get(positionInHeaderList).getItemFactory().getItemType();
        }

        // 数据
        List<ItemFactory> itemFactoryList = adapter.getItemFactoryList();
        int dataCount = adapter.getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        if (itemFactoryList != null && position >= dataStartPosition && position <= dataEndPosition && dataCount > 0) {
            int positionInDataList = position - headerItemCount;
            Object dataObject = adapter.getData(positionInDataList);

            ItemFactory itemFactory;
            for (int w = 0, size = itemFactoryList.size(); w < size; w++) {
                itemFactory = itemFactoryList.get(w);
                if (itemFactory.match(dataObject)) {
                    return itemFactory.getItemType();
                }
            }

            throw new IllegalStateException(String.format("Didn't find suitable ItemFactory. positionInDataList=%d, dataObject=%s",
                    positionInDataList, dataObject != null ? dataObject.toString() : null));
        }

        // 尾巴
        List<ItemHolder> footerItemList = adapter.getFooterItemList();
        int footerItemCount = adapter.getFooterItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        if (footerItemList != null && position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0) {
            int positionInFooterList = position - headerItemCount - dataCount;
            return footerItemList.get(positionInFooterList).getItemFactory().getItemType();
        }

        // 加载更多尾巴
        ItemHolder moreItemHolder = adapter.getMoreItemHolder();
        if (moreItemHolder != null && dataCount > 0 && adapter.hasMoreFooter() && position == getItemCount() - 1) {
            return moreItemHolder.getItemFactory().getItemType();
        }

        throw new IllegalStateException("Not found match viewType, position: " + position);
    }
}
