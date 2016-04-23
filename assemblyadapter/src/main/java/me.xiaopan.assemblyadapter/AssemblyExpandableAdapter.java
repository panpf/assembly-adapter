package me.xiaopan.assemblyadapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class AssemblyExpandableAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "AssemblyExpandAdapter";

    private List dataList;
    private List<AssemblyGroupItemFactory> groupItemFactoryList;
    private List<AssemblyChildItemFactory> childItemFactoryList;
    private AbstractLoadMoreGroupItemFactory loadMoreGroupItemFactory;
    private AbstractLoadMoreGroupItemFactory.AbstractLoadMoreGroupItem loadMoreGroupItem;
    private boolean groupItemFactoryLocked;  // 锁定之后就不能再添加GroupItemFactory了
    private boolean childItemFactoryLocked;  // 锁定之后就不能再添加ChildItemFactory了
    private boolean setEnableLoadMore;  // 已经设置过开启加载功能后就不能再添加GroupItemFactory了

    public AssemblyExpandableAdapter(List dataList) {
        this.dataList = dataList;
    }

    @SuppressWarnings("unused")
    public AssemblyExpandableAdapter(Object... dataArray) {
        if (dataArray != null && dataArray.length > 0) {
            this.dataList = new ArrayList(dataArray.length);
            Collections.addAll(dataList, dataArray);
        }
    }

    public void addGroupItemFactory(AssemblyGroupItemFactory groupItemFactory) {
        if (groupItemFactoryLocked) {
            throw new IllegalStateException("group item factory list locked");
        }
        if (setEnableLoadMore) {
            throw new IllegalStateException("Call a enableLoadMore () method can be not call again after addGroupItemFactory () method");
        }

        if (groupItemFactoryList == null) {
            groupItemFactoryList = new LinkedList<AssemblyGroupItemFactory>();
        }
        groupItemFactory.setAdapter(this);
        groupItemFactory.setItemType(groupItemFactoryList.size());
        groupItemFactoryList.add(groupItemFactory);
    }

    public void addChildItemFactory(AssemblyChildItemFactory childItemFactory) {
        if (childItemFactoryLocked) {
            throw new IllegalStateException("child item factory list locked");
        }

        if (childItemFactoryList == null) {
            childItemFactoryList = new LinkedList<AssemblyChildItemFactory>();
        }
        childItemFactory.setAdapter(this);
        childItemFactory.setItemType(childItemFactoryList.size());
        childItemFactoryList.add(childItemFactory);
    }

    @SuppressWarnings("unused")
    public List getDataList() {
        return dataList;
    }

    @SuppressWarnings("unused")
    public void setDataList(List dataList) {
        this.dataList = dataList;
    }

    @SuppressWarnings("unused")
    public List<AssemblyChildItemFactory> getChildItemFactoryList() {
        return childItemFactoryList;
    }

    @SuppressWarnings("unused")
    public List<AssemblyGroupItemFactory> getGroupItemFactoryList() {
        return groupItemFactoryList;
    }

    @SuppressWarnings("unchecked")
    public void append(List dataList) {
        if (dataList == null || dataList.size() == 0) {
            return;
        }

        if (this.dataList == null) {
            this.dataList = dataList;
        } else {
            this.dataList.addAll(dataList);
        }
        notifyDataSetChanged();
    }

    /**
     * 开启加载更多功能
     *
     * @param loadMoreGroupItemFactory 加载更多ItemFactory
     */
    public void enableLoadMore(AbstractLoadMoreGroupItemFactory loadMoreGroupItemFactory) {
        if (loadMoreGroupItemFactory != null) {
            if (groupItemFactoryList == null || groupItemFactoryList.size() == 0) {
                throw new IllegalStateException("You need to configure AssemblyGroupItemFactory use addGroupItemFactory method");
            }
            setEnableLoadMore = true;
            this.loadMoreGroupItemFactory = loadMoreGroupItemFactory;
            this.loadMoreGroupItemFactory.setLoadMoreRunning(false);
            this.loadMoreGroupItemFactory.setEnd(false);
            this.loadMoreGroupItemFactory.setAdapter(this);
            this.loadMoreGroupItemFactory.setItemType(groupItemFactoryList.size());
            notifyDataSetChanged();
        }
    }

    /**
     * 关闭加载更多功能
     */
    @SuppressWarnings("unused")
    public void disableLoadMore() {
        if (loadMoreGroupItemFactory != null) {
            loadMoreGroupItemFactory.setLoadMoreRunning(false);
            loadMoreGroupItemFactory.setEnd(false);
            loadMoreGroupItemFactory = null;
            notifyDataSetChanged();
        }
    }

    /**
     * 加载更多完成，当你一次请求完成后需要调用此方法
     */
    public void loadMoreFinished() {
        if (loadMoreGroupItemFactory != null) {
            loadMoreGroupItemFactory.setLoadMoreRunning(false);
        }
    }

    /**
     * 加载更过失败，请求失败的时候需要调用此方法，会显示错误提示，并可点击重新加载
     */
    @SuppressWarnings("unused")
    public void loadMoreFailed() {
        if (loadMoreGroupItemFactory != null) {
            loadMoreGroupItemFactory.setLoadMoreRunning(false);
        }
        if (loadMoreGroupItem != null) {
            loadMoreGroupItem.showErrorRetry();
        }
    }

    /**
     * 设置加载更多是否结束，当没有更多内容的时候你需要调用此方法，然后会显示结束的文案并且不再加载更多
     *
     * @param end 加载更多是否结束
     */
    @SuppressWarnings("unused")
    public void setLoadMoreEnd(boolean end) {
        if (loadMoreGroupItemFactory != null) {
            loadMoreGroupItemFactory.setLoadMoreRunning(false);
            loadMoreGroupItemFactory.setEnd(end);
        }
        if (loadMoreGroupItem != null) {
            if (end) {
                loadMoreGroupItem.showEnd();
            } else {
                loadMoreGroupItem.showLoading();
            }
        }
    }

    @Override
    public int getGroupCount() {
        if (dataList == null || dataList.size() == 0) {
            return 0;
        }
        return dataList.size() + (loadMoreGroupItemFactory != null ? 1 : 0);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        Object groupObject = getGroup(groupPosition);
        if (groupObject != null) {
            if (groupObject instanceof AssemblyGroup) {
                return ((AssemblyGroup) groupObject).getChildCount();
            } else {
                throw new IllegalArgumentException("group object must implements AssemblyGroup interface. groupPosition=" + groupPosition + ", groupObject=" + groupObject.getClass().getName());
            }
        } else {
            Log.e(TAG, "getChildrenCount() - group object is null. groupPosition=" + groupPosition);
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return dataList != null && groupPosition < dataList.size() ? dataList.get(groupPosition) : null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Object groupObject = getGroup(groupPosition);
        if (groupObject != null) {
            if (groupObject instanceof AssemblyGroup) {
                return ((AssemblyGroup) groupObject).getChild(childPosition);
            } else {
                throw new IllegalArgumentException("group object must implements AssemblyGroup interface. groupPosition=" + groupPosition + ", groupObject=" + groupObject.getClass().getName());
            }
        } else {
            Log.e(TAG, "getChild() - group object is null. groupPosition=" + groupPosition);
        }
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getGroupTypeCount() {
        if (groupItemFactoryList == null || groupItemFactoryList.size() == 0) {
            throw new IllegalStateException("You need to configure AssemblyGroupItemFactory use addGroupItemFactory method");
        }
        groupItemFactoryLocked = true;
        return groupItemFactoryList.size() + 1;
    }

    @Override
    public int getChildTypeCount() {
        if (childItemFactoryList == null || childItemFactoryList.size() == 0) {
            throw new IllegalStateException("You need to configure AssemblyChildItemFactory use addChildItemFactory method");
        }
        childItemFactoryLocked = true;
        return childItemFactoryList.size();
    }

    @Override
    public int getGroupType(int groupPosition) {
        if (groupItemFactoryList == null || groupItemFactoryList.size() == 0) {
            throw new IllegalStateException("You need to configure AssemblyGroupItemFactory use addGroupItemFactory method");
        }

        groupItemFactoryLocked = true;
        if (loadMoreGroupItemFactory != null && groupPosition == getGroupCount() - 1) {
            return loadMoreGroupItemFactory.getItemType();
        }

        Object groupObject = getGroup(groupPosition);
        for (AssemblyGroupItemFactory groupItemFactory : groupItemFactoryList) {
            if (groupItemFactory.isTarget(groupObject)) {
                return groupItemFactory.getItemType();
            }
        }

        Log.e(TAG, "getGroupType() - Didn't find suitable AssemblyGroupItemFactory. groupPosition=" + groupPosition + ", groupObject=" + (groupObject != null ? groupObject.getClass().getName() : "null"));
        return -1;
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        if (childItemFactoryList == null || childItemFactoryList.size() == 0) {
            throw new IllegalStateException("You need to configure AssemblyChildItemFactory use addChildItemFactory method");
        }

        childItemFactoryLocked = true;
        Object childObject = getChild(groupPosition, childPosition);
        for (AssemblyChildItemFactory childItemFactory : childItemFactoryList) {
            if (childItemFactory.isTarget(childObject)) {
                return childItemFactory.getItemType();
            }
        }

        Log.e(TAG, "getChildType() - Didn't find suitable AssemblyChildItemFactory. groupPosition=" + groupPosition + ", childPosition=" + childPosition + ", childObject=" + (childObject != null ? childObject.getClass().getName() : "null"));
        return -1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (groupItemFactoryList == null || groupItemFactoryList.size() == 0) {
            throw new IllegalStateException("You need to configure AssemblyGroupItemFactory use addGroupItemFactory method");
        }

        // groupPosition是最后一位，说明是加载更多尾巴
        if (loadMoreGroupItemFactory != null && groupPosition == getGroupCount() - 1) {
            if (convertView == null) {
                AssemblyGroupItem assemblyItem = loadMoreGroupItemFactory.createAssemblyItem(parent);
                if (assemblyItem == null) {
                    Log.e(TAG, "getView() - Create AssemblyGroupItem failed. groupPosition=" + groupPosition + ", GroupItemFactory=" + loadMoreGroupItemFactory.getClass().getName());
                    return null;
                }
                convertView = assemblyItem.getItemView();
            }

            this.loadMoreGroupItem = (AbstractLoadMoreGroupItemFactory.AbstractLoadMoreGroupItem) convertView.getTag();
            this.loadMoreGroupItem.setData(groupPosition, isExpanded, null);
            return convertView;
        }

        Object groupObject = getGroup(groupPosition);
        for (AssemblyGroupItemFactory groupItemFactory : groupItemFactoryList) {
            if (!groupItemFactory.isTarget(groupObject)) {
                continue;
            }

            if (convertView == null) {
                AssemblyGroupItem groupItem = groupItemFactory.createAssemblyItem(parent);
                if (groupItem == null) {
                    Log.e(TAG, "getGroupView() - Create AssemblyGroupItem failed. groupPosition=" + groupPosition + ", GroupItemFactory" + groupItemFactory.getClass().getName());
                    return null;
                }
                convertView = groupItem.getItemView();
            }

            ((AssemblyGroupItem) convertView.getTag()).setData(groupPosition, isExpanded, groupObject);
            return convertView;
        }

        Log.e(TAG, "getGroupView() - Didn't find suitable AssemblyGroupItemFactory. groupPosition=" + groupPosition + ", groupObject=" + (groupObject != null ? groupObject.getClass().getName() : "null"));
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (childItemFactoryList == null || childItemFactoryList.size() == 0) {
            throw new IllegalStateException("You need to configure AssemblyChildItemFactory use addChildItemFactory method");
        }

        Object childObject = getChild(groupPosition, childPosition);
        for (AssemblyChildItemFactory childItemFactory : childItemFactoryList) {
            if (!childItemFactory.isTarget(childObject)) {
                continue;
            }

            if (convertView == null) {
                AssemblyChildItem childItem = childItemFactory.createAssemblyItem(parent);
                if (childItem == null) {
                    Log.e(TAG, "getChildView() - Create AssemblyChildItem failed. groupPosition=" + groupPosition + ", childPosition=" + childPosition + ", ChildItemFactory" + childItemFactory.getClass().getName());
                    return null;
                }
                convertView = childItem.getItemView();
            }

            ((AssemblyChildItem) convertView.getTag()).setData(groupPosition, childPosition, isLastChild, childObject);
            return convertView;
        }

        Log.e(TAG, "getChildView() - Didn't find suitable AssemblyChildItemFactory. groupPosition=" + groupPosition + ", childPosition=" + childPosition + ", childObject=" + (childObject != null ? childObject.getClass().getName() : "null"));
        return null;
    }
}
