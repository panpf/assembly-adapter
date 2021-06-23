/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.panpf.assemblyadapter.list.concat.expandable;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.panpf.assemblyadapter.list.expandable.AssemblyExpandableGroup;

/**
 * Wrapper for each adapter in {@link ConcatExpandableListAdapter}.
 */
class NestedExpandableListAdapterWrapper {

    @NonNull
    public final BaseExpandableListAdapter adapter;

    @NonNull
    private final Callback mCallback;
    @NonNull
    private final ExpandableListViewTypeStorage.ViewTypeLookup mGroupViewTypeLookup;
    @NonNull
    private final ExpandableListStableIdStorage.StableIdLookup mGroupStableIdLookup;
    @NonNull
    private final ExpandableListViewTypeStorage.ViewTypeLookup mChildViewTypeLookup;
    @NonNull
    private final ExpandableListStableIdStorage.StableIdLookup mChildStableIdLookup;
    /**
     * we cache this value so that we can know the previous size when change happens
     * this is also important as getting real size while an adapter is dispatching possibly a
     * a chain of events might create inconsistencies (as it happens in DiffUtil).
     * Instead, we always calculate this value based on notify events.
     */
    private int mCachedItemCount;

    private final DataSetObserver mAdapterObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            mCachedItemCount = adapter.getGroupCount();
            mCallback.onChanged(NestedExpandableListAdapterWrapper.this);
        }
    };

    NestedExpandableListAdapterWrapper(
            @NonNull BaseExpandableListAdapter adapter,
            @NonNull Callback callback,
            @NonNull ExpandableListViewTypeStorage viewTypeStorage,
            @NonNull ExpandableListStableIdStorage.StableIdLookup groupStableIdLookup,
            @NonNull ExpandableListStableIdStorage.StableIdLookup childStableIdLookup) {
        this.adapter = adapter;
        this.mCallback = callback;
        this.mGroupViewTypeLookup = viewTypeStorage.createViewTypeWrapper(this);
        this.mGroupStableIdLookup = groupStableIdLookup;
        this.mChildViewTypeLookup = viewTypeStorage.createViewTypeWrapper(this);
        this.mChildStableIdLookup = childStableIdLookup;

        this.mCachedItemCount = this.adapter.getGroupCount();
        this.adapter.registerDataSetObserver(mAdapterObserver);
    }


    void dispose() {
        adapter.unregisterDataSetObserver(mAdapterObserver);
        mGroupViewTypeLookup.dispose();
    }

    int getCachedItemCount() {
        return mCachedItemCount;
    }

    @Nullable
    public Object getGroup(int groupPosition) {
        return adapter.getGroup(groupPosition);
    }

    public long getGroupId(int groupPosition) {
        return mGroupStableIdLookup.localToGlobal(adapter.getGroupId(groupPosition));
    }

    public int getGroupTypeCount() {
        return adapter.getGroupTypeCount();
    }

    public int getGroupType(int groupPosition) {
        return mGroupViewTypeLookup.localToGlobal(adapter.getGroupType(groupPosition));
    }

    @NonNull
    public View getGroupView(int groupPosition, boolean isExpanded, @Nullable View convertView, @NonNull ViewGroup parent) {
        return adapter.getGroupView(groupPosition, isExpanded, convertView, parent);
    }


    public int getChildrenCount(int groupPosition) {
        Object group = adapter.getGroup(groupPosition);
        return group instanceof AssemblyExpandableGroup ? ((AssemblyExpandableGroup) group).getChildCount() : 0;
    }

    @Nullable
    public Object getChild(int groupPosition, int childPosition) {
        Object group = adapter.getGroup(groupPosition);
        return group instanceof AssemblyExpandableGroup ? ((AssemblyExpandableGroup) group).getChild(childPosition) : null;
    }

    public long getChildId(int groupPosition, int childPosition) {
        return mChildStableIdLookup.localToGlobal(adapter.getChildId(groupPosition, childPosition));
    }

    public int getChildTypeCount() {
        return adapter.getChildTypeCount();
    }

    public int getChildType(int groupPosition, int childPosition) {
        return mChildViewTypeLookup.localToGlobal(adapter.getChildType(groupPosition, childPosition));
    }

    @NonNull
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, @Nullable View convertView, @NonNull ViewGroup parent) {
        return adapter.getChildView(groupPosition, childPosition, isLastChild, convertView, parent);
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return adapter.isChildSelectable(groupPosition, childPosition);
    }

    public void onGroupCollapsed(int groupPosition) {
        adapter.onGroupCollapsed(groupPosition);
    }

    public void onGroupExpanded(int groupPosition) {
        adapter.onGroupExpanded(groupPosition);
    }

    interface Callback {
        void onChanged(@NonNull NestedExpandableListAdapterWrapper wrapper);
    }
}
