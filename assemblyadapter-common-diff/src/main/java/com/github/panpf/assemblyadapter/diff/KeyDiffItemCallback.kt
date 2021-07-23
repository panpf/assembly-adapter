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
package com.github.panpf.assemblyadapter.diff

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

/**
 * Use key to compare DiffUtil.ItemCallback of item.
 * The areItemsTheSame(DATA, DATA) method uses the key to compare whether it is the same item, so the item needs to implement the DiffKey interface and provide the comparison key;
 * The areContentsTheSame(DATA, DATA) method uses equals to compare whether the contents of the item are the same
 */
class KeyDiffItemCallback<DATA : Any?> : DiffUtil.ItemCallback<DATA>() {

    override fun areItemsTheSame(oldItem: DATA, newItem: DATA): Boolean {
        if (newItem == null || oldItem == null) {
            return oldItem == null && newItem == null
        } else if (oldItem !is DiffKey) {
            throw IllegalArgumentException("${oldItem.javaClass.name} must implement ${DiffKey::class.java.name} interface")
        } else if (newItem !is DiffKey) {
            throw IllegalArgumentException("${newItem.javaClass.name} must implement ${DiffKey::class.java.name} interface")
        }
        @Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
        return (oldItem!!).javaClass == (newItem!!).javaClass && oldItem.diffKey == newItem.diffKey
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DATA, newItem: DATA): Boolean {
        return oldItem == newItem
    }
}