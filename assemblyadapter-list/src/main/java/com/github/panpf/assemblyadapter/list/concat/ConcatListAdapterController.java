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

package com.github.panpf.assemblyadapter.list.concat;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Preconditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * All logic for the {@link ConcatListAdapter} is here so that we can clearly see a separation
 * between an adapter implementation and merging logic.
 */
class ConcatListAdapterController implements ListNestedAdapterWrapper.Callback {
    private final ConcatListAdapter mConcatAdapter;

    /**
     * Holds the mapping from the view type to the adapter which reported that type.
     */
    private final ListViewTypeStorage mViewTypeStorage;

    private final List<ListNestedAdapterWrapper> mWrappers = new ArrayList<>();

    // keep one of these around so that we can return wrapper & position w/o allocation ¯\_(ツ)_/¯
    private ListWrapperAndLocalPosition mReusableHolder = new ListWrapperAndLocalPosition();

    @NonNull
    private final ConcatListAdapter.Config.StableIdMode mStableIdMode;

    /**
     * This is where we keep stable ids, if supported
     */
    private final ListStableIdStorage mStableIdStorage;

    private int itemViewTypeCount = -1;

    ConcatListAdapterController(
            ConcatListAdapter concatAdapter,
            ConcatListAdapter.Config config) {
        mConcatAdapter = concatAdapter;

        // setup view type handling
        if (config.isolateViewTypes) {
            mViewTypeStorage = new ListViewTypeStorage.IsolatedViewTypeStorage();
        } else {
            mViewTypeStorage = new ListViewTypeStorage.SharedIdRangeViewTypeStorage();
        }

        // setup stable id handling
        mStableIdMode = config.stableIdMode;
        if (config.stableIdMode == ConcatListAdapter.Config.StableIdMode.NO_STABLE_IDS) {
            mStableIdStorage = new ListStableIdStorage.NoStableIdStorage();
        } else if (config.stableIdMode == ConcatListAdapter.Config.StableIdMode.ISOLATED_STABLE_IDS) {
            mStableIdStorage = new ListStableIdStorage.IsolatedStableIdStorage();
        } else if (config.stableIdMode == ConcatListAdapter.Config.StableIdMode.SHARED_STABLE_IDS) {
            mStableIdStorage = new ListStableIdStorage.SharedPoolStableIdStorage();
        } else {
            throw new IllegalArgumentException("unknown stable id mode");
        }
    }

    @Nullable
    private ListNestedAdapterWrapper findWrapperFor(BaseAdapter adapter) {
        final int index = indexOfWrapper(adapter);
        if (index == -1) {
            return null;
        }
        return mWrappers.get(index);
    }

    private int indexOfWrapper(BaseAdapter adapter) {
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
     * @see ConcatListAdapter#addAdapter(BaseAdapter)
     */
    boolean addAdapter(BaseAdapter adapter) {
        return addAdapter(mWrappers.size(), adapter);
    }

    /**
     * return true if added, false otherwise.
     * throws exception if index is out of bounds
     *
     * @see ConcatListAdapter#addAdapter(int, BaseAdapter)
     */
    @SuppressLint("RestrictedApi")
    boolean addAdapter(int index, BaseAdapter adapter) {
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
                Log.w(ConcatListAdapter.TAG, "Stable ids in the adapter will be ignored as the"
                        + " ConcatListAdapter is configured not to have stable ids");
            }
        }
        ListNestedAdapterWrapper existing = findWrapperFor(adapter);
        if (existing != null) {
            return false;
        }
        ListNestedAdapterWrapper wrapper = new ListNestedAdapterWrapper(adapter, this,
                mViewTypeStorage, mStableIdStorage.createStableIdLookup());
        mWrappers.add(index, wrapper);
        itemViewTypeCount = -1;
        // new items, notify add for them
        if (wrapper.getCachedItemCount() > 0) {
            mConcatAdapter.notifyDataSetChanged();
        }
        return true;
    }

    boolean removeAdapter(BaseAdapter adapter) {
        final int index = indexOfWrapper(adapter);
        if (index == -1) {
            return false;
        }
        ListNestedAdapterWrapper wrapper = mWrappers.get(index);
        mWrappers.remove(index);
        itemViewTypeCount = -1;
        mConcatAdapter.notifyDataSetChanged();
        wrapper.dispose();
        return true;
    }

    public long getItemId(int globalPosition) {
        ListWrapperAndLocalPosition wrapperAndPos = findWrapperAndLocalPosition(globalPosition);
        long globalItemId = wrapperAndPos.mWrapper.getItemId(wrapperAndPos.mLocalPosition);
        releaseWrapperAndLocalPosition(wrapperAndPos);
        return globalItemId;
    }

    @Override
    public void onChanged(@NonNull ListNestedAdapterWrapper wrapper) {
        mConcatAdapter.notifyDataSetChanged();
    }

    public int getTotalCount() {
        // should we cache this as well ?
        int total = 0;
        for (ListNestedAdapterWrapper wrapper : mWrappers) {
            total += wrapper.getCachedItemCount();
        }
        return total;
    }

    public int getItemViewTypeCount() {
        if (itemViewTypeCount == -1) {
            itemViewTypeCount = 0;
            for (ListNestedAdapterWrapper mWrapper : mWrappers) {
                itemViewTypeCount += mWrapper.getItemViewTypeCount();
            }
        }
        return itemViewTypeCount;
    }

    public int getItemViewType(int globalPosition) {
        ListWrapperAndLocalPosition wrapperAndPos = findWrapperAndLocalPosition(globalPosition);
        int itemViewType = wrapperAndPos.mWrapper.getItemViewType(wrapperAndPos.mLocalPosition);
        releaseWrapperAndLocalPosition(wrapperAndPos);
        return itemViewType;
    }

    @NonNull
    public View getView(int globalPosition, @Nullable View convertView, @NonNull ViewGroup parent) {
        ListWrapperAndLocalPosition wrapperAndPos = findWrapperAndLocalPosition(globalPosition);
        View itemView = wrapperAndPos.mWrapper.getView(wrapperAndPos.mLocalPosition, convertView, parent);
        releaseWrapperAndLocalPosition(wrapperAndPos);
        return itemView;
    }

    /**
     * Always call {@link #releaseWrapperAndLocalPosition(ListWrapperAndLocalPosition)} when you are
     * done with it
     */
    @NonNull
    public ListWrapperAndLocalPosition findWrapperAndLocalPosition(int globalPosition, ListWrapperAndLocalPosition wrapperAndLocalPosition) {
        int localPosition = globalPosition;
        for (ListNestedAdapterWrapper wrapper : mWrappers) {
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
     * Always call {@link #releaseWrapperAndLocalPosition(ListWrapperAndLocalPosition)} when you are
     * done with it
     */
    @NonNull
    public ListWrapperAndLocalPosition findWrapperAndLocalPosition(int globalPosition) {
        ListWrapperAndLocalPosition result;
        if (mReusableHolder.mInUse) {
            result = new ListWrapperAndLocalPosition();
        } else {
            mReusableHolder.mInUse = true;
            result = mReusableHolder;
        }
        return findWrapperAndLocalPosition(globalPosition, result);
    }

    private void releaseWrapperAndLocalPosition(ListWrapperAndLocalPosition wrapperAndLocalPosition) {
        wrapperAndLocalPosition.mInUse = false;
        wrapperAndLocalPosition.mWrapper = null;
        wrapperAndLocalPosition.mLocalPosition = -1;
        mReusableHolder = wrapperAndLocalPosition;
    }

    @SuppressWarnings("MixedMutabilityReturnType")
    public List<BaseAdapter> getCopyOfAdapters() {
        if (mWrappers.isEmpty()) {
            return Collections.emptyList();
        }
        List<BaseAdapter> adapters = new ArrayList<>(mWrappers.size());
        for (ListNestedAdapterWrapper wrapper : mWrappers) {
            adapters.add(wrapper.adapter);
        }
        return adapters;
    }

    public boolean hasStableIds() {
        return mStableIdMode != ConcatListAdapter.Config.StableIdMode.NO_STABLE_IDS;
    }

    @Nullable
    public Object getItem(int globalPosition) {
        ListWrapperAndLocalPosition wrapperAndPos = findWrapperAndLocalPosition(globalPosition);
        return wrapperAndPos.mWrapper.getItem(wrapperAndPos.mLocalPosition);
    }
}
