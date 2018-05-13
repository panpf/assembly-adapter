package me.panpf.adapter;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

public class ClickListenerManager<DATA> {
    @NonNull
    private List<Object> holders = new LinkedList<Object>();

    public void add(@IdRes int viewId, @NonNull OnClickListener<DATA> onClickListener) {
        holders.add(new ClickListenerHolder<DATA>(viewId, onClickListener));
    }

    public void add(@NonNull OnClickListener<DATA> onClickListener) {
        holders.add(new ClickListenerHolder<DATA>(onClickListener));
    }

    public void add(@IdRes int viewId, @NonNull OnLongClickListener<DATA> onClickListener) {
        holders.add(new LongClickListenerHolder<DATA>(viewId, onClickListener));
    }

    public void add(@NonNull OnLongClickListener<DATA> onClickListener) {
        holders.add(new LongClickListenerHolder<DATA>(onClickListener));
    }

    public void register(@NonNull final ItemFactory<DATA> itemFactory, @NonNull Item<DATA> item, @NonNull View itemView) {
        for (final Object holder : holders) {
            if (holder instanceof ClickListenerHolder) {
                final ClickListenerHolder<DATA> clickListenerHolder = (ClickListenerHolder<DATA>) holder;
                int viewId = clickListenerHolder.getViewId();
                final View targetView = viewId > 0 ? itemView.findViewById(viewId) : itemView;
                if (targetView == null) {
                    throw new IllegalArgumentException("Not found target view by id " + viewId);
                }

                targetView.setTag(R.id.aa_item_holder, item);
                targetView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Item<DATA> item = (Item<DATA>) targetView.getTag(R.id.aa_item_holder);
                        int position = item.getAdapterPosition();
                        AssemblyAdapter adapter = itemFactory.getAdapter();
                        int positionInPart = adapter != null ? adapter.getPositionInPart(position) : position;
                        clickListenerHolder.getListener().onViewClick(v.getContext(), v, position, positionInPart, item.getData());
                    }
                });
            } else if (holder instanceof LongClickListenerHolder) {
                final LongClickListenerHolder<DATA> longClickListenerHolder = (LongClickListenerHolder<DATA>) holder;
                int viewId = longClickListenerHolder.getViewId();
                final View targetView = viewId > 0 ? itemView.findViewById(viewId) : itemView;
                if (targetView == null) {
                    throw new IllegalArgumentException("Not found target view by id " + viewId);
                }

                targetView.setTag(R.id.aa_item_holder, item);
                targetView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Item<DATA> item = (Item<DATA>) targetView.getTag(R.id.aa_item_holder);
                        int position = item.getAdapterPosition();
                        AssemblyAdapter adapter = itemFactory.getAdapter();
                        int positionInPart = adapter != null ? adapter.getPositionInPart(position) : position;
                        return longClickListenerHolder.getListener().onViewLongClick(v.getContext(), v, position, positionInPart, item.getData());
                    }
                });
            }
        }
    }
}
