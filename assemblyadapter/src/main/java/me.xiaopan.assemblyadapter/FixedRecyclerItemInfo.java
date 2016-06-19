package me.xiaopan.assemblyadapter;

public class FixedRecyclerItemInfo {
    private AssemblyRecyclerItemFactory itemFactory;
    private Object data;

    public FixedRecyclerItemInfo(AssemblyRecyclerItemFactory itemFactory, Object data) {
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
    public AssemblyRecyclerItemFactory getItemFactory() {
        return itemFactory;
    }
}
