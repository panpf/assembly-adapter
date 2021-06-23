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

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Preconditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * All logic for the {@link ConcatExpandableListAdapter} is here so that we can clearly see a separation
 * between an adapter implementation and merging logic.
 */
class ConcatExpandableListAdapterController implements NestedExpandableListAdapterWrapper.Callback {
    private final ConcatExpandableListAdapter mConcatAdapter;

    /**
     * Holds the mapping from the view type to the adapter which reported that type.
     */
    private final ExpandableListViewTypeStorage mViewTypeStorage;

    private final List<NestedExpandableListAdapterWrapper> mWrappers = new ArrayList<>();

    // keep one of these around so that we can return wrapper & position w/o allocation ¯\_(ツ)_/¯
    private ExpandableListWrapperAndLocalPosition mReusableHolder = new ExpandableListWrapperAndLocalPosition();

    @NonNull
    private final ConcatExpandableListAdapter.Config.StableIdMode mStableIdMode;

    /**
     * This is where we keep stable ids, if supported
     */
    private final ExpandableListStableIdStorage mStableIdStorage;

    private int groupItemViewTypeCount = -1;
    private int childItemViewTypeCount = -1;

    ConcatExpandableListAdapterController(
            ConcatExpandableListAdapter concatAdapter,
            ConcatExpandableListAdapter.Config config) {
        mConcatAdapter = concatAdapter;

        // setup view type handling
        if (config.isolateViewTypes) {
            mViewTypeStorage = new ExpandableListViewTypeStorage.IsolatedViewTypeStorage();
        } else {
            mViewTypeStorage = new ExpandableListViewTypeStorage.SharedIdRangeViewTypeStorage();
        }

        // setup stable id handling
        mStableIdMode = config.stableIdMode;
        if (config.stableIdMode == ConcatExpandableListAdapter.Config.StableIdMode.NO_STABLE_IDS) {
            mStableIdStorage = new ExpandableListStableIdStorage.NoStableIdStorage();
        } else if (config.stableIdMode == ConcatExpandableListAdapter.Config.StableIdMode.ISOLATED_STABLE_IDS) {
            mStableIdStorage = new ExpandableListStableIdStorage.IsolatedStableIdStorage();
        } else if (config.stableIdMode == ConcatExpandableListAdapter.Config.StableIdMode.SHARED_STABLE_IDS) {
            mStableIdStorage = new ExpandableListStableIdStorage.SharedPoolStableIdStorage();
        } else {
            throw new IllegalArgumentException("unknown stable id mode");
        }
    }

    @Nullable
    private NestedExpandableListAdapterWrapper findWrapperFor(BaseExpandableListAdapter adapter) {
        final int index = indexOfWrapper(adapter);
        if (index == -1) {
            return null;
        }
        return mWrappers.get(index);
    }

    private int indexOfWrapper(BaseExpandableListAdapter adapter) {
        final int limit = mWrappers.size();
        for (int i = 0; i < limit; i++) {
            if (mWrappers.get(i).adapter == adapter) {
                return i;
            }
        }
        return -1;
    }

    /**
     * return true if added, false otherwise.
     *
     * @see ConcatExpandableListAdapter#addAdapter(BaseExpandableListAdapter)
     */
    boolean addAdapter(BaseExpandableListAdapter adapter) {
        return addAdapter(mWrappers.size(), adapter);
    }

    /**
     * return true if added, false otherwise.
     * throws exception if index is out of bounds
     *
     * @see ConcatExpandableListAdapter#addAdapter(int, BaseExpandableListAdapter)
     */
    @SuppressLint({"RestrictedApi", "LongLogTag"})
    boolean addAdapter(int index, BaseExpandableListAdapter adapter) {
        if (index < 0 || index > mWrappers.size()) {
            throw new IndexOutOfBoundsException("Index must be between 0 and "
                    + mWrappers.size() + ". Given:" + index);
        }
        if (hasStableIds()) {
            Preconditions.checkArgument(adapter.hasStableIds(),
                    "All sub adapters must have stable ids when stable id mode "
                            + "is ISOLATED_STABLE_IDS or SHARED_STABLE_IDS");
        } else {
            if (adapter.hasStableIds()) {
                Log.w(ConcatExpandableListAdapter.TAG, "Stable ids in the adapter will be ignored as the"
                        + " ConcatExpandableListAdapter is configured not to have stable ids");
            }
        }
        NestedExpandableListAdapterWrapper existing = findWrapperFor(adapter);
        if (existing != null) {
            return false;
        }
        NestedExpandableListAdapterWrapper wrapper = new NestedExpandableListAdapterWrapper(adapter, this,
                mViewTypeStorage, mStableIdStorage.createStableIdLookup(), mStableIdStorage.createStableIdLookup());
        mWrappers.add(index, wrapper);
        groupItemViewTypeCount = -1;
        childItemViewTypeCount = -1;
        // new items, notify add for them
        if (wrapper.getCachedItemCount() > 0) {
            mConcatAdapter.notifyDataSetChanged();
        }
        return true;
    }

    boolean removeAdapter(BaseExpandableListAdapter adapter) {
        final int index = indexOfWrapper(adapter);
        if (index == -1) {
            return false;
        }
        NestedExpandableListAdapterWrapper wrapper = mWrappers.get(index);
        mWrappers.remove(index);
        groupItemViewTypeCount = -1;
        childItemViewTypeCount = -1;
        mConcatAdapter.notifyDataSetChanged();
        wrapper.dispose();
        return true;
    }

    @Override
    public void onChanged(@NonNull NestedExpandableListAdapterWrapper wrapper) {
        mConcatAdapter.notifyDataSetChanged();
    }

    public int getGroupCount() {
        // should we cache this as well ?
        int total = 0;
        for (NestedExpandableListAdapterWrapper wrapper : mWrappers) {
            total += wrapper.getCachedItemCount();
        }
        return total;
    }

    @Nullable
    public Object getGroup(int globalGroupPosition) {
        ExpandableListWrapperAndLocalPosition wrapperAndPos = findWrapperAndLocalPosition(globalGroupPosition);
        Object group = wrapperAndPos.mWrapper.getGroup(wrapperAndPos.mLocalPosition);
        releaseWrapperAndLocalPosition(wrapperAndPos);
        return group;
    }

    public long getGroupId(int globalGroupPosition) {
        ExpandableListWrapperAndLocalPosition wrapperAndPos = findWrapperAndLocalPosition(globalGroupPosition);
        long globalGroupId = wrapperAndPos.mWrapper.getGroupId(wrapperAndPos.mLocalPosition);
        releaseWrapperAndLocalPosition(wrapperAndPos);
        return globalGroupId;
    }

    public int getGroupTypeCount() {
        if (groupItemViewTypeCount == -1) {
            groupItemViewTypeCount = 0;
            for (NestedExpandableListAdapterWrapper mWrapper : mWrappers) {
                groupItemViewTypeCount += mWrapper.getGroupTypeCount();
            }
        }
        return groupItemViewTypeCount;
    }

    public int getGroupType(int globalGroupPosition) {
        ExpandableListWrapperAndLocalPosition wrapperAndPos = findWrapperAndLocalPosition(globalGroupPosition);
        int groupType = wrapperAndPos.mWrapper.getGroupType(wrapperAndPos.mLocalPosition);
        releaseWrapperAndLocalPosition(wrapperAndPos);
        return groupType;
    }

    public View getGroupView(int globalGroupPosition, boolean isExpanded, @Nullable View convertView, @NonNull ViewGroup parent) {
        ExpandableListWrapperAndLocalPosition wrapperAndPos = findWrapperAndLocalPosition(globalGroupPosition);
        View groupView = wrapperAndPos.mWrapper.getGroupView(wrapperAndPos.mLocalPosition, isExpanded, convertView, parent);
        releaseWrapperAndLocalPosition(wrapperAndPos);
        return groupView;
    }


    public int getChildrenCount(int globalGroupPosition) {
        ExpandableListWrapperAndLocalPosition wrapperAndPos = findWrapperAndLocalPosition(globalGroupPosition);
        int childrenCount = wrapperAndPos.mWrapper.getChildrenCount(wrapperAndPos.mLocalPosition);
        releaseWrapperAndLocalPosition(wrapperAndPos);
        return childrenCount;
    }

    @Nullable
    public Object getChild(int globalGroupPosition, int childPosition) {
        ExpandableListWrapperAndLocalPosition wrapperAndPos = findWrapperAndLocalPosition(globalGroupPosition);
        Object group = wrapperAndPos.mWrapper.getChild(wrapperAndPos.mLocalPosition, childPosition);
        releaseWrapperAndLocalPosition(wrapperAndPos);
        return group;
    }

    public long getChildId(int globalGroupPosition, int childPosition) {
        ExpandableListWrapperAndLocalPosition wrapperAndPos = findWrapperAndLocalPosition(globalGroupPosition);
        long globalChildId = wrapperAndPos.mWrapper.getChildId(wrapperAndPos.mLocalPosition, childPosition);
        releaseWrapperAndLocalPosition(wrapperAndPos);
        return globalChildId;
    }

    public int getChildTypeCount() {
        if (childItemViewTypeCount == -1) {
            childItemViewTypeCount = 0;
            for (NestedExpandableListAdapterWrapper mWrapper : mWrappers) {
                childItemViewTypeCount += mWrapper.getChildTypeCount();
            }
        }
        return childItemViewTypeCount;
    }

    public int getChildType(int globalGroupPosition, int childPosition) {
        ExpandableListWrapperAndLocalPosition wrapperAndPos = findWrapperAndLocalPosition(globalGroupPosition);
        int childType = wrapperAndPos.mWrapper.getChildType(wrapperAndPos.mLocalPosition, childPosition);
        releaseWrapperAndLocalPosition(wrapperAndPos);
        return childType;
    }

    public View getChildView(int globalGroupPosition, int childPosition, boolean isLastChild, @Nullable View convertView, @NonNull ViewGroup parent) {
        ExpandableListWrapperAndLocalPosition wrapperAndPos = findWrapperAndLocalPosition(globalGroupPosition);
        View childView = wrapperAndPos.mWrapper.getChildView(wrapperAndPos.mLocalPosition, childPosition, isLastChild, convertView, parent);
        releaseWrapperAndLocalPosition(wrapperAndPos);
        return childView;
    }

    public boolean isChildSelectable(int globalGroupPosition, int childPosition) {
        ExpandableListWrapperAndLocalPosition wrapperAndPos = findWrapperAndLocalPosition(globalGroupPosition);
        boolean isChildSelectable = wrapperAndPos.mWrapper.isChildSelectable(wrapperAndPos.mLocalPosition, childPosition);
        releaseWrapperAndLocalPosition(wrapperAndPos);
        return isChildSelectable;
    }

    public void onGroupCollapsed(int globalGroupPosition) {
        ExpandableListWrapperAndLocalPosition wrapperAndPos = findWrapperAndLocalPosition(globalGroupPosition);
        wrapperAndPos.mWrapper.onGroupCollapsed(wrapperAndPos.mLocalPosition);
        releaseWrapperAndLocalPosition(wrapperAndPos);
    }

    public void onGroupExpanded(int globalGroupPosition) {
        ExpandableListWrapperAndLocalPosition wrapperAndPos = findWrapperAndLocalPosition(globalGroupPosition);
        wrapperAndPos.mWrapper.onGroupExpanded(wrapperAndPos.mLocalPosition);
        releaseWrapperAndLocalPosition(wrapperAndPos);
    }

    public boolean hasStableIds() {
        return mStableIdMode != ConcatExpandableListAdapter.Config.StableIdMode.NO_STABLE_IDS;
    }


    /**
     * Always call {@link #releaseWrapperAndLocalPosition(ExpandableListWrapperAndLocalPosition)} when you are
     * done with it
     */
    @NonNull
    public ExpandableListWrapperAndLocalPosition findWrapperAndLocalPosition(int globalPosition, ExpandableListWrapperAndLocalPosition wrapperAndLocalPosition) {
        int localPosition = globalPosition;
        for (NestedExpandableListAdapterWrapper wrapper : mWrappers) {
            if (wrapper.getCachedItemCount() > localPosition) {
                wrapperAndLocalPosition.mWrapper = wrapper;
                wrapperAndLocalPosition.mLocalPosition = localPosition;
                break;
            }
            localPosition -= wrapper.getCachedItemCount();
        }
        if (wrapperAndLocalPosition.mWrapper == null) {
            throw new IllegalArgumentException("Cannot find wrapper for " + globalPosition);
        }
        return wrapperAndLocalPosition;
    }

    /**
     * Always call {@link #releaseWrapperAndLocalPosition(ExpandableListWrapperAndLocalPosition)} when you are
     * done with it
     */
    @NonNull
    public ExpandableListWrapperAndLocalPosition findWrapperAndLocalPosition(int globalGroupPosition) {
        ExpandableListWrapperAndLocalPosition result;
        if (mReusableHolder.mInUse) {
            result = new ExpandableListWrapperAndLocalPosition();
        } else {
            mReusableHolder.mInUse = true;
            result = mReusableHolder;
        }
        return findWrapperAndLocalPosition(globalGroupPosition, result);
    }

    private void releaseWrapperAndLocalPosition(ExpandableListWrapperAndLocalPosition wrapperAndLocalPosition) {
        wrapperAndLocalPosition.mInUse = false;
        wrapperAndLocalPosition.mWrapper = null;
        wrapperAndLocalPosition.mLocalPosition = -1;
        mReusableHolder = wrapperAndLocalPosition;
    }

    @NonNull
    @SuppressWarnings("MixedMutabilityReturnType")
    public List<BaseExpandableListAdapter> getCopyOfAdapters() {
        if (mWrappers.isEmpty()) {
            return Collections.emptyList();
        }
        List<BaseExpandableListAdapter> adapters = new ArrayList<>(mWrappers.size());
        for (NestedExpandableListAdapterWrapper wrapper : mWrappers) {
            adapters.add(wrapper.adapter);
        }
        return adapters;
    }
}
