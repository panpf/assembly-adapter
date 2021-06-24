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

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Wrapper for each adapter in {@link ConcatListAdapter}.
 */
class NestedListAdapterWrapper {

    public final BaseAdapter adapter;

    private final Callback mCallback;
    @NonNull
    private final ListViewTypeStorage.ViewTypeLookup mViewTypeLookup;
    @NonNull
    private final ListStableIdStorage.StableIdLookup mStableIdLookup;
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
            mCachedItemCount = adapter.getCount();
            mCallback.onChanged(NestedListAdapterWrapper.this);
        }
    };

    NestedListAdapterWrapper(
            @NonNull BaseAdapter adapter,
            @NonNull Callback callback,
            @NonNull ListViewTypeStorage viewTypeStorage,
            @NonNull ListStableIdStorage.StableIdLookup stableIdLookup) {
        this.adapter = adapter;
        this.mCallback = callback;
        this.mViewTypeLookup = viewTypeStorage.createViewTypeWrapper(this);
        this.mStableIdLookup = stableIdLookup;

        this.mCachedItemCount = this.adapter.getCount();
        this.adapter.registerDataSetObserver(mAdapterObserver);
    }


    void dispose() {
        adapter.unregisterDataSetObserver(mAdapterObserver);
        mViewTypeLookup.dispose();
    }

    int getCachedItemCount() {
        return mCachedItemCount;
    }

    int getItemViewTypeCount() {
        return adapter.getViewTypeCount();
    }

    int getItemViewType(int localPosition) {
        return mViewTypeLookup.localToGlobal(adapter.getItemViewType(localPosition));
    }

    public long getItemId(int localPosition) {
        long localItemId = adapter.getItemId(localPosition);
        return mStableIdLookup.localToGlobal(localItemId);
    }

    @NonNull
    View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return adapter.getView(position, convertView, parent);
    }

    @Nullable
    public Object getItem(int position) {
        return adapter.getItem(position);
    }

    interface Callback {
        void onChanged(@NonNull NestedListAdapterWrapper wrapper);
    }
}
