package me.xiaopan.assemblyadapter;

public class FixedGroupItemInfo {
    private AssemblyGroupItemFactory itemFactory;
    private Object data;
    private boolean enabled;
    private int position;
    private boolean header;

    public FixedGroupItemInfo(AssemblyGroupItemFactory itemFactory, Object data, boolean header) {
        this.data = data;
        this.itemFactory = itemFactory;
        this.enabled = true;
        this.header = header;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;

        AssemblyExpandableAdapter adapter = itemFactory.getAdapter();
        if (adapter.isNotifyOnChange()) {
            adapter.notifyDataSetChanged();
        }
    }

    @SuppressWarnings("unused")
    public AssemblyGroupItemFactory getItemFactory() {
        return itemFactory;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @SuppressWarnings("unused")
    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) {
            return;
        }
        this.enabled = enabled;
        if (header) {
            itemFactory.getAdapter().headerEnabledChanged(this);
        } else {
            itemFactory.getAdapter().footerEnabledChanged(this);
        }
    }

    public int getPosition() {
        return position;
    }

    void setPosition(int position) {
        this.position = position;
    }

    @SuppressWarnings("unused")
    public boolean isHeader() {
        return header;
    }
}
