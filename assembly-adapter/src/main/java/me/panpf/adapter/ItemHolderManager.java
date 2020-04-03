package me.panpf.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// todo 更名 FixedItemManager
public class ItemHolderManager {

    @Nullable
    private ArrayList<ItemHolder> itemHolderList;
    private int itemHolderPosition;

    public void add(@NonNull ItemHolder itemHolder) {
        itemHolder.setPosition(itemHolderPosition++);
        if (itemHolderList == null) {
            itemHolderList = new ArrayList<>();
        }
        itemHolderList.add(itemHolder);
    }

    @Nullable
    public ItemHolder getItem(int positionInHeaderList) {
        return itemHolderList != null && positionInHeaderList < itemHolderList.size() ? itemHolderList.get(positionInHeaderList) : null;
    }

    /**
     * 获取列表头的个数
     */
    public int getItemCount() {
        return itemHolderList != null ? itemHolderList.size() : 0;
    }

    @Nullable
    public Object getItemData(int positionInHeaderList) {
        return itemHolderList != null && positionInHeaderList < itemHolderList.size() ? itemHolderList.get(positionInHeaderList).getData() : null;
    }

    public boolean itemHolderEnabledChanged(@NonNull ItemHolder itemHolder) {
        if (itemHolder.isEnabled()) {
            if (itemHolderList == null) {
                itemHolderList = new ArrayList<>();
            }
            itemHolderList.add(itemHolder);
            Collections.sort(itemHolderList, new Comparator<ItemHolder>() {
                @Override
                public int compare(ItemHolder lhs, ItemHolder rhs) {
                    return lhs.getPosition() - rhs.getPosition();
                }
            });

            return true;
        } else {
            return itemHolderList != null && itemHolderList.remove(itemHolder);
        }
    }

    @Nullable
    public ArrayList<ItemHolder> getItemList() {
        return itemHolderList;
    }
}
