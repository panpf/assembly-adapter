package me.panpf.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FixedItem<DATA> {

    @NonNull
    private ItemFactory<DATA> itemFactory;
    @Nullable
    private DATA data;

    @Nullable
    private ItemManager itemManager;
    private boolean header;
    private boolean enabled = true;
    private int positionInPartList;
    private int positionInPartItemList;

    public FixedItem(@NonNull ItemFactory<DATA> itemFactory, @Nullable DATA data) {
        this.itemFactory = itemFactory;
        this.data = data;
    }

    public FixedItem(@NonNull ItemFactory<DATA> itemFactory) {
        this.itemFactory = itemFactory;
    }

    @Nullable
    public DATA getData() {
        return data;
    }

    public void setData(@Nullable DATA data) {
        this.data = data;

        AssemblyAdapter adapter = itemFactory.getAdapter();
        if (adapter != null && adapter.isNotifyOnChange()) {
            adapter.notifyDataSetChanged();
        }
    }

    @NonNull
    public ItemFactory<DATA> getItemFactory() {
        return itemFactory;
    }

    public boolean isAttached() {
        return itemManager != null;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @NonNull
    public FixedItem setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;
            enableChanged();
        }
        return this;
    }

    void attachToAdapter(@NonNull ItemManager itemManager, boolean header) {
        this.itemManager = itemManager;
        this.header = header;
    }

    protected void enableChanged() {
        if (itemManager != null) {
            itemManager.fixedItemEnabledChanged(this);
        }
    }

    public boolean isHeader() {
        return header;
    }

    public int getPositionInPartList() {
        return positionInPartList;
    }

    public void setPositionInPartList(int positionInPartList) {
        this.positionInPartList = positionInPartList;
    }

    public int getPositionInPartItemList() {
        return positionInPartItemList;
    }

    public void setPositionInPartItemList(int positionInPartItemList) {
        this.positionInPartItemList = positionInPartItemList;
    }
}
