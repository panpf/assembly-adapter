package me.panpf.adapter.expandable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import me.panpf.adapter.AssemblyExpandableAdapter;
import me.panpf.adapter.ItemFactory;
import me.panpf.adapter.ItemActor;

public class ExpandableItemActor extends ItemActor {

    @NonNull
    private AssemblyExpandableAdapter adapter;

    public ExpandableItemActor(@NonNull AssemblyExpandableAdapter adapter) {
        super(adapter);
        this.adapter = adapter;
    }

    public int getChildrenCount(int groupPosition) {
        Object groupObject = getItem(groupPosition);
        if (groupObject instanceof AssemblyGroup) {
            return ((AssemblyGroup) groupObject).getChildCount();
        }
        return 0;
    }

    @Nullable
    public Object getChild(int groupPosition, int childPosition) {
        Object groupDataObject = getItem(groupPosition);
        if (groupDataObject == null) {
            return null;
        }
        if (!(groupDataObject instanceof AssemblyGroup)) {
            throw new IllegalArgumentException(String.format(
                    "group object must implements AssemblyGroup interface. groupPosition=%d, groupDataObject=%s",
                    groupPosition, groupDataObject.getClass().getName()));
        }
        return ((AssemblyGroup) groupDataObject).getChild(childPosition);
    }

    public int getChildViewType(int groupPosition, int childPosition) {
        if (adapter.getChildItemFactoryCount() <= 0) {
            throw new IllegalStateException("You need to configure ItemFactory use addChildItemFactory method");
        }

        Object childDataObject = getChild(groupPosition, childPosition);

        List<ItemFactory> childItemFactoryList = adapter.getChildItemFactoryList();
        if (childItemFactoryList != null) {
            ItemFactory childItemFactory;
            for (int w = 0, size = childItemFactoryList.size(); w < size; w++) {
                childItemFactory = childItemFactoryList.get(w);
                if (childItemFactory.match(childDataObject)) {
                    return childItemFactory.getViewType();
                }
            }
        }

        throw new IllegalStateException(String.format(
                "Didn't find suitable ItemFactory. groupPosition=%d, childPosition=%d, childDataObject=%s",
                groupPosition, childPosition, childDataObject != null ? childDataObject.getClass().getName() : "null"));
    }
}
