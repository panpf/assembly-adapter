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
package com.github.panpf.assemblyadapter.pager.internal

import android.view.View
import com.github.panpf.assemblyadapter.OnClickListener

class PagerChickListenerWrapper<DATA : Any>(
    private val onClickListener: OnClickListener<DATA>,
    private val bindingAdapterPosition: Int,
    private val absoluteAdapterPosition: Int,
    private val data: DATA
) : View.OnClickListener {

    override fun onClick(view: View) {
        @Suppress("UNCHECKED_CAST")
        onClickListener.onClick(
            view.context,
            view,
            bindingAdapterPosition,
            absoluteAdapterPosition,
            data
        )
    }
}