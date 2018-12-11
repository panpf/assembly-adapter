package me.panpf.adapter.paged;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

/**
 * General Object comparison tool areItemsTheSame with memory address comparison, areContentsTheSame with equals
 *
 * @param <T> bean
 */
public class ObjectDiffCallback<T> extends DiffUtil.ItemCallback<T> {

    @Override
    public boolean areItemsTheSame(T oldItem, T newItem) {
        return oldItem == newItem;
    }

    @Override
    public boolean areContentsTheSame(@Nullable T oldItem, @Nullable T newItem) {
        return oldItem == null ? newItem == null : oldItem.equals(newItem);
    }
}
