@file:Suppress("RedundantVisibilityModifier")

package me.panpf.arch.ktx

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

@Suppress("unused")
public fun <V : ViewModel> Fragment.bindViewModel(clazz: KClass<V>): ReadOnlyProperty<Fragment, V> {
    return ViewModelLazy { ref, _: KProperty<*> -> ViewModelProvider(this, defaultViewModelProviderFactory).get(clazz.java) }
}

@Suppress("unused")
public fun <V : ViewModel> FragmentActivity.bindViewModel(clazz: KClass<V>): ReadOnlyProperty<FragmentActivity, V> {
    return ViewModelLazy { ref, _: KProperty<*> -> ViewModelProvider(this, defaultViewModelProviderFactory).get(clazz.java) }
}

private class ViewModelLazy<in REF, out OUT>(val initializer: (REF, KProperty<*>) -> OUT) : ReadOnlyProperty<REF, OUT> {
    private object EMPTY

    var viewModel: Any? = EMPTY

    override fun getValue(thisRef: REF, property: KProperty<*>): OUT {
        if (viewModel == EMPTY) {
            viewModel = initializer(thisRef, property)
        }
        @Suppress("UNCHECKED_CAST")
        return viewModel as OUT
    }
}