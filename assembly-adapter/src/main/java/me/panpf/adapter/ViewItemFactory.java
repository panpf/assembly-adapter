package me.panpf.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ViewItemFactory extends AssemblyItemFactory<ViewItemFactory.ViewItem> {

    @NonNull
    private ViewFactory viewFactory;
    @Nullable
    private Class<?> dataClazz;
    @Nullable
    private OnClickViewListener listener;

    public ViewItemFactory(@NonNull ViewFactory viewFactory, @Nullable Class<?> dataClazz, @Nullable OnClickViewListener listener) {
        this.viewFactory = viewFactory;
        this.dataClazz = dataClazz;
        this.listener = listener;
    }

    public ViewItemFactory(@NonNull ViewFactory viewFactory, @Nullable Class<?> dataClazz) {
        this(viewFactory, dataClazz, null);
    }

    public ViewItemFactory(@NonNull ViewFactory viewFactory, @Nullable OnClickViewListener listener) {
        this(viewFactory, null, listener);
    }

    public ViewItemFactory(@NonNull ViewFactory viewFactory) {
        this(viewFactory, null, null);
    }

    public ViewItemFactory(@LayoutRes final int layoutResId, @Nullable Class<?> dataClazz, @Nullable OnClickViewListener listener) {
        this(new ViewFactory() {
            @NonNull
            @Override
            public View createItemView(@NonNull ViewGroup parent) {
                return LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
            }
        }, dataClazz, listener);
    }

    public ViewItemFactory(@LayoutRes final int layoutResId, @Nullable Class<?> dataClazz) {
        this(layoutResId, dataClazz, null);
    }

    public ViewItemFactory(@LayoutRes final int layoutResId, @Nullable OnClickViewListener listener) {
        this(layoutResId, null, listener);
    }

    public ViewItemFactory(@LayoutRes final int layoutResId) {
        this(layoutResId, null, null);
    }

    public ViewItemFactory(@NonNull final View view, @Nullable Class<?> dataClazz, @Nullable OnClickViewListener listener) {
        this(new ViewFactory() {
            @NonNull
            @Override
            public View createItemView(@NonNull ViewGroup parent) {
                return view;
            }
        }, dataClazz, listener);
    }

    public ViewItemFactory(@NonNull final View view, @Nullable Class<?> dataClazz) {
        this(view, dataClazz, null);
    }

    public ViewItemFactory(@NonNull final View view, @Nullable OnClickViewListener listener) {
        this(view, null, listener);
    }

    public ViewItemFactory(@NonNull final View view) {
        this(view, null, null);
    }

    @Override
    public boolean isTarget(@NonNull Object data) {
        return dataClazz == null || dataClazz.isInstance(data);
    }

    @NonNull
    @Override
    public ViewItem createAssemblyItem(@NonNull ViewGroup parent) {
        return new ViewItem(viewFactory.createItemView(parent));
    }

    public interface ViewFactory {
        @NonNull
        View createItemView(@NonNull ViewGroup parent);
    }

    public interface OnClickViewListener {
        void onClickView(int position, @Nullable Object data);
    }

    public class ViewItem extends AssemblyItem<Object> {

        ViewItem(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onConfigViews(@NonNull Context context) {
            if (listener != null) {
                getItemView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onClickView(getAdapterPosition(), getData());
                    }
                });
            }
        }

        @Override
        protected void onSetData(int position, @NonNull Object o) {

        }
    }
}
