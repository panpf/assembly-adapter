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

import android.util.SparseArray;
import android.util.SparseIntArray;

import androidx.annotation.NonNull;

import com.github.panpf.assemblyadapter.list.concat.ConcatListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Used by {@link ConcatListAdapter} to isolate view types between nested adapters, if necessary.
 */
interface ExpandableListViewTypeStorage {
    @NonNull
    NestedExpandableListAdapterWrapper getWrapperForGlobalType(int globalViewType);

    @NonNull
    ViewTypeLookup createViewTypeWrapper(
            @NonNull NestedExpandableListAdapterWrapper wrapper
    );

    /**
     * Api given to {@link NestedExpandableListAdapterWrapper}s.
     */
    interface ViewTypeLookup {
        int localToGlobal(int localType);

        int globalToLocal(int globalType);

        void dispose();
    }

    class SharedIdRangeViewTypeStorage implements ExpandableListViewTypeStorage {
        // we keep a list of nested wrappers here even though we only need 1 to create because
        // they might be removed.
        SparseArray<List<NestedExpandableListAdapterWrapper>> mGlobalTypeToWrapper = new SparseArray<>();

        @NonNull
        @Override
        public NestedExpandableListAdapterWrapper getWrapperForGlobalType(int globalViewType) {
            List<NestedExpandableListAdapterWrapper> nestedExpandableAdapterWrappers = mGlobalTypeToWrapper.get(
                    globalViewType);
            if (nestedExpandableAdapterWrappers == null || nestedExpandableAdapterWrappers.isEmpty()) {
                throw new IllegalArgumentException("Cannot find the wrapper for global view"
                        + " type " + globalViewType);
            }
            // just return the first one since they are shared
            return nestedExpandableAdapterWrappers.get(0);
        }

        @NonNull
        @Override
        public ExpandableListViewTypeStorage.ViewTypeLookup createViewTypeWrapper(
                @NonNull NestedExpandableListAdapterWrapper wrapper) {
            return new WrapperViewTypeLookup(wrapper);
        }

        void removeWrapper(@NonNull NestedExpandableListAdapterWrapper wrapper) {
            for (int i = mGlobalTypeToWrapper.size() - 1; i >= 0; i--) {
                List<NestedExpandableListAdapterWrapper> wrappers = mGlobalTypeToWrapper.valueAt(i);
                if (wrappers.remove(wrapper)) {
                    if (wrappers.isEmpty()) {
                        mGlobalTypeToWrapper.removeAt(i);
                    }
                }
            }
        }

        class WrapperViewTypeLookup implements ExpandableListViewTypeStorage.ViewTypeLookup {
            final NestedExpandableListAdapterWrapper mWrapper;

            WrapperViewTypeLookup(NestedExpandableListAdapterWrapper wrapper) {
                mWrapper = wrapper;
            }

            @Override
            public int localToGlobal(int localType) {
                // register it first
                List<NestedExpandableListAdapterWrapper> wrappers = mGlobalTypeToWrapper.get(
                        localType);
                if (wrappers == null) {
                    wrappers = new ArrayList<>();
                    mGlobalTypeToWrapper.put(localType, wrappers);
                }
                if (!wrappers.contains(mWrapper)) {
                    wrappers.add(mWrapper);
                }
                return localType;
            }

            @Override
            public int globalToLocal(int globalType) {
                return globalType;
            }

            @Override
            public void dispose() {
                removeWrapper(mWrapper);
            }
        }
    }

    class IsolatedViewTypeStorage implements ExpandableListViewTypeStorage {
        SparseArray<NestedExpandableListAdapterWrapper> mGlobalTypeToWrapper = new SparseArray<>();

        int mNextViewType = 0;

        int obtainViewType(NestedExpandableListAdapterWrapper wrapper) {
            int nextId = mNextViewType++;
            mGlobalTypeToWrapper.put(nextId, wrapper);
            return nextId;
        }

        @NonNull
        @Override
        public NestedExpandableListAdapterWrapper getWrapperForGlobalType(int globalViewType) {
            NestedExpandableListAdapterWrapper wrapper = mGlobalTypeToWrapper.get(
                    globalViewType);
            if (wrapper == null) {
                throw new IllegalArgumentException("Cannot find the wrapper for global"
                        + " view type " + globalViewType);
            }
            return wrapper;
        }

        @Override
        @NonNull
        public ExpandableListViewTypeStorage.ViewTypeLookup createViewTypeWrapper(
                @NonNull NestedExpandableListAdapterWrapper wrapper) {
            return new WrapperViewTypeLookup(wrapper);
        }

        void removeWrapper(@NonNull NestedExpandableListAdapterWrapper wrapper) {
            for (int i = mGlobalTypeToWrapper.size() - 1; i >= 0; i--) {
                NestedExpandableListAdapterWrapper existingWrapper = mGlobalTypeToWrapper.valueAt(i);
                if (existingWrapper == wrapper) {
                    mGlobalTypeToWrapper.removeAt(i);
                }
            }
        }

        class WrapperViewTypeLookup implements ExpandableListViewTypeStorage.ViewTypeLookup {
            private SparseIntArray mLocalToGlobalMapping = new SparseIntArray(1);
            private SparseIntArray mGlobalToLocalMapping = new SparseIntArray(1);
            final NestedExpandableListAdapterWrapper mWrapper;

            WrapperViewTypeLookup(NestedExpandableListAdapterWrapper wrapper) {
                mWrapper = wrapper;
            }

            @Override
            public int localToGlobal(int localType) {
                int index = mLocalToGlobalMapping.indexOfKey(localType);
                if (index > -1) {
                    return mLocalToGlobalMapping.valueAt(index);
                }
                // get a new key.
                int globalType = obtainViewType(mWrapper);
                mLocalToGlobalMapping.put(localType, globalType);
                mGlobalToLocalMapping.put(globalType, localType);
                return globalType;
            }

            @Override
            public int globalToLocal(int globalType) {
                int index = mGlobalToLocalMapping.indexOfKey(globalType);
                if (index < 0) {
                    throw new IllegalStateException("requested global type " + globalType + " does"
                            + " not belong to the adapter:" + mWrapper.adapter);
                }
                return mGlobalToLocalMapping.valueAt(index);
            }

            @Override
            public void dispose() {
                removeWrapper(mWrapper);
            }
        }
    }
}
