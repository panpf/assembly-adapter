package me.panpf.adapter.sample.adapter;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

public class StickyRecyclerItemDecoration extends RecyclerView.ItemDecoration {

    @SuppressWarnings("WeakerAccess")
    public static boolean DEBUG = false;

    @NonNull
    private ViewGroup stickyItemContainer;

    @Nullable
    private RecyclerView.Adapter adapter;
    private int stickyPosition = -1;
    private SparseArray<RecyclerView.ViewHolder> viewHolderArray = new SparseArray<RecyclerView.ViewHolder>();

    public StickyRecyclerItemDecoration(@NonNull ViewGroup stickyItemContainer) {
        this.stickyItemContainer = stickyItemContainer;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        if (!checkAdapter(parent) || adapter == null) {
            return;
        }

        int firstVisiblePosition = findFirstVisiblePosition(parent);
        int newStickPosition = findStickyItemPosition(firstVisiblePosition);

        if (newStickPosition != -1 && newStickPosition != stickyPosition) {
            stickyPosition = newStickPosition;

            int stickyPositionType = getItemTypeByPosition(stickyPosition);
            if (stickyPositionType != -1) {
                RecyclerView.ViewHolder holder = viewHolderArray.get(stickyPositionType);
                if (holder == null) {
                    holder = adapter.createViewHolder(parent, stickyPositionType);
                    viewHolderArray.put(stickyPositionType, holder);

                    if (DEBUG) {
                        Log.w("sticky", "new sticky item: " + stickyPosition);
                    }
                }

                //noinspection unchecked
                adapter.bindViewHolder(holder, stickyPosition);
                if (stickyItemContainer.getChildCount() > 0) {
                    stickyItemContainer.removeAllViews();
                }
                stickyItemContainer.addView(holder.itemView);

                if (DEBUG) {
                    Log.i("sticky", "change sticky item: " + stickyPosition);
                }
            }
        }

        int offset = -1;
        int belowViewTop = -1;
        int stickyViewTop = -1;
        if (stickyPosition != -1) {
            offset = 0;
            @Nullable
            View belowItemView = parent.findChildViewUnder(c.getWidth() / 2, stickyItemContainer.getHeight() + 0.01f);
            if (belowItemView != null) {
                belowViewTop = belowItemView.getTop();
                if (isStickyItemByView(parent, belowItemView) && belowViewTop > 0) {
                    offset = belowViewTop - stickyItemContainer.getHeight();
                }
            }
            if (stickyItemContainer.getChildCount() > 0) {
                View stickyView = stickyItemContainer.getChildAt(0);
                stickyViewTop = stickyView.getTop();
                ViewCompat.offsetTopAndBottom(stickyView, offset - stickyViewTop);
            }
            stickyItemContainer.setVisibility(View.VISIBLE);
        } else {
            if (stickyItemContainer.getChildCount() > 0) {
                stickyItemContainer.removeAllViews();
            }
            stickyItemContainer.setVisibility(View.INVISIBLE);
        }

        if (DEBUG) {
            Log.d("sticky", String.format("firstVisiblePosition: %d, stickyPosition: %d, newStickPosition: %d, offset: %d, belowViewTop: %d, stickyViewTop: %d",
                    firstVisiblePosition, stickyPosition, newStickPosition, offset, belowViewTop, stickyViewTop));
        }
    }

    /**
     * 从当前位置递减查找悬停 item
     */
    private int findStickyItemPosition(int formPosition) {
        if (formPosition >= 0) {
            for (int position = formPosition; position >= 0; position--) {
                final int type = getItemTypeByPosition(position);
                if (isStickyItemByType(type)) {
                    return position;
                }
            }
        }
        return -1;
    }

    private int findFirstVisiblePosition(@NonNull RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int firstVisiblePosition = 0;
        if (layoutManager instanceof GridLayoutManager) {
            firstVisiblePosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else if (layoutManager instanceof LinearLayoutManager) {
            firstVisiblePosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] mInto = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
            ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(mInto);
            firstVisiblePosition = Integer.MAX_VALUE;
            for (int position : mInto) {
                firstVisiblePosition = Math.min(position, firstVisiblePosition);
            }
        }
        return firstVisiblePosition;
    }

    private int getItemTypeByPosition(int position) {
        return adapter instanceof StickyAdapter && position >= 0 && position < adapter.getItemCount() ? adapter.getItemViewType(position) : -1;
    }

    private boolean isStickyItemByType(int type) {
        return adapter instanceof StickyAdapter && ((StickyAdapter) adapter).isStickyItemByType(type);
    }

    private boolean isStickyItemByView(@NonNull RecyclerView parent, @NonNull View view) {
        final int position = parent.getChildAdapterPosition(view);
        return position != RecyclerView.NO_POSITION && adapter != null && isStickyItemByType(adapter.getItemViewType(position));
    }

    private boolean checkAdapter(@NonNull RecyclerView parent) {
        @Nullable
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (this.adapter != adapter) {
            reset();
            this.adapter = adapter;
            if (this.adapter != null) {
                this.adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onChanged() {
                        reset();
                    }

                    @Override
                    public void onItemRangeChanged(int positionStart, int itemCount) {
                        reset();
                    }

                    @Override
                    public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                        reset();
                    }

                    @Override
                    public void onItemRangeInserted(int positionStart, int itemCount) {
                        reset();
                    }

                    @Override
                    public void onItemRangeRemoved(int positionStart, int itemCount) {
                        reset();
                    }

                    @Override
                    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                        reset();
                    }
                });
            }
        }
        return this.adapter != null;
    }

    private void reset() {
        if (stickyItemContainer.getChildCount() > 0) {
            stickyItemContainer.removeAllViews();
        }
        stickyItemContainer.setVisibility(View.INVISIBLE);
        stickyPosition = -1;
    }

    public interface StickyAdapter {
        boolean isStickyItemByType(int type);
    }
}
