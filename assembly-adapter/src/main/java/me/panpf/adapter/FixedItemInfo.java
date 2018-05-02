package me.panpf.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@SuppressWarnings({"WeakerAccess", "unused"})
public class FixedItemInfo {

    @NonNull
    private ItemFactory itemFactory;
    @Nullable
    private Object data;
    private boolean enabled;
    private int position;
    private boolean header;

    public FixedItemInfo(@NonNull ItemFactory itemFactory, @Nullable Object data, boolean header) {
        this.data = data;
        this.itemFactory = itemFactory;
        this.enabled = true;
        this.header = header;
    }

    @Nullable
    public Object getData() {
        return data;
    }

    public void setData(@Nullable Object data) {
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

    protected void enableChanged() {
        AssemblyAdapter adapter = itemFactory.getAdapter();
        if (adapter != null) {
            if (header) {
                adapter.headerEnabledChanged(this);
            } else {
                adapter.footerEnabledChanged(this);
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
}
