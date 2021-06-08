package com.github.panpf.assemblyadapter.list;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.github.panpf.assemblyadapter.AssemblyItem;

public abstract class AssemblyExpandableItem<DATA> extends AssemblyItem<DATA> {

    private int groupPosition = -1;
    private int childPosition = -1;
    private boolean isExpanded;
    private boolean isLastChild;

    public AssemblyExpandableItem(@NonNull View itemView) {
        super(itemView);
    }

    public AssemblyExpandableItem(int itemLayoutId, @NonNull ViewGroup parent) {
        super(itemLayoutId, parent);
    }

    void setGroupPosition(int groupPosition) {
        this.groupPosition = groupPosition;
    }

    void setChildPosition(int childPosition) {
        this.childPosition = childPosition;
    }

    void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    void setLastChild(boolean lastChild) {
        isLastChild = lastChild;
    }


    public int getGroupPosition() {
        return groupPosition;
    }

    public int getChildPosition() {
        return childPosition;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public boolean isLastChild() {
        return isLastChild;
    }

    public boolean isGroup() {
        return childPosition == -1;
    }

    public boolean isChild() {
        return childPosition != -1;
    }
}
