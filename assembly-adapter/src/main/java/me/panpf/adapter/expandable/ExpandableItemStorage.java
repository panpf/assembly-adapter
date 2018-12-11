package me.panpf.adapter.expandable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

import me.panpf.adapter.AssemblyAdapter;
import me.panpf.adapter.ItemHolder;
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
            throw new IllegalStateException("childItemFactory is null or item factory list locked");
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

    public int getChildTypeCount() {
        childItemFactoryLocked = true;
        return childTypeIndex > 0 ? childTypeIndex : 1;
    }

    /**
     * 根据 view 类型获取 {@link ItemFactory} 或 {@link ItemHolder}
     *
     * @param viewType view 类型
     * @return null：没有；{@link ItemFactory} 或 {@link ItemHolder}
     */
    @Nullable
    public Object getChildItemFactoryByViewType(int viewType) {
        return childItemFactoryArray != null ? childItemFactoryArray.get(viewType) : null;
    }
}
