package me.panpf.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class FixedItemManager {

    @Nullable
    private ArrayList<FixedItem> itemList;
    @Nullable
    private ArrayList<FixedItem> enabledItemList;

    public void add(@NonNull FixedItem fixedItem) {
        ArrayList<FixedItem> allList = this.itemList;
        if (allList == null) {
            allList = new ArrayList<>();
            this.itemList = allList;
        }
        fixedItem.setPositionInPartItemList(allList.size());
        allList.add(fixedItem);
        refreshEnabledList();
    }

    private void refreshEnabledList() {
        final ArrayList<FixedItem> allList = this.itemList;
        if (allList != null) {
            ArrayList<FixedItem> enabledList = this.enabledItemList;
            if (enabledList == null) {
                enabledList = new ArrayList<>();
                this.enabledItemList = enabledList;
            } else {
                enabledList.clear();
            }
            for (FixedItem fixedItem : allList) {
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
    public FixedItem getItem(int index) {
        if (itemList != null) {
            return itemList.get(index);
        } else {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: 0");
        }
    }

    @NonNull
    public <DATA> FixedItem<DATA> getItemByFactoryClass(@NonNull Class<? extends ItemFactory<DATA>> clazz, int number) {
        if (itemList != null) {
            int currentNumber = 0;
            for (FixedItem fixedItem : itemList) {
                if (clazz.equals(fixedItem.getItemFactory().getClass())) {
                    if (currentNumber == number) {
                        return fixedItem;
                    } else {
                        currentNumber++;
                    }
                }
            }
        }
        throw new IllegalArgumentException("Not found Item by class=" + clazz.toString() + " and number=" + number);
    }

    @NonNull
    public <DATA> FixedItem<DATA> getItemByFactoryClass(@NonNull Class<? extends ItemFactory<DATA>> clazz) {
        return getItemByFactoryClass(clazz, 0);
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


    public int getEnabledItemCount() {
        return enabledItemList != null ? enabledItemList.size() : 0;
    }

    @NonNull
    public FixedItem getItemInEnabledList(int index) {
        if (enabledItemList != null) {
            return enabledItemList.get(index);
        } else {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: 0");
        }
    }
}