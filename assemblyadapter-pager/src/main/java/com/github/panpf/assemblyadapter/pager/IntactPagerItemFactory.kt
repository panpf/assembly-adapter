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

import android.content.Context
import android.view.View
import android.view.ViewGroup

/**
 * Implementation of [PagerItemFactory], only accept data of type View and return it directly as itemView
 */
open class IntactPagerItemFactory : PagerItemFactory<View>(View::class) {

    override fun createItemView(
        context: Context,
        parent: ViewGroup,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: View
    ): View = data
}