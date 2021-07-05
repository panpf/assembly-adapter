/*
 * Copyright (C) 2021 panpf <panpfpanpf@oulook.com>
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
package com.github.panpf.assemblyadapter.sample

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.sample.bean.Link
import com.github.panpf.assemblyadapter.sample.bean.ListSeparator
import com.github.panpf.assemblyadapter.sample.databinding.AcivityMainBinding
import com.github.panpf.assemblyadapter.sample.item.LinkItemFactory
import com.github.panpf.assemblyadapter.sample.item.ListSeparatorItemFactory
import com.github.panpf.assemblyadapter.sample.ui.list.ExpandableListFragment
import com.github.panpf.assemblyadapter.sample.ui.list.ExpandableListPlaceholderFragment
import com.github.panpf.assemblyadapter.sample.ui.list.ListFragment
import com.github.panpf.assemblyadapter.sample.ui.list.ListPlaceholderFragment
import com.github.panpf.assemblyadapter.sample.ui.pager.*
import com.github.panpf.assemblyadapter.sample.ui.pager2.Pager2FragmentFragment
import com.github.panpf.assemblyadapter.sample.ui.pager2.Pager2FragmentPagingFragment
import com.github.panpf.assemblyadapter.sample.ui.pager2.Pager2FragmentPlaceholderFragment
import com.github.panpf.assemblyadapter.sample.ui.recycler.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = AcivityMainBinding.inflate(LayoutInflater.from(this), null, false)
        setContentView(binding.root)

        setSupportActionBar(binding.mainToolbar)

        binding.mainRecycler.apply {
            layoutManager = LinearLayoutManager(baseContext)
            adapter = AssemblyRecyclerAdapter(
                listOf(
                    LinkItemFactory(this@MainActivity),
                    ListSeparatorItemFactory(this@MainActivity)
                ),
                listOf(
                    ListSeparator("RecyclerView"),
                    Link("RecyclerView - Linear", RecyclerLinearFragment()),
                    Link("RecyclerView - Grid", RecyclerGridFragment()),
                    Link("RecyclerView - Grid - Staggered", RecyclerGridStaggeredFragment()),
                    Link("RecyclerView - Placeholder", RecyclerPlaceholderFragment()),
                    Link("RecyclerView - Paging - Linear", RecyclerPagingLinearFragment()),
                    Link("RecyclerView - Paging - Grid", RecyclerPagingGridFragment()),
                    Link(
                        "RecyclerView - Paging - Grid - Staggered",
                        RecyclerPagingGridStaggeredFragment()
                    ),
                    Link("RecyclerView - Sticky", RecyclerStickyFragment()),
                    Link("RecyclerView - Sticky - Paging", RecyclerStickyPagingFragment()),

                    ListSeparator("ListView"),
                    Link("ListView", ListFragment()),
                    Link("ListView - Placeholder", ListPlaceholderFragment()),
                    Link("ExpandableListView", ExpandableListFragment()),
                    Link("ExpandableListView - Placeholder", ExpandableListPlaceholderFragment()),

                    ListSeparator("ViewPager"),
                    Link("ViewPager - View", PagerViewFragment()),
                    Link("ViewPager - View - Placeholder", PagerViewPlaceholderFragment()),
                    Link("ViewPager - Fragment - State", PagerFragmentStateFragment()),
                    Link("ViewPager - Fragment - State - Placeholder", PagerFragmentStatePlaceholderFragment()),
                    Link("ViewPager2 - Fragment", Pager2FragmentFragment()),
                    Link("ViewPager2 - Fragment - Placeholder", Pager2FragmentPlaceholderFragment()),
                    Link("ViewPager2 - Fragment - Paging", Pager2FragmentPagingFragment()),
                )
            )
        }
    }
}