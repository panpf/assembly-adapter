package com.github.panpf.assemblyadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class BindingAssemblyItemFactory<DATA, VIEW_BINDING extends androidx.viewbinding.ViewBinding> extends AssemblyItemFactory<DATA> {

    @NonNull
    @Override
    public AssemblyItem<DATA> createItem(@NonNull ViewGroup parent) {
        VIEW_BINDING binding = createViewBinding(LayoutInflater.from(parent.getContext()), parent);
        BindingAssemblyItem<DATA, VIEW_BINDING> item = new BindingAssemblyItem<>(this, binding);
        initItem(parent.getContext(), binding, item);
        return item;
    }

    public abstract VIEW_BINDING createViewBinding(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent);

    public void initItem(@NonNull Context context, @NonNull VIEW_BINDING binding, @NonNull BindingAssemblyItem<DATA, VIEW_BINDING> item) {
    }

    public abstract void bindData(@NonNull Context context, @NonNull VIEW_BINDING binding, @NonNull BindingAssemblyItem<DATA, VIEW_BINDING> item, int position, @Nullable DATA data);

    public static class BindingAssemblyItem<DATA, VIEW_BINDING extends androidx.viewbinding.ViewBinding> extends AssemblyItem<DATA> {

        @NonNull
        private final BindingAssemblyItemFactory<DATA, VIEW_BINDING> factory;
        @NonNull
        private final VIEW_BINDING binding;

        private BindingAssemblyItem(@NonNull BindingAssemblyItemFactory<DATA, VIEW_BINDING> factory, @NonNull VIEW_BINDING binding) {
            super(binding.getRoot());
            this.factory = factory;
            this.binding = binding;
        }

        @Override
        public void bindData(int position, @Nullable DATA data) {
            factory.bindData(context, binding, this, position, data);
        }
    }
}
