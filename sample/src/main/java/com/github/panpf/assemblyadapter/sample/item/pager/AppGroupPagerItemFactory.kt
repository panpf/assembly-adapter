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
package com.github.panpf.assemblyadapter.sample.item.pager

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.fondesa.recyclerviewdivider.dividerBuilder
import com.github.panpf.assemblyadapter.pager.BindingPagerItemFactory
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.sample.bean.AppGroup
import com.github.panpf.assemblyadapter.sample.databinding.FragmentAppGroupBinding
import com.github.panpf.assemblyadapter.sample.item.AppCardGridItemFactory
import com.github.panpf.tools4a.dimen.ktx.dp2px

class AppGroupPagerItemFactory(private val activity: Activity) :
    BindingPagerItemFactory<AppGroup, FragmentAppGroupBinding>() {

    override fun match(data: Any): Boolean {
        return data is AppGroup
    }

    override fun createItemViewBinding(
        context: Context,
        inflater: LayoutInflater,
        parent: ViewGroup,
        position: Int,
        data: AppGroup
    ): FragmentAppGroupBinding =
        FragmentAppGroupBinding.inflate(inflater, parent, false).apply {
            appGroupGroupNameText.text = data.title
            appGroupAppCountText.text = data.appList.size.toString()
            appGroupRecycler.apply {
                adapter = AssemblyRecyclerAdapter<Any>(
                    listOf(AppCardGridItemFactory(activity)),
                    data.appList
                )
                layoutManager = GridLayoutManager(context, 3)
                addItemDecoration(
                    context.dividerBuilder().asSpace()
                        .showSideDividers().showLastDivider()
                        .size(20.dp2px).build()
                )
            }
        }
}