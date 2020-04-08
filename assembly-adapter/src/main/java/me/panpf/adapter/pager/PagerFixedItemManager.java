package me.panpf.adapter.pager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PagerFixedItemManager {

    @Nullable
    private ArrayList<PagerFixedItem> itemList;
    @Nullable
    private ArrayList<PagerFixedItem> enabledItemList;

    public void add(@NonNull PagerFixedItem fixedItem) {
        ArrayList<PagerFixedItem> allList = this.itemList;
        if (allList == null) {
            allList = new ArrayList<>();
            this.itemList = allList;
        }
        fixedItem.setPositionInPartItemList(allList.size());
        allList.add(fixedItem);
        refreshEnabledList();
    }

    private void refreshEnabledList() {
        final ArrayList<PagerFixedItem> allList = this.itemList;
        if (allList != null) {
            ArrayList<PagerFixedItem> enabledList = this.enabledItemList;
            if (enabledList == null) {
                enabledList = new ArrayList<>();
                this.enabledItemList = enabledList;
            } else {
                enabledList.clear();
            }
            for (PagerFixedItem fixedItem : allList) {
                if (fixedItem.isEnabled()) {
                    fixedItem.setPositionInPartList(enabledList.size());
                    enabledList.add(fixedItem);
                }
            }
        }
    }

    boolean itemEnabledChanged() {
        refreshEnabledList();
        return true;
    }

    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }

    @NonNull
    public PagerFixedItem getItem(int index) {
        if (itemList != null) {
            return itemList.get(index);
        } else {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: 0");
        }
    }

    @NonNull
    public PagerFixedItem getItemByClass(@NonNull Class clazz, int number) {
        if (itemList != null) {
            int currentNumber = 0;
            for (PagerFixedItem item : itemList) {
                if (clazz.equals(item.getItemFactory().getClass())) {
                    if (currentNumber == number) {
                        return item;
                    } else {
                        currentNumber++;
                    }
                }
            }
        }
        throw new IllegalArgumentException("Not found Item by class=" + clazz.toString() + " and number=" + number);
    }

    @NonNull
    public PagerFixedItem getItemByClass(@NonNull Class clazz) {
        return getItemByClass(clazz, 0);
    }

    public void setItemData(int index, @Nullable Object data) {
        //noinspection unchecked
        getItem(index).setData(data);
    }

    public boolean isItemEnabled(int index) {
        return getItem(index).isEnabled();
    }

    public void setItemEnabled(int index, boolean enabled) {
        getItem(index).setEnabled(enabled);
    }

    public void switchItemEnabled(int index) {
        PagerFixedItem item = getItem(index);
        item.setEnabled(!item.isEnabled());
    }


    public int getEnabledItemCount() {
        return enabledItemList != null ? enabledItemList.size() : 0;
    }

    @NonNull
    PagerFixedItem getItemInEnabledList(int index) {
        if (enabledItemList != null) {
            return enabledItemList.get(index);
        } else {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: 0");
        }
    }
}
