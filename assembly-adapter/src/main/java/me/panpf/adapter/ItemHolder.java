package me.panpf.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ItemHolder<DATA> {

    @Nullable
    private ItemStorage itemStorage;
    @NonNull
    private ItemFactory itemFactory;
    @Nullable
    private DATA data;
    private int position;
    private boolean header;

    private boolean enabled = true;

    protected ItemHolder(@NonNull ItemStorage itemStorage, @NonNull ItemFactory itemFactory, @Nullable DATA data, boolean header) {
        this.itemStorage = itemStorage;
        this.itemFactory = itemFactory;
        this.data = data;
        this.header = header;
    }

    public ItemHolder(@NonNull ItemFactory itemFactory, @Nullable DATA data) {
        this.itemFactory = itemFactory;
        this.data = data;
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
    public ItemFactory getItemFactory() {
        return itemFactory;
    }

    @Nullable
    public ItemStorage getItemStorage() {
        return itemStorage;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) {
            return;
        }
        this.enabled = enabled;
        enableChanged();
    }

    void setItemStorage(@NonNull ItemStorage itemStorage) {
        this.itemStorage = itemStorage;
    }

    protected void enableChanged() {
        if (itemStorage != null) {
            if (header) {
                itemStorage.headerEnabledChanged(this);
            } else {
                itemStorage.footerEnabledChanged(this);
            }
        }
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isHeader() {
        return header;
    }

    void setHeader(boolean header) {
        this.header = header;
    }
}
