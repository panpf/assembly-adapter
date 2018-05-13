package me.panpf.adapter.pager;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

import me.panpf.adapter.ClickListenerHolder;
import me.panpf.adapter.LongClickListenerHolder;
import me.panpf.adapter.OnClickListener;
import me.panpf.adapter.OnLongClickListener;
import me.panpf.adapter.R;

public class PagerClickListenerManager<DATA> {
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

    public void register(@NonNull final AssemblyPagerItemFactory<DATA> itemFactory, @NonNull View itemView, int position, DATA data) {
        for (final Object holder : holders) {
            if (holder instanceof ClickListenerHolder) {
                final ClickListenerHolder<DATA> clickListenerHolder = (ClickListenerHolder<DATA>) holder;
                int viewId = clickListenerHolder.getViewId();
                final View targetView = viewId > 0 ? itemView.findViewById(viewId) : itemView;
                if (targetView == null) {
                    throw new IllegalArgumentException("Not found target view by id " + viewId);
                }

                AssemblyPagerAdapter adapter = itemFactory.getAdapter();
                int positionInPart = adapter != null ? adapter.getPositionInPart(position) : position;

                targetView.setTag(R.id.aa_item_position, position);
                targetView.setTag(R.id.aa_item_position_in_port, positionInPart);
                targetView.setTag(R.id.aa_item_data, data);
                targetView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickListenerHolder.getListener().onViewClick(v.getContext(), v, (Integer) v.getTag(R.id.aa_item_position),
                                (Integer) v.getTag(R.id.aa_item_position_in_port), (DATA) v.getTag(R.id.aa_item_data));
                    }
                });
            } else if (holder instanceof LongClickListenerHolder) {
                final LongClickListenerHolder<DATA> longClickListenerHolder = (LongClickListenerHolder<DATA>) holder;
                int viewId = longClickListenerHolder.getViewId();
                final View targetView = viewId > 0 ? itemView.findViewById(viewId) : itemView;
                if (targetView == null) {
                    throw new IllegalArgumentException("Not found target view by id " + viewId);
                }

                AssemblyPagerAdapter adapter = itemFactory.getAdapter();
                int positionInPart = adapter != null ? adapter.getPositionInPart(position) : position;

                targetView.setTag(R.id.aa_item_position, position);
                targetView.setTag(R.id.aa_item_position_in_port, positionInPart);
                targetView.setTag(R.id.aa_item_data, data);
                targetView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return longClickListenerHolder.getListener().onViewLongClick(v.getContext(), v, (Integer) v.getTag(R.id.aa_item_position),
                                (Integer) v.getTag(R.id.aa_item_position_in_port), (DATA) v.getTag(R.id.aa_item_data));
                    }
                });
            }
        }
    }
}
