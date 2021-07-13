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
package com.github.panpf.assemblyadapter.pager

import androidx.fragment.app.Fragment
import com.github.panpf.assemblyadapter.internal.Matchable
import kotlin.reflect.KClass

/**
 * FragmentItemFactory is responsible for matching data, creating Fragment
 *
 * When the Adapter needs to display a data, it will find a matching FragmentItemFactory from ItemFactoryStorage
 * through the matchData() method, and then use this FragmentItemFactory to create an Fragment
 *
 * @param DATA Define the type of matching data
 * @param dataClass The class of data that can be matched. By default, as long as the given data is an instance of this class,
 * it is considered a match. You can also override the [exactMatchData] method to achieve exact matching
 * @see ViewFragmentItemFactory
 */
abstract class FragmentItemFactory<DATA : Any>(val dataClass: KClass<DATA>) : Matchable {

    /**
     * If it returns true, it means that this [FragmentItemFactory] can handle this [data]
     */
    final override fun matchData(data: Any): Boolean {
        @Suppress("UNCHECKED_CAST")
        return dataClass.isInstance(data) && exactMatchData(data as DATA)
    }

    /**
     * Exactly match this [data], such as checking the value of a specific attribute
     */
    protected open fun exactMatchData(data: DATA): Boolean = true


    /**
     * When the Adapter needs a new [Fragment] to display data, it will execute this method to create an [Fragment].
     *
     * @param bindingAdapterPosition The position of the current item in its directly bound adapter.
     * For its specific meaning, please refer to the RecyclerView.ViewHolder.getBindingAdapterPosition() method.
     * This value will be different when using Concat*Adapter
     * @param absoluteAdapterPosition The position of the current item in the RecyclerView.adapter adapter.
     * For the specific meaning, please refer to the RecyclerView.ViewHolder.getAbsoluteAdapterPosition() method.
     * This value will be different when using Concat*Adapter
     * @param data Data to be bound
     * @return A new [Fragment]
     * @see createFragment
     * @see Fragment
     */
    fun dispatchCreateFragment(
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: DATA
    ): Fragment {
        return createFragment(bindingAdapterPosition, absoluteAdapterPosition, data)
    }

    /**
     * Create a new [Fragment]. This method can only be called by [dispatchCreateFragment]
     *
     * @param bindingAdapterPosition The position of the current item in its directly bound adapter.
     * For its specific meaning, please refer to the RecyclerView.ViewHolder.getBindingAdapterPosition() method.
     * This value will be different when using Concat*Adapter
     * @param absoluteAdapterPosition The position of the current item in the RecyclerView.adapter adapter.
     * For the specific meaning, please refer to the RecyclerView.ViewHolder.getAbsoluteAdapterPosition() method.
     * This value will be different when using Concat*Adapter
     * @param data Data to be bound
     * @see dispatchCreateFragment
     * @see Fragment
     */
    protected abstract fun createFragment(
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: DATA
    ): Fragment
}