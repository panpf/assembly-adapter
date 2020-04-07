package me.panpf.adapter.expandable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import me.panpf.adapter.AssemblyAdapter;
import me.panpf.adapter.ItemFactory;
import me.panpf.adapter.ItemManager;
import me.panpf.adapter.ViewTypeManager;

public class ExpandableItemManager extends ItemManager {

    @NonNull
    private ViewTypeManager childViewTypeManager = new ViewTypeManager();
    @NonNull
    private ArrayList<ItemFactory> childItemFactoryList = new ArrayList<>();

    public ExpandableItemManager(@NonNull AssemblyAdapter adapter) {
        super(adapter);
    }

    public ExpandableItemManager(@NonNull AssemblyAdapter adapter, @Nullable List dataList) {
        super(adapter, dataList);
    }

    public ExpandableItemManager(@NonNull AssemblyAdapter adapter, @Nullable Object[] dataArray) {
        super(adapter, dataArray);
    }

    /**
     * 添加一个用来处理并显示 dataList 中的 child 数据的 {@link ItemFactory}
     */
    public void addChildItemFactory(@NonNull ItemFactory childItemFactory) {
        //noinspection ConstantConditions
        if (childItemFactory == null || childViewTypeManager.isLocked()) {
            throw new IllegalStateException("childItemFactory is null or item factory list locked");
        }

        childItemFactoryList.add(childItemFactory);
        int viewType = childViewTypeManager.add(childItemFactory);

        childItemFactory.attachToAdapter(getAdapter(), viewType);
    }

    public int getChildTypeCount() {
        if (!childViewTypeManager.isLocked()) {
            childViewTypeManager.lock();
        }
        return childViewTypeManager.getCount();
    }

    @Nullable
    public Object getChildItemFactoryByViewType(int viewType) {
        return childViewTypeManager.get(viewType);
    }

    public int getChildrenCount(int groupPosition) {
        Object groupObject = getItemDataByPosition(groupPosition);
        if (groupObject instanceof AssemblyGroup) {
            return ((AssemblyGroup) groupObject).getChildCount();
        }
        return 0;
    }

    @Nullable
    public Object getChildDataByPosition(int groupPosition, int childPosition) {
        Object groupDataObject = getItemDataByPosition(groupPosition);
        if (groupDataObject == null) {
            throw new IllegalArgumentException("Not found group item data by group position: " + groupPosition);
        }
        if (!(groupDataObject instanceof AssemblyGroup)) {
            throw new IllegalArgumentException(String.format(
                    "group object must implements AssemblyGroup interface. groupPosition=%d, groupDataObject=%s",
                    groupPosition, groupDataObject.getClass().getName()));
        }
        return ((AssemblyGroup) groupDataObject).getChild(childPosition);
    }

    public int getChildViewType(int groupPosition, int childPosition) {
        if (childItemFactoryList.size() <= 0) {
            throw new IllegalStateException("You need to configure ItemFactory use addChildItemFactory method");
        }

        Object childDataObject = getChildDataByPosition(groupPosition, childPosition);

        ItemFactory childItemFactory;
        for (int w = 0, size = childItemFactoryList.size(); w < size; w++) {
            childItemFactory = childItemFactoryList.get(w);
            if (childItemFactory.match(childDataObject)) {
                return childItemFactory.getViewType();
            }
        }

        throw new IllegalStateException(String.format(
                "Didn't find suitable ItemFactory. groupPosition=%d, childPosition=%d, childDataObject=%s",
                groupPosition, childPosition, childDataObject != null ? childDataObject.getClass().getName() : "null"));
    }
}
