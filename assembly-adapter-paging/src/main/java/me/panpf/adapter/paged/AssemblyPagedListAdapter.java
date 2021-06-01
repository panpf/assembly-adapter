/*
 * Copyright (C) 2017 Peng fei Pan <sky@panpf.me>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.panpf.adapter.paged;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.AsyncPagedListDiffer;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.AdapterListUpdateCallback;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class AssemblyPagedListAdapter<T> extends BasePagingAssemblyRecyclerAdapter<T> {

    private final AsyncPagedListDiffer<T> mDiffer;
    private final AsyncPagedListDiffer.PagedListListener<T> mListener = new AsyncPagedListDiffer.PagedListListener<T>() {
        @Override
        public void onCurrentListChanged(@Nullable PagedList<T> previousList, @Nullable PagedList<T> currentList) {
            AssemblyPagedListAdapter.this.onCurrentListChanged(previousList, currentList);
        }
    };

    /**
     * Creates a PagedListAdapter with default threading and
     * {@link androidx.recyclerview.widget.ListUpdateCallback}.
     * <p>
     * Convenience for PagedListAdapter(AsyncDifferConfig), which uses default threading
     * behavior.
     *
     * @param diffCallback The {@link DiffUtil.ItemCallback DiffUtil.ItemCallback} instance to
     *                     compare items in the list.
     */
    public AssemblyPagedListAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback) {
        mDiffer = new AsyncPagedListDiffer<T>(this, diffCallback);
        mDiffer.addPagedListListener(mListener);
    }

    public AssemblyPagedListAdapter(@NonNull AsyncDifferConfig<T> config) {
        mDiffer = new AsyncPagedListDiffer<T>(new AdapterListUpdateCallback(this), config);
        mDiffer.addPagedListListener(mListener);
    }


    @Nullable
    @Override
    public List getDataList() {
        return mDiffer.getCurrentList();
    }

    @Override
    public int getDataCount() {
        return mDiffer.getItemCount();
    }

    @Nullable
    @Override
    public Object getData(int positionInDataList) {
        return mDiffer.getItem(positionInDataList);
    }

    /**
     * Set the new list to be displayed.
     * <p>
     * If a list is already being displayed, a diff will be computed on a background thread, which
     * will dispatch Adapter.notifyItem events on the main thread.
     *
     * @param pagedList The new list to be displayed.
     */
    public void submitList(PagedList<T> pagedList) {
        mDiffer.submitList(pagedList);
    }

    /**
     * Returns the PagedList currently being displayed by the Adapter.
     * <p>
     * This is not necessarily the most recent list passed to {@link #submitList(PagedList)},
     * because a diff is computed asynchronously between the new list and the current list before
     * updating the currentList value. May be null if no PagedList is being presented.
     *
     * @return The list currently being displayed.
     */
    @Nullable
    public PagedList<T> getCurrentList() {
        return mDiffer.getCurrentList();
    }

    /**
     * Called when the current PagedList is updated.
     * <p>
     * This may be dispatched as part of {@link #submitList(PagedList)} if a background diff isn't
     * needed (such as when the first list is passed, or the list is cleared). In either case,
     * PagedListAdapter will simply call
     * {@link #notifyItemRangeInserted(int, int) notifyItemRangeInserted/Removed(0, mPreviousSize)}.
     * <p>
     * This method will <em>not</em>be called when the Adapter switches from presenting a PagedList
     * to a snapshot version of the PagedList during a diff. This means you cannot observe each
     * PagedList via this method.
     *
     * @param previousList PagedList that was previously displayed, may be null.
     * @param currentList  new PagedList being displayed, may be null.
     * @see #getCurrentList()
     */
    public void onCurrentListChanged(@Nullable PagedList<T> previousList, @Nullable PagedList<T> currentList) {
    }
}