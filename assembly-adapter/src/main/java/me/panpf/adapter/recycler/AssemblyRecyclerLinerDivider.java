package me.panpf.adapter.recycler;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import me.panpf.adapter.AssemblyRecyclerAdapter;

/**
 * {@link RecyclerView} 专用 divider，头和尾巴不显示 divider
 */
@SuppressWarnings("unused")
public class AssemblyRecyclerLinerDivider extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    private Drawable mDivider;
    private RecyclerView recyclerView;

    @SuppressWarnings("unused")
    public AssemblyRecyclerLinerDivider(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;

        final TypedArray a = recyclerView.getContext().obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    public AssemblyRecyclerLinerDivider(RecyclerView recyclerView, Drawable dividerDrawable) {
        this.recyclerView = recyclerView;
        this.mDivider = dividerDrawable;
    }

    @SuppressWarnings("unused")
    public AssemblyRecyclerLinerDivider(RecyclerView recyclerView, @DrawableRes int dividerDrawableResId) {
        this(recyclerView, recyclerView.getResources().getDrawable(dividerDrawableResId));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager == null || !(layoutManager instanceof LinearLayoutManager)) {
            outRect.set(0, 0, 0, 0);
            return;
        }
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
        int mOrientation = linearLayoutManager.getOrientation();

        int itemPosition = 0;
        int firstDataItemPosition = 0;
        int lastDataItemPosition = 0;
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        AssemblyRecyclerAdapter recyclerAdapter = null;
        if (adapter instanceof AssemblyRecyclerAdapter) {
            recyclerAdapter = (AssemblyRecyclerAdapter) adapter;
            itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
            firstDataItemPosition = recyclerAdapter.getHeaderItemCount();
            lastDataItemPosition = firstDataItemPosition + (recyclerAdapter.getDataCount() - 1);
        }

        if (recyclerAdapter == null || (itemPosition >= firstDataItemPosition && itemPosition <= lastDataItemPosition)) {
            if (mOrientation == LinearLayoutManager.VERTICAL) {
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            } else {
                outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
            }
        } else {
            outRect.set(0, 0, 0, 0);
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager == null || !(layoutManager instanceof LinearLayoutManager)) {
            return;
        }
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
        int mOrientation = linearLayoutManager.getOrientation();
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        int firstDataItemPosition = 0;
        int lastDataItemPosition = 0;
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        AssemblyRecyclerAdapter recyclerAdapter = null;
        if (adapter instanceof AssemblyRecyclerAdapter) {
            recyclerAdapter = (AssemblyRecyclerAdapter) adapter;
            firstDataItemPosition = recyclerAdapter.getHeaderItemCount();
            lastDataItemPosition = firstDataItemPosition + (recyclerAdapter.getDataCount() - 1);
        }

        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            int itemPosition = ((RecyclerView.LayoutParams) child.getLayoutParams()).getViewLayoutPosition();
            int bottomMargin = ((RecyclerView.LayoutParams) child.getLayoutParams()).bottomMargin;

            if (recyclerAdapter == null || (itemPosition >= firstDataItemPosition && itemPosition <= lastDataItemPosition)) {
                final int top = child.getBottom() + bottomMargin;
                final int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        int firstDataItemPosition = 0;
        int lastDataItemPosition = 0;
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        AssemblyRecyclerAdapter recyclerAdapter = null;
        if (adapter instanceof AssemblyRecyclerAdapter) {
            recyclerAdapter = (AssemblyRecyclerAdapter) adapter;
            firstDataItemPosition = recyclerAdapter.getHeaderItemCount();
            lastDataItemPosition = firstDataItemPosition + (recyclerAdapter.getDataCount() - 1);
        }
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            int itemPosition = ((RecyclerView.LayoutParams) child.getLayoutParams()).getViewLayoutPosition();
            int rightMargin = ((RecyclerView.LayoutParams) child.getLayoutParams()).rightMargin;

            if (recyclerAdapter == null || (itemPosition >= firstDataItemPosition && itemPosition <= lastDataItemPosition)) {
                final int left = child.getRight() + rightMargin;
                final int right = left + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }
}

