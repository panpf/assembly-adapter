package me.panpf.adapter.expandable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

import me.panpf.adapter.AssemblyAdapter;
import me.panpf.adapter.FixedItemInfo;
import me.panpf.adapter.ItemFactory;
import me.panpf.adapter.ItemStorage;

@SuppressWarnings({"unused", "WeakerAccess"})
public class ExpandableItemStorage extends ItemStorage {

    @NonNull
    private final Object childItemFactoryListLock = new Object();

    private int childTypeIndex = 0;
    private boolean childItemFactoryLocked;

    @Nullable
    private ArrayList<ItemFactory> childItemFactoryList;
    @Nullable
    private SparseArray<Object> childItemFactoryArray;

    public ExpandableItemStorage(@NonNull AssemblyAdapter adapter) {
        super(adapter);
    }

    public ExpandableItemStorage(@NonNull AssemblyAdapter adapter, @Nullable List dataList) {
        super(adapter, dataList);
    }

    public ExpandableItemStorage(@NonNull AssemblyAdapter adapter, @Nullable Object[] dataArray) {
        super(adapter, dataArray);
    }

    /**
     * 添加一个用来处理并显示 dataList 中的 child 数据的 {@link ItemFactory}
     */
    public void addChildItemFactory(@NonNull ItemFactory childItemFactory) {
        //noinspection ConstantConditions
        if (childItemFactory == null || childItemFactoryLocked) {
            throw new IllegalStateException("childItemFactory is null or item factory locked");
        }

        childItemFactory.setAdapter(getAdapter());
        childItemFactory.setItemType(childTypeIndex++);

        if (childItemFactoryArray == null) {
            childItemFactoryArray = new SparseArray<Object>();
        }
        childItemFactoryArray.put(childItemFactory.getItemType(), childItemFactory);

        synchronized (childItemFactoryListLock) {
            if (childItemFactoryList == null) {
                childItemFactoryList = new ArrayList<ItemFactory>(5);
            }
            childItemFactoryList.add(childItemFactory);
        }
    }

    /**
     * 获取 child {@link ItemFactory} 列表
     */
    @Nullable
    public List<ItemFactory> getChildItemFactoryList() {
        return childItemFactoryList;
    }

    /**
     * 获取 child {@link ItemFactory} 的个数
     */
    public int getChildItemFactoryCount() {
        return childItemFactoryList != null ? childItemFactoryList.size() : 0;
    }

    public int getChildrenCount(int groupPosition) {
        Object groupObject = getItem(groupPosition);
        if (groupObject != null && groupObject instanceof AssemblyGroup) {
            return ((AssemblyGroup) groupObject).getChildCount();
        }
        return 0;
    }

    @Nullable
    public Object getChild(int groupPosition, int childPosition) {
        Object groupDataObject = getItem(groupPosition);
        if (groupDataObject == null) {
            return null;
        }
        if (!(groupDataObject instanceof AssemblyGroup)) {
            throw new IllegalArgumentException(String.format(
                    "group object must implements AssemblyGroup interface. groupPosition=%d, groupDataObject=%s",
                    groupPosition, groupDataObject.getClass().getName()));
        }
        return ((AssemblyGroup) groupDataObject).getChild(childPosition);
    }

    public int getChildTypeCount() {
        childItemFactoryLocked = true;
        return childTypeIndex > 0 ? childTypeIndex : 1;
    }

    public int getChildType(int groupPosition, int childPosition) {
        if (getChildItemFactoryCount() <= 0) {
            throw new IllegalStateException("You need to configure ItemFactory use addChildItemFactory method");
        }
        childItemFactoryLocked = true;

        Object childDataObject = getChild(groupPosition, childPosition);

        if (childItemFactoryList != null) {
            ItemFactory childItemFactory;
            for (int w = 0, size = childItemFactoryList.size(); w < size; w++) {
                childItemFactory = childItemFactoryList.get(w);
                if (childItemFactory.isTarget(childDataObject)) {
                    return childItemFactory.getItemType();
                }
            }
        }

        throw new IllegalStateException(String.format(
                "Didn't find suitable ItemFactory. groupPosition=%d, childPosition=%d, childDataObject=%s",
                groupPosition, childPosition, childDataObject != null ? childDataObject.getClass().getName() : "null"));
    }

    /**
     * 根据 view 类型获取 {@link ItemFactory} 或 {@link FixedItemInfo}
     *
     * @param viewType view 类型，参见 {@link #getItemViewType(int)} 方法
     * @return null：没有；{@link ItemFactory} 或 {@link FixedItemInfo}
     */
    @Nullable
    public Object getChildItemFactoryByViewType(int viewType) {
        return childItemFactoryArray != null ? childItemFactoryArray.get(viewType) : null;
    }
}
