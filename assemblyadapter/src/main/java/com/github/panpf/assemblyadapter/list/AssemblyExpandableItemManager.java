//package com.github.panpf.assemblyadapter.list;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import com.github.panpf.assemblyadapter.ItemFactory;
//import com.github.panpf.assemblyadapter.internal.ItemManager;
//
//import java.util.ArrayList;
//
//public class AssemblyExpandableItemManager<FACTORY extends ItemFactory<?>> extends ItemManager<FACTORY> {
//
//    @NonNull
//    private final ViewTypeManager<ItemFactory> childViewTypeManager = new ViewTypeManager<>();
//    @NonNull
//    private final ArrayList<ItemFactory> childItemFactoryList = new ArrayList<>();
//
//    public AssemblyExpandableItemManager(@NonNull Callback callback) {
//        super(callback);
//    }
//
//    /**
//     * 添加一个用来处理并显示 dataList 中的 child 数据的 {@link ItemFactory}
//     */
//    public void addChildItemFactory(@NonNull ItemFactory childItemFactory, @NonNull AssemblyAdapter adapter) {
//        //noinspection ConstantConditions
//        if (childItemFactory == null || childViewTypeManager.isLocked()) {
//            throw new IllegalStateException("childItemFactory is null or item factory list locked");
//        }
//
//        childItemFactoryList.add(childItemFactory);
//        int viewType = childViewTypeManager.add(childItemFactory);
//
//        childItemFactory.attachToAdapter(adapter, viewType);
//    }
//
//    @NonNull
//    public ArrayList<ItemFactory> getChildItemFactoryList() {
//        return childItemFactoryList;
//    }
//
//    public int getChildTypeCount() {
//        if (!childViewTypeManager.isLocked()) {
//            childViewTypeManager.lock();
//        }
//        return childViewTypeManager.getCount();
//    }
//
//    @NonNull
//    public ItemFactory getChildItemFactoryByViewType(int viewType) {
//        ItemFactory itemFactory = childViewTypeManager.get(viewType);
//        if (itemFactory != null) {
//            return itemFactory;
//        } else {
//            throw new IllegalArgumentException("Unknown child viewType. viewType=" + viewType);
//        }
//    }
//
//    public int getChildrenCount(int groupPosition) {
//        Object groupObject = getItemDataByPosition(groupPosition);
//        if (groupObject instanceof AssemblyGroup) {
//            return ((AssemblyGroup) groupObject).getChildCount();
//        }
//        return 0;
//    }
//
//    @Nullable
//    public Object getChildDataByPosition(int groupPosition, int childPosition) {
//        Object groupDataObject = getItemDataByPosition(groupPosition);
//        if (groupDataObject == null) {
//            throw new IllegalArgumentException("Not found group item data by group position: " + groupPosition);
//        }
//        if (!(groupDataObject instanceof AssemblyGroup)) {
//            throw new IllegalArgumentException(String.format(
//                    "group object must implements AssemblyGroup interface. groupPosition=%d, groupDataObject=%s",
//                    groupPosition, groupDataObject.getClass().getName()));
//        }
//        return ((AssemblyGroup) groupDataObject).getChild(childPosition);
//    }
//
//    public int getChildViewType(int groupPosition, int childPosition) {
//        if (childItemFactoryList.size() <= 0) {
//            throw new IllegalStateException("You need to configure ItemFactory use addChildItemFactory method");
//        }
//
//        Object childDataObject = getChildDataByPosition(groupPosition, childPosition);
//
//        ItemFactory childItemFactory;
//        for (int w = 0, size = childItemFactoryList.size(); w < size; w++) {
//            childItemFactory = childItemFactoryList.get(w);
//            if (childItemFactory.match(childDataObject)) {
//                return childItemFactory.getViewType();
//            }
//        }
//
//        throw new IllegalStateException(String.format(
//                "Didn't find suitable ItemFactory. groupPosition=%d, childPosition=%d, childDataObject=%s",
//                groupPosition, childPosition, childDataObject != null ? childDataObject.getClass().getName() : "null"));
//    }
//}
