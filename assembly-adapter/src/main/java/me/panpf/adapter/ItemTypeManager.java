package me.panpf.adapter;

import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class ItemTypeManager {

    private int itemTypeIndex = 0;
    private boolean itemTypeLocked;
    @NonNull
    private SparseArray<Object> itemTypeFactoryArray = new SparseArray<>();
    // todo 弄一个 key 为 item，value 为 itemType 的 Map，这样 itemTye 就不需要保存到 Factory 中了

    boolean isLocked() {
        return itemTypeLocked;
    }

    void lock() {
        itemTypeLocked = true;
    }

    int getCount() {
        return itemTypeIndex > 0 ? itemTypeIndex : 1;
    }

    public int add(@NonNull Object item) {
        int newItemType = itemTypeIndex++;
        itemTypeFactoryArray.put(newItemType, item);
        return newItemType;
    }

    @Nullable
    public Object get(int itemType) {
        return itemTypeFactoryArray.get(itemType);
    }
}
