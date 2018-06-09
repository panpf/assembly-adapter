package me.panpf.adapter.paged;

import android.arch.paging.AsyncPagedListDifferProxy;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.recyclerview.extensions.AsyncDifferConfig;
import android.support.v7.util.AdapterListUpdateCallback;
import android.support.v7.util.DiffUtil;

import me.panpf.adapter.AssemblyRecyclerAdapter;

@SuppressWarnings({"WeakerAccess", "unused"})
public class AssemblyPagedListAdapter<T> extends AssemblyRecyclerAdapter {

    private final AsyncPagedListDifferProxy<T> mDiffer;
    private final AsyncPagedListDifferProxy.PagedListChangedListener<T> mListener = new AsyncPagedListDifferProxy.PagedListChangedListener<T>() {
        @Override
        public void onCurrentListChanged(@Nullable PagedList<T> currentList) {
            AssemblyPagedListAdapter.this.onCurrentListChanged(currentList);
        }
    };

    /**
     * Creates a PagedListAdapter with default threading and
     * {@link android.support.v7.util.ListUpdateCallback}.
     * <p>
     * Convenience for PagedListAdapter(AsyncDifferConfig), which uses default threading
     * behavior.
     *
     * @param diffCallback The {@link DiffUtil.ItemCallback DiffUtil.ItemCallback} instance to
     *                     compare items in the list.
     */
    public AssemblyPagedListAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback) {
        mDiffer = new AsyncPagedListDifferProxy<T>(this, diffCallback);
        mDiffer.setPagedListChangedListener(mListener);
    }

    public AssemblyPagedListAdapter(@NonNull AsyncDifferConfig<T> config) {
        mDiffer = new AsyncPagedListDifferProxy<T>(new AdapterListUpdateCallback(this), config);
        mDiffer.setPagedListChangedListener(mListener);
    }

    /**
     * 使用通用的 {@link ObjectDiffCallback} 作为处理对比
     */
    public AssemblyPagedListAdapter() {
        this(new ObjectDiffCallback<T>());
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
        setDataList(pagedList);
        mDiffer.submitList(pagedList);
    }

//    @Nullable
//    protected T getItem(int position) {
//        return mDiffer.getItem(position);
//    }
//
//    @Override
//    public int getItemCount() {
//        return mDiffer.getItemCount();
//    }

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
     * @param currentList new PagedList being displayed, may be null.
     */
    public void onCurrentListChanged(@Nullable PagedList<T> currentList) {
    }
}