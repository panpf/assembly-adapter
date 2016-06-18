package me.xiaopan.assemblyadapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import me.xiaopan.assemblyadapter.AssemblyLoadMoreGroupItemFactory.AssemblyLoadMoreGroupItem;

public class AssemblyExpandableAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "AssemblyExpandableAdapter";

    private final Object mLock = new Object();
    private List dataList;
    private ExpandCallback expandCallback;

    private int groupTypeIndex = 0;
    private int childTypeIndex = 0;
    private boolean groupItemFactoryLocked;
    private boolean childItemFactoryLocked;
    private ArrayList<FixedGroupItemInfo> headerItemList;
    private ArrayList<FixedGroupItemInfo> footerItemList;
    private ArrayList<AssemblyGroupItemFactory> groupItemFactoryList;
    private ArrayList<AssemblyChildItemFactory> childItemFactoryList;
    private SparseArray<Object> groupItemFactoryArray;
    private SparseArray<Object> childItemFactoryArray;

    private boolean disableLoadMore;
    private AssemblyLoadMoreGroupItem loadMoreItem;
    private AssemblyLoadMoreGroupItemFactory loadMoreItemFactory;

    public AssemblyExpandableAdapter(List dataList) {
        this.dataList = dataList;
    }

    @SuppressWarnings("unused")
    public AssemblyExpandableAdapter(Object[] dataArray) {
        if (dataArray != null && dataArray.length > 0) {
            this.dataList = new ArrayList(dataArray.length);
            Collections.addAll(dataList, dataArray);
        }
    }

    /**
     * 添加一个将按添加顺序显示在列表头部的AssemblyGroupItemFactory
     */
    @SuppressLint("LongLogTag")
    @SuppressWarnings("unused")
    public FixedGroupItemInfo addHeaderItem(AssemblyGroupItemFactory headerFactory, Object data) {
        if (headerFactory == null || groupItemFactoryLocked) {
            Log.w(TAG, "headerFactory is nll or locked");
            return null;
        }

        headerFactory.setAdapter(this);
        headerFactory.setItemType(groupTypeIndex++);
        FixedGroupItemInfo headerItemInfo = new FixedGroupItemInfo(headerFactory, data);

        if (groupItemFactoryArray == null) {
            groupItemFactoryArray = new SparseArray<Object>();
        }
        groupItemFactoryArray.put(headerFactory.getItemType(), headerItemInfo);

        if (headerItemList == null) {
            headerItemList = new ArrayList<FixedGroupItemInfo>(2);
        }
        headerItemList.add(headerItemInfo);

        return headerItemInfo;
    }

    /**
     * 添加一个用来处理并显示dataList中的Group数据的AssemblyGroupItemFactory
     */
    public void addGroupItemFactory(AssemblyGroupItemFactory groupItemFactory) {
        if (groupItemFactory == null || groupItemFactoryLocked) {
            throw new IllegalStateException("groupItemFactory is null or locked");
        }

        groupItemFactory.setAdapter(this);
        groupItemFactory.setItemType(groupTypeIndex++);

        if (groupItemFactoryArray == null) {
            groupItemFactoryArray = new SparseArray<Object>();
        }
        groupItemFactoryArray.put(groupItemFactory.getItemType(), groupItemFactory);

        if (groupItemFactoryList == null) {
            groupItemFactoryList = new ArrayList<AssemblyGroupItemFactory>(5);
        }
        groupItemFactoryList.add(groupItemFactory);
    }

    /**
     * 添加一个用来处理并显示dataList中的Child数据的AssemblyChildItemFactory
     */
    public void addChildItemFactory(AssemblyChildItemFactory childItemFactory) {
        if (childItemFactory == null || childItemFactoryLocked) {
            throw new IllegalStateException("childItemFactory is null or locked");
        }

        childItemFactory.setAdapter(this);
        childItemFactory.setItemType(childTypeIndex++);

        if (childItemFactoryArray == null) {
            childItemFactoryArray = new SparseArray<Object>();
        }
        childItemFactoryArray.put(childItemFactory.getItemType(), childItemFactory);

        if (childItemFactoryList == null) {
            childItemFactoryList = new ArrayList<AssemblyChildItemFactory>(5);
        }
        childItemFactoryList.add(childItemFactory);
    }

    /**
     * 添加一个将按添加顺序显示在列表尾部的AssemblyGroupItemFactory
     */
    @SuppressLint("LongLogTag")
    @SuppressWarnings("unused")
    public FixedGroupItemInfo addFooterItem(AssemblyGroupItemFactory footerFactory, Object data) {
        if (footerFactory == null || groupItemFactoryLocked) {
            Log.w(TAG, "footerFactory is nll or locked");
            return null;
        }

        footerFactory.setAdapter(this);
        footerFactory.setItemType(groupTypeIndex++);
        FixedGroupItemInfo footerItemInfo = new FixedGroupItemInfo(footerFactory, data);

        if (groupItemFactoryArray == null) {
            groupItemFactoryArray = new SparseArray<Object>();
        }
        groupItemFactoryArray.put(footerFactory.getItemType(), footerItemInfo);

        if (footerItemList == null) {
            footerItemList = new ArrayList<FixedGroupItemInfo>(2);
        }
        footerItemList.add(footerItemInfo);

        return footerItemInfo;
    }

    /**
     * 设置一个将显示在列表最后（在Footer的后面）的加载更多尾巴
     */
    @SuppressLint("LongLogTag")
    public void setLoadMoreItem(AssemblyLoadMoreGroupItemFactory newLoadMoreItemFactory) {
        if (newLoadMoreItemFactory == null || groupItemFactoryLocked) {
            Log.w(TAG, "loadMoreItemFactory is null or locked");
            return;
        }

        newLoadMoreItemFactory.setEnd(false);
        newLoadMoreItemFactory.setAdapter(this);
        if (loadMoreItemFactory != null) {
            newLoadMoreItemFactory.setItemType(loadMoreItemFactory.getItemType());
        } else {
            newLoadMoreItemFactory.setItemType(groupTypeIndex++);
        }
        newLoadMoreItemFactory.setLoadMoreRunning(false);

        if (groupItemFactoryArray == null) {
            groupItemFactoryArray = new SparseArray<Object>();
        }
        groupItemFactoryArray.put(newLoadMoreItemFactory.getItemType(), newLoadMoreItemFactory);

        loadMoreItemFactory = newLoadMoreItemFactory;
    }

    /**
     * 批量添加数据
     */
    @SuppressWarnings("unused")
    public void addAll(Collection collection) {
        if (collection == null || collection.size() == 0) {
            return;
        }
        synchronized (mLock) {
            if (dataList == null) {
                dataList = new ArrayList(collection.size());
            }
            //noinspection unchecked
            dataList.addAll(collection);
        }
        notifyDataSetChanged();
    }

    /**
     * 批量添加数据
     */
    @SuppressWarnings("unused")
    public void addAll(Object... items) {
        if (items == null || items.length == 0) {
            return;
        }
        synchronized (mLock) {
            if (dataList == null) {
                dataList = new ArrayList(items.length);
            }
            Collections.addAll(dataList, items);
        }
        notifyDataSetChanged();
    }

    /**
     * 插入一条数据
     */
    @SuppressWarnings("unused")
    public void insert(Object object, int index) {
        if (object == null) {
            return;
        }
        synchronized (mLock) {
            if (dataList == null) {
                dataList = new ArrayList();
            }
            //noinspection unchecked
            dataList.add(index, object);
        }
        notifyDataSetChanged();
    }

    /**
     * 删除一条数据
     */
    @SuppressWarnings("unused")
    public void remove(Object object) {
        if (object == null) {
            return;
        }
        synchronized (mLock) {
            if (dataList != null) {
                dataList.remove(object);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 清空数据
     */
    @SuppressWarnings("unused")
    public void clear() {
        synchronized (mLock) {
            if (dataList != null) {
                dataList.clear();
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 对数据排序
     */
    @SuppressWarnings("unused")
    public void sort(Comparator comparator) {
        synchronized (mLock) {
            if (dataList != null) {
                Collections.sort(dataList, comparator);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 设置是否禁用加载更多
     */
    @SuppressWarnings("unused")
    public void setDisableLoadMore(boolean disableLoadMore) {
        this.disableLoadMore = disableLoadMore;

        if (loadMoreItemFactory != null) {
            loadMoreItemFactory.setEnd(false);
            loadMoreItemFactory.setLoadMoreRunning(false);
        }

        notifyDataSetChanged();
    }

    /**
     * 设置加载更多是否已结束，已结束会显示结束的文案并且不再触发加载更多
     */
    @SuppressWarnings("unused")
    public void setLoadMoreEnd(boolean loadMoreEnd) {
        if (loadMoreItemFactory != null) {
            loadMoreItemFactory.setLoadMoreRunning(false);
            loadMoreItemFactory.setEnd(loadMoreEnd);
        }
        if (loadMoreItem != null) {
            if (loadMoreEnd) {
                loadMoreItem.showEnd();
            } else {
                loadMoreItem.showLoading();
            }
        }
    }

    /**
     * 加载更多失败的时候调用此方法显示错误提示，并可点击重新加载
     */
    @SuppressWarnings("unused")
    public void loadMoreFailed() {
        if (loadMoreItemFactory != null) {
            loadMoreItemFactory.setLoadMoreRunning(false);
        }
        if (loadMoreItem != null) {
            loadMoreItem.showErrorRetry();
        }
    }

    /**
     * 删除一个HeaderItem
     */
    @SuppressWarnings("unused")
    public void removeHeaderItem(FixedGroupItemInfo headerItemInfo) {
        if (headerItemList != null && headerItemInfo != null) {
            Iterator<FixedGroupItemInfo> iterator = headerItemList.iterator();
            FixedGroupItemInfo fixedItemInfo;
            while (iterator.hasNext()) {
                fixedItemInfo = iterator.next();
                if (fixedItemInfo == headerItemInfo) {
                    iterator.remove();
                    notifyDataSetChanged();
                    return;
                }
            }
        }
    }

    /**
     * 删除一个FooterItem
     */
    @SuppressWarnings("unused")
    public void removeFooterItem(FixedGroupItemInfo footerItemInfo) {
        if (footerItemList != null && footerItemInfo != null) {
            Iterator<FixedGroupItemInfo> iterator = footerItemList.iterator();
            FixedGroupItemInfo fixedItemInfo;
            while (iterator.hasNext()) {
                fixedItemInfo = iterator.next();
                if (fixedItemInfo == footerItemInfo) {
                    iterator.remove();
                    notifyDataSetChanged();
                    return;
                }
            }
        }
    }

    /**
     * 获取Header列表
     */
    @SuppressWarnings("unused")
    public List<FixedGroupItemInfo> getHeaderItemList() {
        return headerItemList;
    }

    /**
     * 获取GroupItemFactory列表
     */
    @SuppressWarnings("unused")
    public List<AssemblyGroupItemFactory> getGroupItemFactoryList() {
        return groupItemFactoryList;
    }

    /**
     * 获取ChildItemFactory列表
     */
    @SuppressWarnings("unused")
    public List<AssemblyChildItemFactory> getChildItemFactoryList() {
        return childItemFactoryList;
    }

    /**
     * 获取Footer列表
     */
    @SuppressWarnings("unused")
    public List<FixedGroupItemInfo> getFooterItemList() {
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
     * 设置数据列表
     */
    @SuppressWarnings("unused")
    public void setDataList(List dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    /**
     * 获取列表头的个数
     */
    public int getHeaderItemCount() {
        return headerItemList != null ? headerItemList.size() : 0;
    }

    /**
     * 获取GroupItemFactory的个数
     */
    public int getGroupItemFactoryCount() {
        return groupItemFactoryList != null ? groupItemFactoryList.size() : 0;
    }

    /**
     * 获取ChildItemFactory的个数
     */
    public int getChildItemFactoryCount() {
        return childItemFactoryList != null ? childItemFactoryList.size() : 0;
    }

    /**
     * 获取列表头的个数
     */
    public int getFooterItemCount() {
        return footerItemList != null ? footerItemList.size() : 0;
    }

    /**
     * 是否有加载更多尾巴
     */
    public boolean hasLoadMoreFooter() {
        return !disableLoadMore && loadMoreItemFactory != null;
    }

    /**
     * 获取数据列表的长度
     */
    public int getDataCount() {
        return dataList != null ? dataList.size() : 0;
    }

    /**
     * 设置扩展回调
     */
    @SuppressWarnings("unused")
    public void setExpandCallback(ExpandCallback expandCallback) {
        this.expandCallback = expandCallback;
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

        // 加载更多尾巴
        if (dataCount > 0 && hasLoadMoreFooter() && position == getGroupCount() - 1) {
            return 0;
        }

        throw new IllegalArgumentException("illegal position: " + position);
    }

    @Override
    public int getGroupCount() {
        int headerItemCount = getHeaderItemCount();
        int dataCount = getDataCount();
        int footerItemCount = getFooterItemCount();

        if (dataCount > 0) {
            return headerItemCount + dataCount + footerItemCount + (hasLoadMoreFooter() ? 1 : 0);
        } else {
            return headerItemCount + footerItemCount;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        // 头
        int headerItemCount = getHeaderItemCount();
        int headerStartPosition = 0;
        int headerEndPosition = headerItemCount - 1;
        if (groupPosition >= headerStartPosition && groupPosition <= headerEndPosition && headerItemCount > 0) {
            //noinspection UnnecessaryLocalVariable
            int positionInHeaderList = groupPosition;
            return headerItemList.get(positionInHeaderList).getData();
        }

        // 数据
        int dataCount = getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        if (groupPosition >= dataStartPosition && groupPosition <= dataEndPosition && dataCount > 0) {
            int positionInDataList = groupPosition - headerItemCount;
            return dataList.get(positionInDataList);
        }

        // 尾巴
        int footerItemCount = getFooterItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        if (groupPosition >= footerStartPosition && groupPosition <= footerEndPosition && footerItemCount > 0) {
            int positionInFooterList = groupPosition - headerItemCount - dataCount;
            return footerItemList.get(positionInFooterList).getData();
        }

        // 加载更多尾巴
        if (dataCount > 0 && hasLoadMoreFooter() && groupPosition == getGroupCount() - 1) {
            return null;
        }

        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @SuppressLint("LongLogTag")
    @Override
    public int getChildrenCount(int groupPosition) {
        Object groupObject = getGroup(groupPosition);
        if (groupObject != null && groupObject instanceof AssemblyGroup) {
            return ((AssemblyGroup) groupObject).getChildCount();
        }
        return 0;
    }

    @SuppressLint("LongLogTag")
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Object groupDataObject = getGroup(groupPosition);
        if (groupDataObject == null) {
            return null;
        }
        if (!(groupDataObject instanceof AssemblyGroup)) {
            throw new IllegalArgumentException("group object must implements AssemblyGroup interface. " +
                    "groupPosition=" + groupPosition + ", " +
                    "groupDataObject=" + groupDataObject.getClass().getName());
        }
        return ((AssemblyGroup) groupDataObject).getChild(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getGroupTypeCount() {
        groupItemFactoryLocked = true;
        return groupTypeIndex > 0 ? groupTypeIndex : super.getGroupTypeCount();
    }

    @SuppressLint("LongLogTag")
    @Override
    public int getGroupType(int groupPosition) {
        if (getGroupItemFactoryCount() <= 0) {
            throw new IllegalStateException("You need to configure AssemblyGroupItemFactory use addGroupItemFactory method");
        }
        groupItemFactoryLocked = true;

        // 头
        int headerItemCount = getHeaderItemCount();
        int headerStartPosition = 0;
        int headerEndPosition = headerItemCount - 1;
        if (groupPosition >= headerStartPosition && groupPosition <= headerEndPosition && headerItemCount > 0) {
            //noinspection UnnecessaryLocalVariable
            int positionInHeaderList = groupPosition;
            return headerItemList.get(positionInHeaderList).getItemFactory().getItemType();
        }

        // 数据
        int dataCount = getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        if (groupPosition >= dataStartPosition && groupPosition <= dataEndPosition && dataCount > 0) {
            int positionInDataList = groupPosition - headerItemCount;
            Object groupDataObject = dataList.get(positionInDataList);

            AssemblyGroupItemFactory itemFactory;
            for (int w = 0, size = groupItemFactoryList.size(); w < size; w++) {
                itemFactory = groupItemFactoryList.get(w);
                if (itemFactory.isTarget(groupDataObject)) {
                    return itemFactory.getItemType();
                }
            }

            throw new IllegalStateException("Didn't find suitable AssemblyGroupItemFactory. " +
                    "positionInDataList=" + positionInDataList + ", " +
                    "groupDataObject=" + (groupDataObject != null ? groupDataObject.getClass().getName() : "null"));
        }

        // 尾巴
        int footerItemCount = getFooterItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        if (groupPosition >= footerStartPosition && groupPosition <= footerEndPosition && footerItemCount > 0) {
            int positionInFooterList = groupPosition - headerItemCount - dataCount;
            return footerItemList.get(positionInFooterList).getItemFactory().getItemType();
        }

        // 加载更多尾巴
        if (dataCount > 0 && hasLoadMoreFooter() && groupPosition == getGroupCount() - 1) {
            return loadMoreItemFactory.getItemType();
        }

        throw new IllegalStateException("not found match viewType, groupPosition: " + groupPosition);
    }

    @Override
    public int getChildTypeCount() {
        childItemFactoryLocked = true;
        return childTypeIndex > 0 ? childTypeIndex : super.getChildTypeCount();
    }

    @SuppressLint("LongLogTag")
    @Override
    public int getChildType(int groupPosition, int childPosition) {
        if (getChildItemFactoryCount() <= 0) {
            throw new IllegalStateException("You need to configure AssemblyChildItemFactory use addChildItemFactory method");
        }
        childItemFactoryLocked = true;

        Object childDataObject = getChild(groupPosition, childPosition);

        AssemblyChildItemFactory childItemFactory;
        for (int w = 0, size = childItemFactoryList.size(); w < size; w++) {
            childItemFactory = childItemFactoryList.get(w);
            if (childItemFactory.isTarget(childDataObject)) {
                return childItemFactory.getItemType();
            }
        }

        throw new IllegalStateException("Didn't find suitable AssemblyChildItemFactory. " +
                "groupPosition=" + groupPosition + ", " +
                "childPosition=" + childPosition + ", " +
                "childDataObject=" + (childDataObject != null ? childDataObject.getClass().getName() : "null"));
    }

    @Override
    public boolean hasStableIds() {
        return expandCallback != null && expandCallback.hasStableIds();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return expandCallback != null && expandCallback.isChildSelectable(groupPosition, childPosition);
    }

    @SuppressLint("LongLogTag")
    @Override
    @SuppressWarnings("unchecked")
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        AssemblyGroupItem groupItem;
        if (convertView == null) {
            groupItem = createGroupItem(parent, getGroupType(groupPosition));
            convertView = groupItem.getItemView();
            convertView.setTag(groupItem);
        } else {
            groupItem = (AssemblyGroupItem) convertView.getTag();
        }
        bindGroupItem(groupItem, isExpanded, groupPosition);
        return convertView;
    }

    private AssemblyGroupItem createGroupItem(ViewGroup parent, int viewType) {
        Object item = groupItemFactoryArray.get(viewType);

        // GroupItem或加载更多尾巴
        if (item instanceof AssemblyGroupItemFactory) {
            AssemblyGroupItemFactory itemFactory = (AssemblyGroupItemFactory) item;
            AssemblyGroupItem assemblyItem = itemFactory.createAssemblyItem(parent);
            if (assemblyItem instanceof AssemblyLoadMoreGroupItem) {
                this.loadMoreItem = (AssemblyLoadMoreGroupItem) assemblyItem;
            }
            return assemblyItem;
        }

        // 头或尾巴
        if (item instanceof FixedGroupItemInfo) {
            FixedGroupItemInfo fixedItemInfo = (FixedGroupItemInfo) item;
            return fixedItemInfo.getItemFactory().createAssemblyItem(parent);
        }

        throw new IllegalStateException("unknown groupViewType: " + viewType + ", " +
                "itemFactory: " + (item != null ? item.getClass().getName() : "null"));
    }

    @SuppressWarnings("unchecked")
    public void bindGroupItem(AssemblyGroupItem groupItem, boolean isExpanded, int groupPosition) {
        groupItem.setData(groupPosition, isExpanded, getGroup(groupPosition));
    }

    @SuppressLint("LongLogTag")
    @Override
    @SuppressWarnings("unchecked")
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        AssemblyChildItem childItem;
        if (convertView == null) {
            childItem = createChildItem(parent, getChildType(groupPosition, childPosition));
            convertView = childItem.getItemView();
            convertView.setTag(childItem);
        } else {
            childItem = (AssemblyChildItem) convertView.getTag();
        }
        bindChildItem(childItem, groupPosition, childPosition, isLastChild);
        return convertView;
    }

    private AssemblyChildItem createChildItem(ViewGroup parent, int viewType) {
        Object item = childItemFactoryArray.get(viewType);

        // ChildItem
        if (item instanceof AssemblyChildItemFactory) {
            AssemblyChildItemFactory itemFactory = (AssemblyChildItemFactory) item;
            return itemFactory.createAssemblyItem(parent);
        }

        throw new IllegalStateException("unknown childViewType: " + viewType + ", " +
                "itemFactory: " + (item != null ? item.getClass().getName() : "null"));
    }

    @SuppressWarnings("unchecked")
    public void bindChildItem(AssemblyChildItem childItem, int groupPosition, int childPosition, boolean isLastChild) {
        childItem.setData(groupPosition, childPosition, isLastChild, getChild(groupPosition, childPosition));
    }

    public interface ExpandCallback {
        boolean hasStableIds();

        boolean isChildSelectable(int groupPosition, int childPosition);
    }
}
