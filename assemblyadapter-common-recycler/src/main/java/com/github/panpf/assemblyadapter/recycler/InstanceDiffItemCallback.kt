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
package com.github.panpf.assemblyadapter.recycler

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

/**
 * Use instance to compare DiffUtil.ItemCallback of item.
 */
class InstanceDiffItemCallback<DATA : Any?> : DiffUtil.ItemCallback<DATA>() {

    override fun areItemsTheSame(oldItem: DATA, newItem: DATA): Boolean {
        return oldItem === newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DATA, newItem: DATA): Boolean {
        return oldItem === newItem
    }
}