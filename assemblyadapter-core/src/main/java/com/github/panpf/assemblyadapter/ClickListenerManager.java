package com.github.panpf.assemblyadapter;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

import com.github.panpf.assemblyadapter.core.R;

import java.util.LinkedList;
import java.util.List;

public class ClickListenerManager<DATA> {

    @NonNull
    private final List<Object> holders = new LinkedList<>();

    public void add(@IdRes int viewId, @NonNull OnClickListener<DATA> onClickListener) {
        holders.add(new ClickListenerHolder<>(viewId, onClickListener));
    }

    public void add(@NonNull OnClickListener<DATA> onClickListener) {
        holders.add(new ClickListenerHolder<>(onClickListener));
    }

    public void add(@IdRes int viewId, @NonNull OnLongClickListener<DATA> onClickListener) {
        holders.add(new LongClickListenerHolder<>(viewId, onClickListener));
    }

    public void add(@NonNull OnLongClickListener<DATA> onClickListener) {
        holders.add(new LongClickListenerHolder<>(onClickListener));
    }

    public void register(@NonNull Item<DATA> item, @NonNull View itemView) {
        for (final Object holder : holders) {
            if (holder instanceof ClickListenerHolder) {
                //noinspection unchecked
                final ClickListenerHolder<DATA> clickListenerHolder = (ClickListenerHolder<DATA>) holder;
                final int viewId = clickListenerHolder.viewId;
                final View targetView = viewId > 0 ? itemView.findViewById(viewId) : itemView;
                if (targetView == null) {
                    throw new IllegalArgumentException("Not found click bind target view by id " + viewId);
                }

                targetView.setTag(R.id.aa_tag_item, item);
                targetView.setOnClickListener(view -> {
                    //noinspection unchecked
                    Item<DATA> item1 = (Item<DATA>) targetView.getTag(R.id.aa_tag_item);
                    clickListenerHolder.listener.onViewClick(view.getContext(), view, item1.getPosition(), item1.getData());
                });
            } else if (holder instanceof LongClickListenerHolder) {
                //noinspection unchecked
                final LongClickListenerHolder<DATA> longClickListenerHolder = (LongClickListenerHolder<DATA>) holder;
                final int viewId = longClickListenerHolder.viewId;
                final View targetView = viewId > 0 ? itemView.findViewById(viewId) : itemView;
                if (targetView == null) {
                    throw new IllegalArgumentException("Not found long click bind target view by id " + viewId);
                }

                targetView.setTag(R.id.aa_tag_item, item);
                targetView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        //noinspection unchecked
                        Item<DATA> item = (Item<DATA>) targetView.getTag(R.id.aa_tag_item);
                        return longClickListenerHolder.listener.onViewLongClick(v.getContext(), v, item.getPosition(), item.getData());
                    }
                });
            }
        }
    }

    public static class ClickListenerHolder<DATA> {
        @IdRes
        public final int viewId;
        @NonNull
        public final OnClickListener<DATA> listener;

        public ClickListenerHolder(@IdRes int viewId, @NonNull OnClickListener<DATA> listener) {
            this.viewId = viewId;
            this.listener = listener;
        }

        public ClickListenerHolder(@NonNull OnClickListener<DATA> listener) {
            this(0, listener);
        }
    }

    public static class LongClickListenerHolder<DATA> {
        @IdRes
        public final int viewId;
        @NonNull
        public final OnLongClickListener<DATA> listener;

        public LongClickListenerHolder(@IdRes int viewId, @NonNull OnLongClickListener<DATA> listener) {
            this.viewId = viewId;
            this.listener = listener;
        }

        public LongClickListenerHolder(@NonNull OnLongClickListener<DATA> listener) {
            this(0, listener);
        }
    }
}
