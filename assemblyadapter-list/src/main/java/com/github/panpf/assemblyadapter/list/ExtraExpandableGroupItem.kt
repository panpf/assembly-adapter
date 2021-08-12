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
package com.github.panpf.assemblyadapter.list

import android.view.View
import androidx.collection.ArrayMap

/**
 * [ExpandableGroupItem] that provide extra support
 */
abstract class ExtraExpandableGroupItem<DATA : Any>(itemView: View) :
    ExpandableGroupItem<DATA>(itemView) {

    private var extras: ArrayMap<String, Any>? = null

    /**
     * Save an value to item
     *
     * @param key The key of the value, later you can use the key to get the value through the [getExtraOrNull] or [getExtraOrThrow] method
     * @param value Value to save
     * @see getExtraOrNull
     * @see getExtraOrThrow
     */
    fun putExtra(key: String, value: Any?) {
        (extras ?: ArrayMap<String, Any>().apply {
            this@ExtraExpandableGroupItem.extras = this
        }).apply {
            if (value != null) {
                put(key, value)
            } else {
                remove(key)
            }
        }
    }

    /**
     * Use the given [key] to get value, If it does not exist, return null
     */
    fun <T : Any> getExtraOrNull(key: String): T? {
        return extras?.get(key) as T?
    }

    /**
     * Use the given [key] to get value, Throw an exception if it doesn't exist
     */
    fun <T : Any> getExtraOrThrow(key: String): T {
        return extras?.get(key) as T? ?: throw Exception("Not found extra by key: $key")
    }

    /**
     * Use the given [key] to get value, Return [defaultValue] if it doesn't exist
     */
    fun <T : Any> getExtraOrDefault(key: String, defaultValue: T): T {
        return extras?.get(key) as T? ?: defaultValue
    }

    /**
     * Use the given [key] to get value, Return and put [defaultValue] if it doesn't exist
     */
    fun <T : Any> getExtraOrPut(key: String, defaultValue: () -> T): T {
        return extras?.get(key) as T? ?: defaultValue().apply {
            putExtra(key, this)
        }
    }

    /**
     * Use the given [key] to get value, Return [defaultValue] if it doesn't exist
     */
    fun <T : Any> getExtraOrElse(key: String, defaultValue: () -> T): T {
        return extras?.get(key) as T? ?: defaultValue()
    }
}