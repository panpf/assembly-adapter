package me.panpf.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ViewItem extends AssemblyItem<Object> {

    public ViewItem(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    protected void onSetData(int position, @Nullable Object o) {

    }

    public static class Factory extends AssemblyItemFactory<Object> {

        @NonNull
        private ViewFactory viewFactory;
        @Nullable
        private Class<?> dataClazz;

        public Factory(@NonNull ViewFactory viewFactory, @Nullable Class<?> dataClazz) {
            this.viewFactory = viewFactory;
            this.dataClazz = dataClazz;
        }

        public Factory(@NonNull ViewFactory viewFactory) {
            this(viewFactory, null);
        }

        public Factory(@LayoutRes final int layoutResId, @Nullable Class<?> dataClazz) {
            this(new ViewFactory() {
                @NonNull
                @Override
                public View createItemView(@NonNull ViewGroup parent) {
                    return LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
                }
            }, dataClazz);
        }

        public Factory(@LayoutRes final int layoutResId) {
            this(layoutResId, null);
        }

        public Factory(@NonNull final View view, @Nullable Class<?> dataClazz) {
            this(new ViewFactory() {
                @NonNull
                @Override
                public View createItemView(@NonNull ViewGroup parent) {
                    return view;
                }
            }, dataClazz);
        }

        public Factory(@NonNull final View view) {
            this(view, null);
        }

        @Override
        public boolean match(@Nullable Object data) {
            return dataClazz == null || dataClazz.isInstance(data);
        }

        @NonNull
        @Override
        public ViewItem createAssemblyItem(@NonNull ViewGroup parent) {
            return new ViewItem(viewFactory.createItemView(parent));
        }
    }

    public interface ViewFactory {
        @NonNull
        View createItemView(@NonNull ViewGroup parent);
    }
}
