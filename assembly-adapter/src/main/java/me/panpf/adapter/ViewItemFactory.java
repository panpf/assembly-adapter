package me.panpf.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ViewItemFactory extends AssemblyItemFactory<Object> {

    @NonNull
    private ViewFactory viewFactory;
    @Nullable
    private Class<?> dataClazz;

    public ViewItemFactory(@NonNull ViewFactory viewFactory, @Nullable Class<?> dataClazz) {
        this.viewFactory = viewFactory;
        this.dataClazz = dataClazz;
    }

    public ViewItemFactory(@NonNull ViewFactory viewFactory) {
        this(viewFactory, null);
    }

    public ViewItemFactory(@LayoutRes final int layoutResId, @Nullable Class<?> dataClazz) {
        this(new ViewFactory() {
            @NonNull
            @Override
            public View createItemView(@NonNull ViewGroup parent) {
                return LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
            }
        }, dataClazz);
    }

    public ViewItemFactory(@LayoutRes final int layoutResId) {
        this(layoutResId, null);
    }

    public ViewItemFactory(@NonNull final View view, @Nullable Class<?> dataClazz) {
        this(new ViewFactory() {
            @NonNull
            @Override
            public View createItemView(@NonNull ViewGroup parent) {
                return view;
            }
        }, dataClazz);
    }

    public ViewItemFactory(@NonNull final View view) {
        this(view, null);
    }

    @Override
    public boolean isTarget(@Nullable Object data) {
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

    public class ViewItem extends AssemblyItem<Object> {

        ViewItem(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onSetData(int position, @Nullable Object o) {

        }
    }
}
