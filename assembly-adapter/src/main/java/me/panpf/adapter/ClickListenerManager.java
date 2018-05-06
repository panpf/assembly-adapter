package me.panpf.adapter;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

public class ClickListenerManager {
    @NonNull
    private List<Object> holders = new LinkedList<Object>();

    public void add(@IdRes int viewId, @NonNull OnClickListener onClickListener) {
        holders.add(new ClickListenerHolder(viewId, onClickListener));
    }

    public void add(@NonNull OnClickListener onClickListener) {
        holders.add(new ClickListenerHolder(onClickListener));
    }

    public void add(@IdRes int viewId, @NonNull OnLongClickListener onClickListener) {
        holders.add(new LongClickListenerHolder(viewId, onClickListener));
    }

    public void add(@NonNull OnLongClickListener onClickListener) {
        holders.add(new LongClickListenerHolder(onClickListener));
    }

    public void register(@NonNull final ItemFactory itemFactory, @NonNull Item item, @NonNull View itemView) {
        for (final Object holder : holders) {
            if (holder instanceof ClickListenerHolder) {
                final ClickListenerHolder clickListenerHolder = (ClickListenerHolder) holder;
                int viewId = clickListenerHolder.getViewId();
                final View targetView = viewId > 0 ? itemView.findViewById(viewId) : itemView;
                if (targetView == null) {
                    throw new IllegalArgumentException("Not found target view by id " + viewId);
                }

                targetView.setTag(R.id.aa_item_holder, item);
                targetView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Item item = (Item) targetView.getTag(R.id.aa_item_holder);
                        int position = item.getAdapterPosition();
                        AssemblyAdapter adapter = itemFactory.getAdapter();
                        int positionInPart = adapter != null ? adapter.getPositionInPart(position) : position;
                        clickListenerHolder.getListener().onViewClick(v, position, positionInPart, item.getData());
                    }
                });
            } else if (holder instanceof LongClickListenerHolder) {
                final LongClickListenerHolder longClickListenerHolder = (LongClickListenerHolder) holder;
                int viewId = longClickListenerHolder.getViewId();
                final View targetView = viewId > 0 ? itemView.findViewById(viewId) : itemView;
                if (targetView == null) {
                    throw new IllegalArgumentException("Not found target view by id " + viewId);
                }

                targetView.setTag(R.id.aa_item_holder, item);
                targetView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Item item = (Item) targetView.getTag(R.id.aa_item_holder);
                        int position = item.getAdapterPosition();
                        AssemblyAdapter adapter = itemFactory.getAdapter();
                        int positionInPart = adapter != null ? adapter.getPositionInPart(position) : position;
                        return longClickListenerHolder.getListener().onViewLongClick(v, position, positionInPart, item.getData());
                    }
                });
            }
        }
    }
}
