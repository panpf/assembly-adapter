package com.github.panpf.assemblyadapter.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class BindingAssemblyExpandableItemFactory<DATA, VIEW_BINDING extends androidx.viewbinding.ViewBinding> extends AssemblyExpandableItemFactory<DATA> {

    @NonNull
    @Override
    public AssemblyExpandableItem<DATA> createItem(@NonNull ViewGroup parent) {
        VIEW_BINDING binding = createViewBinding(LayoutInflater.from(parent.getContext()), parent);
        BindingAssemblyExpandableItem<DATA, VIEW_BINDING> item = new BindingAssemblyExpandableItem<>(this, binding);
        initItem(parent.getContext(), binding, item);
        return item;
    }

    public abstract VIEW_BINDING createViewBinding(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent);

    public void initItem(@NonNull Context context, @NonNull VIEW_BINDING binding, @NonNull BindingAssemblyExpandableItem<DATA, VIEW_BINDING> item) {
    }

    public abstract void bindData(@NonNull Context context, @NonNull VIEW_BINDING binding, @NonNull BindingAssemblyExpandableItem<DATA, VIEW_BINDING> item, int position, @Nullable DATA data);

    public static class BindingAssemblyExpandableItem<DATA, VIEW_BINDING extends androidx.viewbinding.ViewBinding> extends AssemblyExpandableItem<DATA> {

        @NonNull
        private final BindingAssemblyExpandableItemFactory<DATA, VIEW_BINDING> factory;
        @NonNull
        private final VIEW_BINDING binding;

        private BindingAssemblyExpandableItem(@NonNull BindingAssemblyExpandableItemFactory<DATA, VIEW_BINDING> factory, @NonNull VIEW_BINDING binding) {
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
