//package com.github.panpf.assemblyadapter.recycler;
//
//import android.content.res.TypedArray;
//import android.graphics.Canvas;
//import android.graphics.Rect;
//import android.graphics.drawable.Drawable;
//import android.view.View;
//
//import androidx.annotation.DrawableRes;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
///**
// * {@link RecyclerView} 专用 divider，头和尾巴不显示 divider
// */
//public class AssemblyRecyclerLinerDivider extends RecyclerView.ItemDecoration {
//
//    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
//
//    private Drawable mDivider;
//    private RecyclerView recyclerView;
//
//    public AssemblyRecyclerLinerDivider(RecyclerView recyclerView) {
//        this.recyclerView = recyclerView;
//
//        final TypedArray a = recyclerView.getContext().obtainStyledAttributes(ATTRS);
//        mDivider = a.getDrawable(0);
//        a.recycle();
//    }
//
//    public AssemblyRecyclerLinerDivider(RecyclerView recyclerView, Drawable dividerDrawable) {
//        this.recyclerView = recyclerView;
//        this.mDivider = dividerDrawable;
//    }
//
//    public AssemblyRecyclerLinerDivider(RecyclerView recyclerView, @DrawableRes int dividerDrawableResId) {
//        this(recyclerView, recyclerView.getResources().getDrawable(dividerDrawableResId));
//    }
//
//    @Override
//    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//        if (!(layoutManager instanceof LinearLayoutManager)) {
//            outRect.set(0, 0, 0, 0);
//            return;
//        }
//        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
//        int mOrientation = linearLayoutManager.getOrientation();
//
//        int itemPosition = 0;
//        int firstDataItemPosition = 0;
//        int lastDataItemPosition = 0;
//        RecyclerView.Adapter adapter = recyclerView.getAdapter();
//        AssemblyRecyclerAdapter recyclerAdapter = null;
//        if (adapter instanceof AssemblyRecyclerAdapter) {
//            recyclerAdapter = (AssemblyRecyclerAdapter) adapter;
//            itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
//            firstDataItemPosition = recyclerAdapter.getHeaderCount();
//            lastDataItemPosition = firstDataItemPosition + (recyclerAdapter.getDataCount() - 1);
//        }
//
//        if (recyclerAdapter == null || (itemPosition >= firstDataItemPosition && itemPosition <= lastDataItemPosition)) {
//            if (mOrientation == LinearLayoutManager.VERTICAL) {
//                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
//            } else {
//                outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
//            }
//        } else {
//            outRect.set(0, 0, 0, 0);
//        }
//    }
//
//    @Override
//    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//        if (!(layoutManager instanceof LinearLayoutManager)) {
//            return;
//        }
//        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
//        int mOrientation = linearLayoutManager.getOrientation();
//        if (mOrientation == LinearLayoutManager.VERTICAL) {
//            drawVertical(c, parent);
//        } else {
//            drawHorizontal(c, parent);
//        }
//    }
//
//    private void drawVertical(Canvas c, RecyclerView parent) {
//        int firstDataItemPosition = 0;
//        int lastDataItemPosition = 0;
//        RecyclerView.Adapter adapter = recyclerView.getAdapter();
//        AssemblyRecyclerAdapter recyclerAdapter = null;
//        if (adapter instanceof AssemblyRecyclerAdapter) {
//            recyclerAdapter = (AssemblyRecyclerAdapter) adapter;
//            firstDataItemPosition = recyclerAdapter.getHeaderCount();
//            lastDataItemPosition = firstDataItemPosition + (recyclerAdapter.getDataCount() - 1);
//        }
//
//        final int left = parent.getPaddingLeft();
//        final int right = parent.getWidth() - parent.getPaddingRight();
//
//        final int childCount = parent.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            final View child = parent.getChildAt(i);
//            int itemPosition = ((RecyclerView.LayoutParams) child.getLayoutParams()).getViewLayoutPosition();
//            int bottomMargin = ((RecyclerView.LayoutParams) child.getLayoutParams()).bottomMargin;
//
//            if (recyclerAdapter == null || (itemPosition >= firstDataItemPosition && itemPosition <= lastDataItemPosition)) {
//                final int top = child.getBottom() + bottomMargin;
//                final int bottom = top + mDivider.getIntrinsicHeight();
//                mDivider.setBounds(left, top, right, bottom);
//                mDivider.draw(c);
//            }
//        }
//    }
//
//    private void drawHorizontal(Canvas c, RecyclerView parent) {
//        int firstDataItemPosition = 0;
//        int lastDataItemPosition = 0;
//        RecyclerView.Adapter adapter = recyclerView.getAdapter();
//        AssemblyRecyclerAdapter recyclerAdapter = null;
//        if (adapter instanceof AssemblyRecyclerAdapter) {
//            recyclerAdapter = (AssemblyRecyclerAdapter) adapter;
//            firstDataItemPosition = recyclerAdapter.getHeaderCount();
//            lastDataItemPosition = firstDataItemPosition + (recyclerAdapter.getDataCount() - 1);
//        }
//        final int top = parent.getPaddingTop();
//        final int bottom = parent.getHeight() - parent.getPaddingBottom();
//
//        final int childCount = parent.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            final View child = parent.getChildAt(i);
//            int itemPosition = ((RecyclerView.LayoutParams) child.getLayoutParams()).getViewLayoutPosition();
//            int rightMargin = ((RecyclerView.LayoutParams) child.getLayoutParams()).rightMargin;
//
//            if (recyclerAdapter == null || (itemPosition >= firstDataItemPosition && itemPosition <= lastDataItemPosition)) {
//                final int left = child.getRight() + rightMargin;
//                final int right = left + mDivider.getIntrinsicHeight();
//                mDivider.setBounds(left, top, right, bottom);
//                mDivider.draw(c);
//            }
//        }
//    }
//}
//
