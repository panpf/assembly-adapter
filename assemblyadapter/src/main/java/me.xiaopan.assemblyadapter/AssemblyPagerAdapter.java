package me.xiaopan.assemblyadapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AssemblyPagerAdapter extends PagerAdapter {
    private static final String TAG = "AssemblyPagerAdapter";
    private List dataList;

    private boolean itemFactoryLocked;
    private ArrayList<FixedPagerItemInfo> headerItemList;
    private ArrayList<FixedPagerItemInfo> footerItemList;
    private ArrayList<AssemblyPagerItemFactory> itemFactoryList;

    @SuppressWarnings("unused")
    public AssemblyPagerAdapter(List dataList) {
        this.dataList = dataList;
    }

    @SuppressWarnings("unused")
    public AssemblyPagerAdapter(FragmentManager fm, Object[] dataArray) {
        if (dataArray != null && dataArray.length > 0) {
            this.dataList = new ArrayList(dataArray.length);
            Collections.addAll(dataList, dataArray);
        }
    }

    /**
     * 添加一个将按添加顺序显示在列表头部的AssemblyPagerItemFactory
     */
    @SuppressWarnings("unused")
    public void addHeaderItem(AssemblyPagerItemFactory headerFactory, Object data) {
        if (headerFactory == null || itemFactoryLocked) {
            Log.w(TAG, "headerFactory is nll or locked");
            return;
        }

        if (headerItemList == null) {
            headerItemList = new ArrayList<FixedPagerItemInfo>(2);
        }
        headerItemList.add(new FixedPagerItemInfo(headerFactory, data));
    }

    @SuppressWarnings("unused")
    public void addItemFactory(AssemblyPagerItemFactory itemFactory) {
        if (itemFactory == null || itemFactoryLocked) {
            Log.w(TAG, "itemFactory is nll or locked");
        }

        if (itemFactoryList == null) {
            itemFactoryList = new ArrayList<AssemblyPagerItemFactory>(2);
        }
        itemFactoryList.add(itemFactory);
    }

    /**
     * 添加一个将按添加顺序显示在列表尾部的AssemblyPagerItemFactory
     */
    @SuppressWarnings("unused")
    public void addFooterItem(AssemblyPagerItemFactory footerFactory, Object data) {
        if (footerFactory == null || itemFactoryLocked) {
            Log.w(TAG, "footerFactory is nll or locked");
            return;
        }

        if (footerItemList == null) {
            footerItemList = new ArrayList<FixedPagerItemInfo>(2);
        }
        footerItemList.add(new FixedPagerItemInfo(footerFactory, data));
    }

    /**
     * 获取Header列表
     */
    @SuppressWarnings("unused")
    public List<FixedPagerItemInfo> getHeaderItemList() {
        return headerItemList;
    }

    /**
     * 获取ItemFactory列表
     */
    @SuppressWarnings("unused")
    public List<AssemblyPagerItemFactory> getItemFactoryList() {
        return itemFactoryList;
    }

    /**
     * 获取Footer列表
     */
    @SuppressWarnings("unused")
    public List<FixedPagerItemInfo> getFooterItemList() {
        return footerItemList;
    }

    /**
     * 获取数据列表
     */
    @SuppressWarnings("unused")
    public List getDataList() {
        return dataList;
    }

    /**
     * 获取列表头的个数
     */
    public int getHeaderItemCount() {
        return headerItemList != null ? headerItemList.size() : 0;
    }

    /**
     * 获取ItemFactory的个数
     */
    public int getItemFactoryCount() {
        return itemFactoryList != null ? itemFactoryList.size() : 0;
    }

    /**
     * 获取列表头的个数
     */
    public int getFooterItemCount() {
        return footerItemList != null ? footerItemList.size() : 0;
    }

    /**
     * 获取数据列表的长度
     */
    public int getDataCount() {
        return dataList != null ? dataList.size() : 0;
    }

    /**
     * 获取在各自区域的位置
     */
    @SuppressWarnings("unused")
    public int getPositionInPart(int position) {
        // 头
        int headerItemCount = getHeaderItemCount();
        int headerStartPosition = 0;
        int headerEndPosition = headerItemCount - 1;
        if (position >= headerStartPosition && position <= headerEndPosition && headerItemCount > 0) {
            return position;
        }

        // 数据
        int dataCount = getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        if (position >= dataStartPosition && position <= dataEndPosition && dataCount > 0) {
            return position - headerItemCount;
        }

        // 尾巴
        int footerItemCount = getFooterItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        if (position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0) {
            return position - headerItemCount - dataCount;
        }

        throw new IllegalArgumentException("illegal position: " + position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // TODO 这个要实现一下
        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        itemFactoryLocked = true;
        return getHeaderItemCount() + getDataCount() + getFooterItemCount();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (getItemFactoryCount() <= 0) {
            throw new IllegalStateException("You need to configure AssemblyPagerItemFactory use addItemFactory method");
        }

        // 头
        int headerItemCount = getHeaderItemCount();
        int headerStartPosition = 0;
        int headerEndPosition = headerItemCount - 1;
        if (position >= headerStartPosition && position <= headerEndPosition && headerItemCount > 0) {
            FixedPagerItemInfo fixedItemInfo = headerItemList.get(position);
            //noinspection unchecked
            View itemView = fixedItemInfo.getItemFactory().createView(container.getContext(), container, position, fixedItemInfo.getData());
            container.addView(itemView);
            return itemView;
        }

        // 数据
        int dataCount = getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        if (position >= dataStartPosition && position <= dataEndPosition && dataCount > 0) {
            int positionInDataList = position - headerItemCount;
            Object dataObject = dataList.get(positionInDataList);

            AssemblyPagerItemFactory itemFactory;
            for (int w = 0, size = itemFactoryList.size(); w < size; w++) {
                itemFactory = itemFactoryList.get(w);
                if (itemFactory.isTarget(dataObject)) {
                    //noinspection unchecked
                    View itemView = itemFactory.createView(container.getContext(), container, position, dataObject);
                    container.addView(itemView);
                    return itemView;
                }
            }

            throw new IllegalStateException("Didn't find suitable AssemblyPagerItemFactory. " +
                    "position=" + position + ", " +
                    "dataObject=" + (dataObject != null ? dataObject.getClass().getName() : "null"));
        }

        // 尾巴
        int footerItemCount = getFooterItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        if (position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0) {
            int positionInFooterList = position - headerItemCount - dataCount;
            FixedPagerItemInfo fixedItemInfo = footerItemList.get(positionInFooterList);
            //noinspection unchecked
            View itemView = fixedItemInfo.getItemFactory().createView(container.getContext(), container, position, fixedItemInfo.getData());
            container.addView(itemView);
            return itemView;
        }

        throw new IllegalArgumentException("illegal position: " + position);
    }
}
