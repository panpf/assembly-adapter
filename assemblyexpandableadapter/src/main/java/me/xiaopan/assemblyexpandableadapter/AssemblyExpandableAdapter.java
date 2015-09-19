package me.xiaopan.assemblyexpandableadapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.LinkedList;
import java.util.List;

public class AssemblyExpandableAdapter extends BaseExpandableListAdapter implements AbstractLoadMoreGroupItemFactory.AdapterCallback {
    private static final String TAG = "AssemblyExpandAdapter";

    private List<Object> dataList;
    private List<AssemblyGroupItemFactory> groupItemFactoryList;
    private List<AssemblyChildItemFactory> childItemFactoryList;
    private AbstractLoadMoreGroupItemFactory loadMoreGroupItemFactory;
    private AbstractLoadMoreGroupItemFactory.AbstractLoadMoreGroupItem loadMoreGroupItem;

    public AssemblyExpandableAdapter(List<Object> dataList) {
        this.dataList = dataList;
    }

    public void addGroupItemFactory(AssemblyGroupItemFactory groupItemFactory) {
        if (loadMoreGroupItemFactory != null) {
            throw new IllegalStateException("Call a enableLoadMore () method can be not call again after addGroupItemFactory () method");
        }

        if (groupItemFactoryList == null) {
            groupItemFactoryList = new LinkedList<AssemblyGroupItemFactory>();
        }
        groupItemFactory.setItemType(groupItemFactoryList.size());
        groupItemFactoryList.add(groupItemFactory);
    }

    public void addChildItemFactory(AssemblyChildItemFactory childItemFactory) {
        if (childItemFactoryList == null) {
            childItemFactoryList = new LinkedList<AssemblyChildItemFactory>();
        }
        childItemFactory.setItemType(childItemFactoryList.size());
        childItemFactoryList.add(childItemFactory);
    }

    public List<Object> getDataList() {
        return dataList;
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

    public void enableLoadMore(AbstractLoadMoreGroupItemFactory loadMoreGroupItemFactory) {
        if (groupItemFactoryList == null || groupItemFactoryList.size() == 0) {
            throw new IllegalStateException("You need to configure AssemblyGroupItemFactory use addGroupItemFactory method");
        }

        if (loadMoreGroupItemFactory != null) {
            this.loadMoreGroupItemFactory = loadMoreGroupItemFactory;
            this.loadMoreGroupItemFactory.setAdapterCallback(this);
            this.loadMoreGroupItemFactory.setItemType(groupItemFactoryList.size());
            notifyDataSetChanged();
        }
    }

    public void disableLoadMore() {
        if(loadMoreGroupItemFactory != null){
            loadMoreGroupItemFactory.loadMoreRunning = false;
            loadMoreGroupItemFactory = null;
            notifyDataSetChanged();
        }
    }

    @Override
    public void loading() {
        if(loadMoreGroupItemFactory != null){
            loadMoreGroupItemFactory.loadMoreRunning = true;
        }
    }

    @Override
    public void loadMoreFinished() {
        if(loadMoreGroupItemFactory != null){
            loadMoreGroupItemFactory.loadMoreRunning = false;
        }
    }

    @Override
    public void loadMoreFailed() {
        if(loadMoreGroupItemFactory != null){
            loadMoreGroupItemFactory.loadMoreRunning = false;
        }
        if (loadMoreGroupItem != null) {
            loadMoreGroupItem.showErrorRetry();
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
        return groupItemFactoryList.size() + (loadMoreGroupItemFactory != null ? 1 : 0);
    }

    @Override
    public int getChildTypeCount() {
        if (childItemFactoryList == null || childItemFactoryList.size() == 0) {
            throw new IllegalStateException("You need to configure AssemblyChildItemFactory use addChildItemFactory method");
        }
        return childItemFactoryList.size();
    }

    @Override
    public int getGroupType(int groupPosition) {
        if (groupItemFactoryList == null || groupItemFactoryList.size() == 0) {
            throw new IllegalStateException("You need to configure AssemblyGroupItemFactory use addGroupItemFactory method");
        }

        if(loadMoreGroupItemFactory != null && groupPosition == getGroupCount()-1){
            return loadMoreGroupItemFactory.getItemType();
        }

        Object groupObject = getGroup(groupPosition);
        for (AssemblyGroupItemFactory groupItemFactory : groupItemFactoryList) {
            if (groupItemFactory.isAssignableFrom(groupObject)) {
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

        Object childObject = getChild(groupPosition, childPosition);
        for (AssemblyChildItemFactory childItemFactory : childItemFactoryList) {
            if (childItemFactory.isAssignableFrom(childObject)) {
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
        if(loadMoreGroupItemFactory != null && groupPosition == getGroupCount()-1){
            if(convertView == null){
                AssemblyGroupItem assemblyItem = loadMoreGroupItemFactory.createAssemblyItem(parent);
                if(assemblyItem == null){
                    Log.e(TAG, "getView() - Create AssemblyGroupItem failed. groupPosition="+groupPosition+", GroupItemFactory="+loadMoreGroupItemFactory.getClass().getName());
                    return null;
                }
                convertView = assemblyItem.getConvertView();
            }

            this.loadMoreGroupItem = (AbstractLoadMoreGroupItemFactory.AbstractLoadMoreGroupItem) convertView.getTag();
            this.loadMoreGroupItem.setData(groupPosition, isExpanded, null);
            return convertView;
        }

        Object groupObject = getGroup(groupPosition);
        for (AssemblyGroupItemFactory groupItemFactory : groupItemFactoryList) {
            if (!groupItemFactory.isAssignableFrom(groupObject)) {
                continue;
            }

            if (convertView == null) {
                AssemblyGroupItem groupItem = groupItemFactory.createAssemblyItem(parent);
                if (groupItem == null) {
                    Log.e(TAG, "getGroupView() - Create AssemblyGroupItem failed. groupPosition=" + groupPosition + ", GroupItemFactory" + groupItemFactory.getClass().getName());
                    return null;
                }
                convertView = groupItem.getConvertView();
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
            if (!childItemFactory.isAssignableFrom(childObject)) {
                continue;
            }

            if (convertView == null) {
                AssemblyChildItem childItem = childItemFactory.createAssemblyItem(parent);
                if (childItem == null) {
                    Log.e(TAG, "getChildView() - Create AssemblyChildItem failed. groupPosition=" + groupPosition + ", childPosition=" + childPosition + ", ChildItemFactory" + childItemFactory.getClass().getName());
                    return null;
                }
                convertView = childItem.getConvertView();
            }

            ((AssemblyChildItem) convertView.getTag()).setData(groupPosition, childPosition, isLastChild, childObject);
            return convertView;
        }

        Log.e(TAG, "getChildView() - Didn't find suitable AssemblyChildItemFactory. groupPosition=" + groupPosition + ", childPosition=" + childPosition + ", childObject=" + (childObject != null ? childObject.getClass().getName() : "null"));
        return null;
    }
}
