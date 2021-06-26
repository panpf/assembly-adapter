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
package com.github.panpf.assemblyadapter.pager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.github.panpf.assemblyadapter.Placeholder

abstract class BindingPagerPlaceholderItemFactory<VIEW_BINDING : ViewBinding> :
    PagerPlaceholderItemFactory() {

    abstract fun createViewBinding(
        context: Context,
        inflater: LayoutInflater,
        parent: ViewGroup,
        position: Int,
        data: Placeholder
    ): VIEW_BINDING

    override fun createView(
        context: Context,
        container: ViewGroup,
        position: Int,
        data: Placeholder
    ): View {
        return createViewBinding(
            context,
            LayoutInflater.from(container.context),
            container,
            position,
            data
        ).root
    }

}