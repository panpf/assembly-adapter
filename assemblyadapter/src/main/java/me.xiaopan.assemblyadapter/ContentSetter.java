package me.xiaopan.assemblyadapter;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ContentSetter {
    private SparseArray<View> views;
    private View itemView;

    public ContentSetter(View itemView) {
        this.itemView = itemView;
        this.views = new SparseArray<View>();
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    public ViewGroup.LayoutParams getLayoutParams(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && view.getParent() instanceof AbsListView) {
                params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            } else {
                params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        }
        return params;
    }

    public
    @Nullable
    ViewGroup.MarginLayoutParams getLayoutMarginParams(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            if (!(view.getParent() instanceof AbsListView)) {
                params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        }
        return params != null && params instanceof ViewGroup.MarginLayoutParams ? (ViewGroup.MarginLayoutParams) params : null;
    }

    /**
     * ------------View属性-------------
     **/
    @SuppressWarnings("unused")
    public ContentSetter setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    @SuppressWarnings("unused")
    public ContentSetter setBackgroundRes(int viewId, int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    @SuppressWarnings("unused")
    public ContentSetter setBackground(int viewId, Drawable backgroundDrawable) {
        View view = getView(viewId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(backgroundDrawable);
        } else {
            //noinspection deprecation
            view.setBackgroundDrawable(backgroundDrawable);
        }
        return this;
    }

    @SuppressWarnings("unused")
    public ContentSetter setAlpha(int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView(viewId).setAlpha(value);
        } else {
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            getView(viewId).startAnimation(alpha);
        }
        return this;
    }

    @SuppressWarnings("unused")
    public ContentSetter setVisibility(int viewId, @Visibility int visibility) {
        View view = getView(viewId);
        view.setVisibility(visibility);
        return this;
    }

    @SuppressWarnings("unused")
    public ContentSetter setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    @SuppressWarnings("unused")
    public ContentSetter setOnTouchListener(int viewId, View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    @SuppressWarnings("unused")
    public ContentSetter setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

    @SuppressWarnings("unused")
    public ContentSetter setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    @SuppressWarnings("unused")
    public ContentSetter setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    @SuppressWarnings("unused")
    public ContentSetter setSelected(int viewId, boolean selected) {
        View view = getView(viewId);
        view.setSelected(selected);
        return this;
    }

    @SuppressWarnings("unused")
    public ContentSetter setEnabled(int viewId, boolean enabled) {
        View view = getView(viewId);
        view.setEnabled(enabled);
        return this;
    }

    @SuppressWarnings("unused")
    public ContentSetter setFocusable(int viewId, boolean focusable) {
        View view = getView(viewId);
        view.setFocusable(focusable);
        return this;
    }

    @SuppressWarnings("unused")
    public ContentSetter setFocusableInTouchMode(int viewId, boolean focusableInTouchMode) {
        View view = getView(viewId);
        view.setFocusableInTouchMode(focusableInTouchMode);
        return this;
    }

    @SuppressWarnings("unused")
    public ContentSetter setClickable(int viewId, boolean clickable) {
        View view = getView(viewId);
        view.setClickable(clickable);
        return this;
    }

    @SuppressWarnings("unused")
    public ContentSetter setPadding(int viewId, int left, int top, int right, int bottom) {
        View view = getView(viewId);
        view.setPadding(left, top, right, bottom);
        return this;
    }

    @SuppressWarnings("unused")
    public ContentSetter setLayoutSize(int viewId, int width, int height) {
        View view = getView(viewId);
        ViewGroup.LayoutParams params = getLayoutParams(view);
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
        return this;
    }

    @SuppressWarnings("unused")
    public ContentSetter setLayoutMargin(int viewId, int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        View view = getView(viewId);
        ViewGroup.MarginLayoutParams marginParams = getLayoutMarginParams(view);
        if (marginParams != null) {
            marginParams.leftMargin = leftMargin;
            marginParams.topMargin = topMargin;
            marginParams.rightMargin = rightMargin;
            marginParams.bottomMargin = bottomMargin;
            view.setLayoutParams(marginParams);
        }
        return this;
    }


    /**
     * ------------TextView属性-------------
     **/
    @SuppressWarnings("unused")
    public ContentSetter setText(int viewId, CharSequence value) {
        TextView view = getView(viewId);
        view.setText(value);
        return this;
    }

    @SuppressWarnings("unused")
    public ContentSetter setTextColor(int viewId, int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    @SuppressWarnings("unused")
    public ContentSetter setTextColorRes(int viewId, int textColorRes) {
        TextView view = getView(viewId);
        //noinspection deprecation
        view.setTextColor(view.getResources().getColor(textColorRes));
        return this;
    }

    @SuppressWarnings("unused")
    public ContentSetter addLinks(int viewId, @LinkMask int mask) {
        TextView view = getView(viewId);
        Linkify.addLinks(view, mask);
        return this;
    }

    @SuppressWarnings("unused")
    public ContentSetter addLinkAll(int viewId) {
        TextView view = getView(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    @SuppressWarnings("unused")
    public ContentSetter setTypeface(int viewId, Typeface typeface) {
        TextView view = getView(viewId);
        view.setTypeface(typeface);
        view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        return this;
    }

    @SuppressWarnings("unused")
    public ContentSetter setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = getView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }


    /**
     * ------------ImageView属性-------------
     **/
    @SuppressWarnings("unused")
    public ContentSetter setImageResource(int viewId, int imageResId) {
        ImageView view = getView(viewId);
        view.setImageResource(imageResId);
        return this;
    }

    @SuppressWarnings("unused")
    public ContentSetter setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    @SuppressWarnings("unused")
    public ContentSetter setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    @SuppressWarnings("unused")
    public ContentSetter setImageURI(int viewId, Uri uri) {
        ImageView view = getView(viewId);
        view.setImageURI(uri);
        return this;
    }


    /**
     * ------------ProgressBar属性-------------
     **/
    @SuppressWarnings("unused")
    public ContentSetter setProgress(int viewId, int progress) {
        ProgressBar view = getView(viewId);
        view.setProgress(progress);
        return this;
    }

    @SuppressWarnings("unused")
    public ContentSetter setProgress(int viewId, int progress, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    @SuppressWarnings("unused")
    public ContentSetter setMax(int viewId, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        return this;
    }


    /**
     * ------------RatingBar属性-------------
     **/
    @SuppressWarnings("unused")
    public ContentSetter setRating(int viewId, float rating) {
        RatingBar view = getView(viewId);
        view.setRating(rating);
        return this;
    }

    @SuppressWarnings("unused")
    public ContentSetter setRating(int viewId, float rating, int max) {
        RatingBar view = getView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    /**
     * ------------AdapterView属性-------------
     **/
    @SuppressWarnings("unused")
    public ContentSetter setAdapter(int viewId, Adapter adapter) {
        AdapterView view = getView(viewId);
        //noinspection unchecked
        view.setAdapter(adapter);
        return this;
    }

    @SuppressWarnings("unused")
    public ContentSetter setOnItemClickListener(int viewId, AdapterView.OnItemClickListener listener) {
        AdapterView view = getView(viewId);
        view.setOnItemClickListener(listener);
        return this;
    }

    @SuppressWarnings("unused")
    public ContentSetter setOnItemLongClickListener(int viewId, AdapterView.OnItemLongClickListener listener) {
        AdapterView view = getView(viewId);
        view.setOnItemLongClickListener(listener);
        return this;
    }

    @SuppressWarnings("unused")
    public ContentSetter setOnItemSelectedClickListener(int viewId, AdapterView.OnItemSelectedListener listener) {
        AdapterView view = getView(viewId);
        view.setOnItemSelectedListener(listener);
        return this;
    }


    /**
     * ------------CompoundButton属性-------------
     **/
    @SuppressWarnings("unused")
    public ContentSetter setOnCheckedChangeListener(int viewId, CompoundButton.OnCheckedChangeListener listener) {
        CompoundButton view = getView(viewId);
        view.setOnCheckedChangeListener(listener);
        return this;
    }

    @SuppressWarnings("unused")
    public ContentSetter setChecked(int viewId, boolean checked) {
        View view = getView(viewId);
        // View unable cast to Checkable
        if (view instanceof CompoundButton) {
            ((CompoundButton) view).setChecked(checked);
        } else if (view instanceof CheckedTextView) {
            ((CheckedTextView) view).setChecked(checked);
        }
        return this;
    }

    /**
     * ------------RecyclerView属性-------------
     **/
    @SuppressWarnings("unused")
    public ContentSetter setRecyclerAdapter(int viewId, RecyclerView.Adapter adapter) {
        RecyclerView view = getView(viewId);
        view.setAdapter(adapter);
        return this;
    }

    @SuppressWarnings("unused")
    public ContentSetter setLayoutManager(int viewId, RecyclerView.LayoutManager layoutManager) {
        RecyclerView view = getView(viewId);
        view.setLayoutManager(layoutManager);
        return this;
    }

    @IntDef({View.VISIBLE, View.INVISIBLE, View.GONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Visibility {
    }

    @IntDef({Linkify.ALL, Linkify.EMAIL_ADDRESSES, Linkify.MAP_ADDRESSES, Linkify.PHONE_NUMBERS, Linkify.WEB_URLS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LinkMask {
    }
}
