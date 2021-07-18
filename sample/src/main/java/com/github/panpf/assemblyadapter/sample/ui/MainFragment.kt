package com.github.panpf.assemblyadapter.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.bean.Link
import com.github.panpf.assemblyadapter.sample.bean.ListSeparator
import com.github.panpf.assemblyadapter.sample.databinding.FragmentMainBinding
import com.github.panpf.assemblyadapter.sample.item.LinkItemFactory
import com.github.panpf.assemblyadapter.sample.item.ListSeparatorItemFactory
import com.github.panpf.assemblyadapter.sample.ui.list.ExpandableListFragment
import com.github.panpf.assemblyadapter.sample.ui.list.ExpandableListPlaceholderFragment
import com.github.panpf.assemblyadapter.sample.ui.list.ListFragment
import com.github.panpf.assemblyadapter.sample.ui.list.ListPlaceholderFragment
import com.github.panpf.assemblyadapter.sample.ui.pager.PagerFragmentStateFragment
import com.github.panpf.assemblyadapter.sample.ui.pager.PagerFragmentStatePlaceholderFragment
import com.github.panpf.assemblyadapter.sample.ui.pager.PagerViewFragment
import com.github.panpf.assemblyadapter.sample.ui.pager.PagerViewPlaceholderFragment
import com.github.panpf.assemblyadapter.sample.ui.pager2.Pager2FragmentFragment
import com.github.panpf.assemblyadapter.sample.ui.pager2.Pager2FragmentPagingFragment
import com.github.panpf.assemblyadapter.sample.ui.pager2.Pager2FragmentPlaceholderFragment
import com.github.panpf.assemblyadapter.sample.ui.recycler.*

class MainFragment : BaseBindingFragment<FragmentMainBinding>() {

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ) = FragmentMainBinding.inflate(inflater, parent, false)

    override fun onInitData(binding: FragmentMainBinding, savedInstanceState: Bundle?) {
        binding.mainRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = AssemblyRecyclerAdapter(
                listOf(
                    LinkItemFactory(requireActivity()),
                    ListSeparatorItemFactory(requireActivity())
                ),
                listOf(
                    ListSeparator("RecyclerView"),
                    Link("RecyclerView - Normal - Linear", RecyclerNormalLinearFragment()),
                    Link("RecyclerView - Normal - Grid", RecyclerNormalGridFragment()),
                    Link(
                        "RecyclerView - Normal - Grid - Staggered",
                        RecyclerNormalGridStaggeredFragment()
                    ),
                    Link(
                        "RecyclerView - Normal - Placeholder",
                        RecyclerNormalPlaceholderFragment()
                    ),
                    Link("RecyclerView - Normal - Sticky", RecyclerNormalStickyFragment()),
                    Link("RecyclerView - Paging - Linear", RecyclerPagingLinearFragment()),
                    Link("RecyclerView - Paging - Grid", RecyclerPagingGridFragment()),
                    Link(
                        "RecyclerView - Paging - Grid - Staggered",
                        RecyclerPagingGridStaggeredFragment()
                    ),
                    Link("RecyclerView - Paging - Sticky", RecyclerPagingStickyFragment()),
                    Link("RecyclerView - List - Linear", RecyclerListLinearFragment()),
                    Link("RecyclerView - List - Placeholder", RecyclerListPlaceholderFragment()),

                    ListSeparator("ListView"),
                    Link("ListView", ListFragment()),
                    Link("ListView - Placeholder", ListPlaceholderFragment()),
                    Link("ExpandableListView", ExpandableListFragment()),
                    Link("ExpandableListView - Placeholder", ExpandableListPlaceholderFragment()),

                    ListSeparator("ViewPager"),
                    Link("ViewPager - View", PagerViewFragment()),
                    Link("ViewPager - View - Placeholder", PagerViewPlaceholderFragment()),
                    Link("ViewPager - Fragment - State", PagerFragmentStateFragment()),
                    Link(
                        "ViewPager - Fragment - State - Placeholder",
                        PagerFragmentStatePlaceholderFragment()
                    ),
                    Link("ViewPager2 - Fragment", Pager2FragmentFragment()),
                    Link(
                        "ViewPager2 - Fragment - Placeholder",
                        Pager2FragmentPlaceholderFragment()
                    ),
                    Link("ViewPager2 - Fragment - Paging", Pager2FragmentPagingFragment()),
                )
            )
        }
    }
}