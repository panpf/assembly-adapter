package me.xiaopan.assemblyadapter;

public class FixedFragmentItemInfo {
    private AssemblyFragmentItemFactory itemFactory;
    private Object data;

    public FixedFragmentItemInfo(AssemblyFragmentItemFactory itemFactory, Object data) {
        this.data = data;
        this.itemFactory = itemFactory;
    }

    public Object getData() {
        return data;
    }

    @SuppressWarnings("unused")
    public AssemblyFragmentItemFactory getItemFactory() {
        return itemFactory;
    }

    public void setData(Object data) {
        this.data = data;
        itemFactory.getAdapter().notifyDataSetChanged();
    }
}
