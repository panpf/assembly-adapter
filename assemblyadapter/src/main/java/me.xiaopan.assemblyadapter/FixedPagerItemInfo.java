package me.xiaopan.assemblyadapter;

public class FixedPagerItemInfo {
    private AssemblyPagerItemFactory itemFactory;
    private Object data;

    public FixedPagerItemInfo(AssemblyPagerItemFactory itemFactory, Object data) {
        this.data = data;
        this.itemFactory = itemFactory;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @SuppressWarnings("unused")
    public AssemblyPagerItemFactory getItemFactory() {
        return itemFactory;
    }
}
