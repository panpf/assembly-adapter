package me.panpf.adapter.pager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class FragmentFixedItemManager {

    @Nullable
    private ArrayList<FragmentFixedItem> itemList;

    public void add(@NonNull FragmentFixedItem item) {
        ArrayList<FragmentFixedItem> allList = this.itemList;
        if (allList == null) {
            allList = new ArrayList<>();
            this.itemList = allList;
        }
        allList.add(item);
    }

    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }

    @NonNull
    public FragmentFixedItem getItem(int index) {
        if (itemList != null) {
            return itemList.get(index);
        } else {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: 0");
        }
    }

    @NonNull
    public FragmentFixedItem getItemByClass(@NonNull Class clazz, int number) {
        if (itemList != null) {
            int currentNumber = 0;
            for (FragmentFixedItem item : itemList) {
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
    public FragmentFixedItem getItemByClass(@NonNull Class clazz) {
        return getItemByClass(clazz, 0);
    }

    public void setItemData(int index, @Nullable Object data) {
        //noinspection unchecked
        getItem(index).setData(data);
    }
}
