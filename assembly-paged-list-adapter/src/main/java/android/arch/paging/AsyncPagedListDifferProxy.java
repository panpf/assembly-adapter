package android.arch.paging;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.recyclerview.extensions.AsyncDifferConfig;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.RecyclerView;

public class AsyncPagedListDifferProxy<T> extends AsyncPagedListDiffer<T> {

    public AsyncPagedListDifferProxy(@NonNull RecyclerView.Adapter adapter, @NonNull DiffUtil.ItemCallback<T> diffCallback) {
        super(adapter, diffCallback);
    }

    public AsyncPagedListDifferProxy(@NonNull ListUpdateCallback listUpdateCallback, @NonNull AsyncDifferConfig<T> config) {
        super(listUpdateCallback, config);
    }

    public void setPagedListChangedListener(@Nullable final PagedListChangedListener<T> listener) {
        mListener = listener != null ? new PagedListListener<T>() {
            @Override
            public void onCurrentListChanged(@Nullable PagedList<T> currentList) {
                listener.onCurrentListChanged(currentList);
            }
        } : null;
    }

    public interface PagedListChangedListener<T> {
        void onCurrentListChanged(@Nullable PagedList<T> currentList);
    }
}
