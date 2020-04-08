package me.panpf.adapter.pager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class FragmentItemHolderManager {
    @Nullable
    private ArrayList<FragmentItemHolder> itemHolderList;

    public void add(@NonNull FragmentItemHolder itemHolder) {
        ArrayList<FragmentItemHolder> allList = this.itemHolderList;
        if (allList == null) {
            allList = new ArrayList<>();
            this.itemHolderList = allList;
        }
        allList.add(itemHolder);
    }

    public int getItemCount() {
        return itemHolderList != null ? itemHolderList.size() : 0;
    }

    @NonNull
    public FragmentItemHolder getItem(int index) {
        if (itemHolderList != null) {
            return itemHolderList.get(index);
        } else {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: 0");
        }
    }

    @NonNull
    public FragmentItemHolder getItemByClass(@NonNull Class clazz, int number) {
        if (itemHolderList != null) {
            int currentNumber = 0;
            for (FragmentItemHolder itemHolder : itemHolderList) {
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
    public FragmentItemHolder getItemByClass(@NonNull Class clazz) {
        return getItemByClass(clazz, 0);
    }

    public void setItemData(int index, @Nullable Object data) {
        //noinspection unchecked
        getItem(index).setData(data);
    }
}
