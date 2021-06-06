package com.github.panpf.assemblyadapter;

import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemManager<FACTORY extends ItemFactory<?>> {

    private final int itemTypeCount;
    @NonNull
    private final List<FACTORY> itemFactoryList;
    @NonNull
    private final SparseArray<FACTORY> getItemFactoryByItemTypeArray;
    @NonNull
    private final Map<FACTORY, Integer> getItemTypeByItemFactoryArray;

    public ItemManager(@NonNull List<FACTORY> itemFactoryList) {
        if (itemFactoryList.size() == 0) {
            throw new IllegalArgumentException("itemFactoryList Can not be empty");
        }
        this.itemFactoryList = new ArrayList<>(itemFactoryList);
        this.getItemFactoryByItemTypeArray = new SparseArray<>();
        this.getItemTypeByItemFactoryArray = new HashMap<>();
        for (int i = 0; i < itemFactoryList.size(); i++) {
            //noinspection UnnecessaryLocalVariable
            final int itemType = i;
            final FACTORY itemFactory = itemFactoryList.get(i);
            getItemFactoryByItemTypeArray.append(itemType, itemFactory);
            getItemTypeByItemFactoryArray.put(itemFactory, itemType);
        }
        itemTypeCount = itemFactoryList.size();
    }

    @NonNull
    public List<FACTORY> getItemFactoryList() {
        return itemFactoryList;
    }

    public int getItemTypeCount() {
        return itemTypeCount;
    }

    @NonNull
    public FACTORY matchItemFactoryByData(@Nullable Object data) {
        for (FACTORY itemFactory : itemFactoryList) {
            if (itemFactory.match(data)) {
                return itemFactory;
            }
        }
        throw new IllegalStateException("Not found item factory by data: " + data);
    }

    @NonNull
    public FACTORY getItemFactoryByItemType(int itemType) {
        FACTORY itemFactory = getItemFactoryByItemTypeArray.get(itemType);
        if (itemFactory == null) {
            throw new IllegalArgumentException("Unknown item type: " + itemType);
        }
        return itemFactory;
    }

    public int getItemTypeByData(@Nullable Object data) {
        FACTORY itemFactory = matchItemFactoryByData(data);
        Integer itemType = getItemTypeByItemFactoryArray.get(itemFactory);
        if (itemType == null) {
            throw new IllegalStateException("Not found item type by item factory: " + itemFactory);
        }
        return itemType;
    }

    public int getItemTypeByItemFactory(@NonNull FACTORY itemFactory) {
        Integer itemType = getItemTypeByItemFactoryArray.get(itemFactory);
        if (itemType == null) {
            throw new IllegalStateException("Not found item type by item factory: " + itemFactory);
        }
        return itemType;
    }
}
