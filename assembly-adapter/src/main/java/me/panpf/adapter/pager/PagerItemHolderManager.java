package me.panpf.adapter.pager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PagerItemHolderManager {
    @Nullable
    private ArrayList<PagerItemHolder> itemHolderList;
    @Nullable
    private ArrayList<PagerItemHolder> enabledItemHolderList;

    public void add(@NonNull PagerItemHolder itemHolder) {
        ArrayList<PagerItemHolder> allList = this.itemHolderList;
        if (allList == null) {
            allList = new ArrayList<>();
            this.itemHolderList = allList;
        }
        allList.add(itemHolder);
        refreshEnabledList();
    }

    private void refreshEnabledList() {
        final ArrayList<PagerItemHolder> allList = this.itemHolderList;
        if (allList != null) {
            ArrayList<PagerItemHolder> enabledList = this.enabledItemHolderList;
            if (enabledList == null) {
                enabledList = new ArrayList<>();
                this.enabledItemHolderList = enabledList;
            } else {
                enabledList.clear();
            }
            for (PagerItemHolder itemHolder : allList) {
                if (itemHolder.isEnabled()) {
                    enabledList.add(itemHolder);
                }
            }
        }
    }

    boolean itemEnabledChanged() {
        refreshEnabledList();
        return true;
    }

    public int getItemCount() {
        return itemHolderList != null ? itemHolderList.size() : 0;
    }

    @NonNull
    public PagerItemHolder getItem(int index) {
        if (itemHolderList != null) {
            return itemHolderList.get(index);
        } else {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: 0");
        }
    }

    @NonNull
    public PagerItemHolder getItemByClass(@NonNull Class clazz, int number) {
        if (itemHolderList != null) {
            int currentNumber = 0;
            for (PagerItemHolder itemHolder : itemHolderList) {
                if (clazz.equals(itemHolder.getItemFactory().getClass())) {
                    if (currentNumber == number) {
                        return itemHolder;
                    } else {
                        currentNumber++;
                    }
                }
            }
        }
        throw new IllegalArgumentException("Not found Item by class=" + clazz.toString() + " and number=" + number);
    }

    @NonNull
    public PagerItemHolder getItemByClass(@NonNull Class clazz) {
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
        PagerItemHolder itemHolder = getItem(index);
        itemHolder.setEnabled(!itemHolder.isEnabled());
    }


    public int getEnabledItemCount() {
        return enabledItemHolderList != null ? enabledItemHolderList.size() : 0;
    }

    @NonNull
    PagerItemHolder getItemInEnabledList(int index) {
        if (enabledItemHolderList != null) {
            return enabledItemHolderList.get(index);
        } else {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: 0");
        }
    }
}
