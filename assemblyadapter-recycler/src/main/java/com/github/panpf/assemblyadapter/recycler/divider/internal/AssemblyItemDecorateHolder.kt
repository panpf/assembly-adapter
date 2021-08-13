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
package com.github.panpf.assemblyadapter.recycler.divider.internal

import androidx.collection.ArrayMap
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.recycler.divider.FindItemFactoryClassByPosition

class AssemblyItemDecorateHolder(
    private val defaultItemDecorateConfig: ItemDecorateHolder,
    private val disableItemDecorateMap: ArrayMap<Class<*>, Boolean>?,
    private val personaliseItemDecorateMap: ArrayMap<Class<*>, ItemDecorate>?,
    private val findItemFactoryClassByPosition: FindItemFactoryClassByPosition,
) : ItemDecorateHolder {

    override fun get(
        parent: RecyclerView,
        position: Int,
    ): ItemDecorate? {
        if (disableItemDecorateMap != null || personaliseItemDecorateMap != null) {
            val adapter = parent.adapter
            val itemFactoryClass = adapter?.let {
                findItemFactoryClassByPosition.find(it, position)
            }
            if (itemFactoryClass != null) {
                if (disableItemDecorateMap?.get(itemFactoryClass) == true) {
                    return null
                }

                val personaliseItemDecorate = personaliseItemDecorateMap?.get(itemFactoryClass)
                if (personaliseItemDecorate != null) {
                    return personaliseItemDecorate
                }
            }
        }
        return defaultItemDecorateConfig.get(parent, position)
    }
}