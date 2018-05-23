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
    public static boolean DEBUG = true;

    @NonNull
    private ViewGroup stickyItemContainer;

    @Nullable
    private RecyclerView.Adapter adapter;
    private int cachedStickyPosition = -1;
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
        int stickItemPosition = findStickyItemPositionToBack(firstVisiblePosition);

        /*
         * 从当前位置往回找最近的一个 sticky item，找到并且是新的就创建 item
         */
        if (stickItemPosition != -1) {
            if (stickItemPosition != cachedStickyPosition) {
                int stickyItemType = getItemTypeByPosition(stickItemPosition);
                if (stickyItemType != -1) {
                    RecyclerView.ViewHolder holder = viewHolderArray.get(stickyItemType);
                    if (holder == null) {
                        holder = adapter.createViewHolder(parent, stickyItemType);
                        viewHolderArray.put(stickyItemType, holder);

                        if (DEBUG) {
                            Log.w("sticky", "new sticky item: " + stickItemPosition);
                        }
                    }

                    //noinspection unchecked
                    adapter.bindViewHolder(holder, stickItemPosition);
                    if (stickyItemContainer.getChildCount() > 0) {
                        stickyItemContainer.removeAllViews();
                    }
                    stickyItemContainer.addView(holder.itemView);

                    if (DEBUG) {
                        Log.i("sticky", "change sticky item: " + stickItemPosition);
                    }

                    cachedStickyPosition = stickItemPosition;
                }
            }
        } else {
            cachedStickyPosition = -1;
        }

        /*
         * 当前有 sticky item 需要显示就从当前位置往前找下一个 sticky item，找到并且已经跟当前 sticky item 的位置重叠了，就往上顶当前 sticky item
         */
        int offset = -1;
        int belowViewTop = -1;
        int stickyViewTop = -1;
        int stickyContainerHeight = -1;
        int nextStickPosition = -1;
        if (cachedStickyPosition != -1) {
            offset = 0;
            stickyContainerHeight = stickyItemContainer.getHeight();
            nextStickPosition = findStickyItemPositionToNext(parent, firstVisiblePosition);
            if (nextStickPosition >= 0) {
                View belowItemView = findViewByPosition(parent, nextStickPosition);
                if (belowItemView != null) {
                    belowViewTop = belowItemView.getTop();
                    if (belowViewTop >= 0 && belowViewTop <= stickyContainerHeight) {
                        offset = belowViewTop - stickyContainerHeight;
                    }
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
            Log.d("sticky", String.format("firstVisiblePosition: %d, stickyPosition: %d, newStickPosition: %d, " +
                            "nextStickPosition: %d, belowViewTop: %d, containerHeight: %d, offset: %d, stickyViewTop: %d",
                    firstVisiblePosition, cachedStickyPosition, stickItemPosition, nextStickPosition, belowViewTop, stickyContainerHeight, offset, stickyViewTop));
        }
    }

    /**
     * 从当前位置往回查找悬停 item
     */
    private int findStickyItemPositionToBack(int formPosition) {
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

    /**
     * 从当前位置往前查找悬停 item
     */
    private int findStickyItemPositionToNext(@NonNull RecyclerView recyclerView, int formPosition) {
        if (formPosition >= 0) {
            int lastVisibleItemPosition = findLastVisibleItemPosition(recyclerView);
            if (lastVisibleItemPosition >= 0) {
                for (int position = formPosition; position <= lastVisibleItemPosition; position++) {
                    final int type = getItemTypeByPosition(position);
                    if (isStickyItemByType(type)) {
                        return position;
                    }
                }
            }
        }
        return -1;
    }

    /**
     * 查找列表中第一个可见的 item 的位置
     */
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

    /**
     * 根据位置获取其 view
     */
    private View findViewByPosition(@NonNull RecyclerView recyclerView, int position) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        return layoutManager != null ? layoutManager.findViewByPosition(position) : null;
    }

    /**
     * 获取列表中最后一个可见的 item 的位置
     */
    private int findLastVisibleItemPosition(@NonNull RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int lastVisiblePosition = 0;
        if (layoutManager instanceof GridLayoutManager) {
            lastVisiblePosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof LinearLayoutManager) {
            lastVisiblePosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] mInto = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
            ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(mInto);
            lastVisiblePosition = Integer.MAX_VALUE;
            for (int position : mInto) {
                lastVisiblePosition = Math.min(position, lastVisiblePosition);
            }
        }
        return lastVisiblePosition;
    }

    /**
     * 根据位置获取其类型
     */
    private int getItemTypeByPosition(int position) {
        return adapter instanceof StickyAdapter && position >= 0 && position < adapter.getItemCount() ? adapter.getItemViewType(position) : -1;
    }

    /**
     * 根据类型判断是否是悬停 item
     */
    private boolean isStickyItemByType(int type) {
        return adapter instanceof StickyAdapter && ((StickyAdapter) adapter).isStickyItemByType(type);
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
        cachedStickyPosition = -1;
    }

    public interface StickyAdapter {
        boolean isStickyItemByType(int type);
    }
}
