package me.panpf.adapter.paged;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

/**
 * 通用 Object 对比工具 areItemsTheSame 用内存地址对比，areContentsTheSame 用 equals 对比
 *
 * @param <T> bean 类型
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
