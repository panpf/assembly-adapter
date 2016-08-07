package me.xiaopan.assemblyadapter;

public class FixedGroupItemInfo {
    private AssemblyGroupItemFactory itemFactory;
    private Object data;

    public FixedGroupItemInfo(AssemblyGroupItemFactory itemFactory, Object data) {
        this.data = data;
        this.itemFactory = itemFactory;
    }

    public Object getData() {
        return data;
    }

    @SuppressWarnings("unused")
    public AssemblyGroupItemFactory getItemFactory() {
        return itemFactory;
    }

    public void setData(Object data) {
        this.data = data;
        itemFactory.getAdapter().notifyDataSetChanged();
    }
}
