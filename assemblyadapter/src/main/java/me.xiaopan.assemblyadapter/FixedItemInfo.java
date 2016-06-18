package me.xiaopan.assemblyadapter;

public class FixedItemInfo {
    private AssemblyItemFactory itemFactory;
    private Object data;

    public FixedItemInfo(AssemblyItemFactory itemFactory, Object data) {
        this.data = data;
        this.itemFactory = itemFactory;
    }

    public Object getData() {
        return data;
    }

    @SuppressWarnings("unused")
    public AssemblyItemFactory getItemFactory() {
        return itemFactory;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @SuppressWarnings("unused")
    public void setItemFactory(AssemblyItemFactory itemFactory) {
        this.itemFactory = itemFactory;
    }
}
