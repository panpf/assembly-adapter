/*
 * Copyright (C) 2017 Peng fei Pan <sky@panpf.me>
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
package com.github.panpf.assemblyadapter3.compat

import android.os.Handler
import android.os.Looper

class CompatMoreFixedItem constructor(val itemFactory: CompatAssemblyMoreItemFactory) {

    var data: CompatMoreState = CompatMoreState.IDLE
        set(value) {
            field = value
            callback?.onChanged(this)
        }
    var callback: Callback? = null
    var isEnabled = true
        set(value) {
            field = value
            callback?.onChanged(this)
            if (value) {
                loadMoreFinished(false)
            }
        }

    val isAttached: Boolean
        get() = callback != null

    private val handler = Handler(Looper.getMainLooper())

    init {
        itemFactory.onLoadCallback = CompatAssemblyMoreItemFactory.OnLoadCallback {
            handler.post {
                data = CompatMoreState.LOADING
            }
        }
    }

    fun loadMoreFinished(end: Boolean) {
        data = if (end) CompatMoreState.END else CompatMoreState.IDLE
    }

    fun loadMoreFailed() {
        data = CompatMoreState.ERROR
    }

    fun interface Callback {
        fun onChanged(fixedItem: CompatMoreFixedItem)
    }
}