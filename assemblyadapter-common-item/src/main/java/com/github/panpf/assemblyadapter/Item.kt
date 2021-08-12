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
package com.github.panpf.assemblyadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Item is similar to ViewHolder, responsible for holding item view and binding data
 */
abstract class Item<DATA : Any>(val itemView: View) {

    private var _data: DATA? = null
    private var _bindingAdapterPosition: Int = -1
    private var _absoluteAdapterPosition: Int = -1

    val context: Context = itemView.context

    /**
     * Get the bound data, or null if there is none
     */
    val dataOrNull: DATA?
        get() = _data


    /**
     * Get the bound data, throw an exception if there is none
     */
    val dataOrThrow: DATA
        get() = _data!!

    /**
     * The position of the current item in its directly bound adapter.
     * For its specific meaning, please refer to the RecyclerView.ViewHolder.getBindingAdapterPosition() method
     *
     * This value will be different when using Concat*Adapter
     */
    val bindingAdapterPosition: Int
        get() = _bindingAdapterPosition

    /**
     * The position of the current item in the RecyclerView.adapter adapter.
     * For the specific meaning, please refer to the RecyclerView.ViewHolder.getAbsoluteAdapterPosition() method
     *
     * This value will be different when using Concat*Adapter
     */
    val absoluteAdapterPosition: Int
        get() = _absoluteAdapterPosition

    /**
     * Create Item by layout id and parent ViewGroup
     */
    constructor(itemLayoutId: Int, parent: ViewGroup) : this(
        LayoutInflater.from(parent.context).inflate(itemLayoutId, parent, false)
    )

    /**
     * Bind data to item view
     *
     * @param bindingAdapterPosition The position of the current item in its directly bound adapter.
     * For its specific meaning, please refer to the RecyclerView.ViewHolder.getBindingAdapterPosition() method.
     * This value will be different when using Concat*Adapter
     * @param absoluteAdapterPosition The position of the current item in the RecyclerView.adapter adapter.
     * For the specific meaning, please refer to the RecyclerView.ViewHolder.getAbsoluteAdapterPosition() method.
     * This value will be different when using Concat*Adapter
     * @param data Data to be bound
     */
    fun dispatchBindData(
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: DATA
    ) {
        this._data = data
        this._bindingAdapterPosition = bindingAdapterPosition
        this._absoluteAdapterPosition = absoluteAdapterPosition
        bindData(_absoluteAdapterPosition, absoluteAdapterPosition, data)
    }

    /**
     * Bind data to item view
     *
     * @param bindingAdapterPosition The position of the current item in its directly bound adapter.
     * For its specific meaning, please refer to the RecyclerView.ViewHolder.getBindingAdapterPosition() method.
     * This value will be different when using Concat*Adapter
     * @param absoluteAdapterPosition The position of the current item in the RecyclerView.adapter adapter.
     * For the specific meaning, please refer to the RecyclerView.ViewHolder.getAbsoluteAdapterPosition() method.
     * This value will be different when using Concat*Adapter
     * @param data Data to be bound
     */
    protected abstract fun bindData(
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: DATA
    )
}