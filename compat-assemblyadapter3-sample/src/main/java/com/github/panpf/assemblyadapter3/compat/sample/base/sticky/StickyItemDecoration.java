/*
 * Copyright (C) 2021 panpf <panpfpanpf@outlook.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.panpf.assemblyadapter3.compat.sample.base.sticky;

import android.graphics.Canvas;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.github.panpf.assemblyadapter.recycler.ConcatAdapterLocalHelper;

import kotlin.Pair;

public class StickyItemDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "StickyItemDecoration";
    public static boolean DEBUG = false;

    @NonNull
    private final ViewGroup stickyItemContainer;
    @NonNull
    private final SparseArray<RecyclerView.ViewHolder> viewHolderArray = new SparseArray<>();
    private boolean disabledScrollStickyHeader;
    private boolean invisibleStickyItemInList;
    @Nullable
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    private int stickyItemPosition = -1;
    @Nullable
    private View invisibleItemView;
    @NonNull
    private final ConcatAdapterLocalHelper concatAdapterLocalHelper = new ConcatAdapterLocalHelper();

    public StickyItemDecoration(@NonNull ViewGroup stickyItemContainer) {
        this.stickyItemContainer = stickyItemContainer;
    }

    /**
     * 禁止滑动过程中下一个 sticky header 往上顶当前 sticky header 的效果
     *
     * @param disabledScrollStickyHeader 禁止
     * @return StickyItemDecoration
     */
    public StickyItemDecoration setDisabledScrollStickyHeader(boolean disabledScrollStickyHeader) {
        this.disabledScrollStickyHeader = disabledScrollStickyHeader;
        return this;
    }

    /**
     * 开启滑动过程中 sticky header 显示时隐藏列表中的 sticky item
     *
     * @param invisibleStickyItemInList 开启
     * @return StickyItemDecoration
     */
    public StickyItemDecoration setInvisibleStickyItemInList(boolean invisibleStickyItemInList) {
        this.invisibleStickyItemInList = invisibleStickyItemInList;
        return this;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull final RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);

        if (!checkAdapter(parent) || adapter == null) {
            return;
        }

        final int oldStickItemPosition = stickyItemPosition;
        final int firstVisiblePosition = findFirstVisiblePosition(parent);
        final int newStickItemPosition = findStickyItemPositionToBack(firstVisiblePosition);

        /*
         * 从当前位置往回找最近的一个 sticky item，找到并且是新的就创建 item
         */
        if (newStickItemPosition != -1) {
            if (newStickItemPosition != stickyItemPosition) {
                final int stickyItemType = getItemTypeByPosition(newStickItemPosition);
                if (stickyItemType != -1) {
                    RecyclerView.ViewHolder holder = viewHolderArray.get(stickyItemType);
                    if (holder == null) {
                        holder = adapter.createViewHolder(stickyItemContainer, stickyItemType);
                        viewHolderArray.put(stickyItemType, holder);

                        if (DEBUG) Log.w(TAG, "new sticky item: " + newStickItemPosition);
                    }

                    adapter.bindViewHolder(holder, newStickItemPosition);
                    if (stickyItemContainer.getChildCount() > 0) {
                        stickyItemContainer.removeAllViews();
                    }
                    stickyItemContainer.addView(holder.itemView);
                    stickyItemContainer.setVisibility(View.VISIBLE);

                    if (DEBUG) Log.i(TAG, "change sticky item: " + newStickItemPosition);

                    stickyItemPosition = newStickItemPosition;
                }
            }
        } else {
            stickyItemPosition = -1;
        }

        /*
         * 当前有 sticky item 需要显示就从当前位置往前找下一个 sticky item，找到并且已经跟当前 sticky item 的位置重叠了，就往上顶当前 sticky item
         */
        int offset = -99999;
        int nextStickyViewTop = -99999;
        int oldStickyViewTop = -99999;
        int stickyContainerHeight = -1;
        int nextStickItemPosition = -1;
        if (stickyItemPosition != -1) {
            if (!disabledScrollStickyHeader) {
                offset = 0;
                stickyContainerHeight = stickyItemContainer.getHeight();
                nextStickItemPosition = findStickyItemPositionToNext(parent, firstVisiblePosition);
                if (nextStickItemPosition > stickyItemPosition) {
                    View nextStickyItemView = findViewByPosition(parent, nextStickItemPosition);
                    if (nextStickyItemView != null) {
                        nextStickyViewTop = nextStickyItemView.getTop();
                        if (nextStickyViewTop >= 0 && nextStickyViewTop <= stickyContainerHeight) {
                            offset = nextStickyViewTop - stickyContainerHeight;
                        }
                    }
                }
                if (stickyItemContainer.getChildCount() > 0) {
                    View stickyView = stickyItemContainer.getChildAt(0);
                    oldStickyViewTop = stickyView.getTop();
                    ViewCompat.offsetTopAndBottom(stickyView, offset - oldStickyViewTop);
                }
            }
            stickyItemContainer.setVisibility(View.VISIBLE);
        } else {
            if (stickyItemContainer.getChildCount() > 0) {
                stickyItemContainer.removeAllViews();
            }
            stickyItemContainer.setVisibility(View.INVISIBLE);
        }

        /*
         * 列表中的 item 需要在 sticky item 显示的时候隐藏，划出列表的时候恢复显示
         */
        if (invisibleStickyItemInList) {
            if (stickyItemPosition != -1 && stickyItemPosition == firstVisiblePosition) {
                View stickyItemView = findViewByPosition(parent, stickyItemPosition);
                // stickyItemView.getTop() == 0 时隐藏 stickyItemView 会导致 sticky header 区域闪烁一下，这是因为在 sticky header 显示出来之前隐藏了 stickyItemView
                // 因此限定 stickyItemView.getTop() < 0 也就是说 sticky item 和 sticky header 错开的时候隐藏 sticky item 可以一定程度上避免闪烁，但滑动的快了还是会闪烁一下
                if (stickyItemView != null && stickyItemView.getVisibility() != View.INVISIBLE && stickyItemView.getTop() < 0) {
                    stickyItemView.setVisibility(View.INVISIBLE);
                    invisibleItemView = stickyItemView;

                    if (DEBUG) Log.i(TAG, "hidden in list sticky item: " + stickyItemPosition);
                }
            } else if (invisibleItemView != null && invisibleItemView.getVisibility() != View.VISIBLE) {
                invisibleItemView.setVisibility(View.VISIBLE);

                if (DEBUG) Log.i(TAG, "resume in list sticky item: " + stickyItemPosition);
            }
        }

        if (DEBUG) {
            Log.d(TAG, String.format(
                    "firstVisibleP: %d, oldStickP: %d, newStickP: %d, stickyP: %d, " +
                            "nextStickP: %d, nextStickyTop: %d, containerHeight: %d, oldStickyTop: %d, offset: %d",
                    firstVisiblePosition, oldStickItemPosition, newStickItemPosition, stickyItemPosition,
                    nextStickItemPosition, nextStickyViewTop, stickyContainerHeight, oldStickyViewTop, offset));
        }
    }

    /**
     * 从当前位置往回查找悬停 item
     */
    private int findStickyItemPositionToBack(int formPosition) {
        if (adapter != null && formPosition >= 0) {
            for (int position = formPosition; position >= 0; position--) {
                if (isStickyItemByPosition(adapter, position)) {
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
        if (adapter != null && formPosition >= 0) {
            int lastVisibleItemPosition = findLastVisibleItemPosition(recyclerView);
            if (lastVisibleItemPosition >= 0) {
                for (int position = formPosition; position <= lastVisibleItemPosition; position++) {
                    if (isStickyItemByPosition(adapter, position)) {
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
        return adapter != null ? adapter.getItemViewType(position) : -1;
    }

    /**
     * 根据类型判断是否是悬停 item
     */
    public final boolean isStickyItemByPosition(@NonNull RecyclerView.Adapter<?> adapter, int position) {
        Pair<RecyclerView.Adapter<?>, Integer> localPair = concatAdapterLocalHelper.findLocalAdapterAndPosition(adapter, position);
        return isStickyItemByPositionReal(localPair.getFirst(), localPair.getSecond());
    }

    protected boolean isStickyItemByPositionReal(@NonNull RecyclerView.Adapter<?> adapter, int position) {
        if (adapter instanceof StickyAdapter) {
            return ((StickyAdapter) adapter).isStickyItemByPosition(position);
        } else {
            return false;
        }
    }

    private boolean checkAdapter(@NonNull RecyclerView parent) {
        //noinspection unchecked
        @Nullable
        RecyclerView.Adapter<RecyclerView.ViewHolder> adapter = parent.getAdapter();
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
        stickyItemPosition = -1;
        viewHolderArray.clear();
        if (invisibleItemView != null) {
            invisibleItemView.setVisibility(View.VISIBLE);
            invisibleItemView = null;
        }
    }

    public interface StickyAdapter {
        boolean isStickyItemByPosition(int position);
    }
}
