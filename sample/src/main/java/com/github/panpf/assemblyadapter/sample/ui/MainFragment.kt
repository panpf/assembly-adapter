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
import com.github.panpf.assemblyadapter.sample.ui.pager.*
import com.github.panpf.assemblyadapter.sample.ui.pager2.Pager2Fragment
import com.github.panpf.assemblyadapter.sample.ui.pager2.Pager2ListFragment
import com.github.panpf.assemblyadapter.sample.ui.pager2.Pager2PagingFragment
import com.github.panpf.assemblyadapter.sample.ui.pager2.Pager2PlaceholderFragment
import com.github.panpf.assemblyadapter.sample.ui.recycler.*

class MainFragment : BaseBindingFragment<FragmentMainBinding>() {

    private val links = listOf(
        ListSeparator("RecyclerView - Linear"),
        Link("RecyclerView - Linear", RecyclerLinearFragment()),
        Link("RecyclerView - Linear - Paging", RecyclerLinearPagingFragment()),
        Link("RecyclerView - Linear - Placeholder", RecyclerLinearPlaceholderFragment()),
        Link("RecyclerView - Linear - Divider - Vertical", RecyclerLinearDividerVerFragment()),
        Link("RecyclerView - Linear - Divider - Horizontal", RecyclerLinearDividerHorFragment()),

        ListSeparator("RecyclerView - Grid"),
        Link("RecyclerView - Grid", RecyclerGridFragment()),
        Link("RecyclerView - Grid - Paging", RecyclerGridPagingFragment()),
        Link("RecyclerView - Grid - Divider - Vertical", RecyclerGridDividerVerFragment()),
        Link("RecyclerView - Grid - Divider - Horizontal", RecyclerGridDividerHorFragment()),

        ListSeparator("RecyclerView - StaggeredGrid"),
        Link("RecyclerView - StaggeredGrid", RecyclerStaggeredGridFragment()),
        Link("RecyclerView - StaggeredGrid - Paging", RecyclerStaggeredGridPagingFragment()),
        Link(
            "RecyclerView - StaggeredGrid - Divider - Vertical",
            RecyclerStaggeredGridDividerVerFragment()
        ),
        Link(
            "RecyclerView - StaggeredGrid - Divider - Horizontal",
            RecyclerStaggeredGridDividerHorFragment()
        ),

        ListSeparator("RecyclerView - ListAdapter"),
        Link("RecyclerView - ListAdapter - Linear", RecyclerListAdapterLinearFragment()),
        Link("RecyclerView - ListAdapter - Placeholder", RecyclerListAdapterPlaceholderFragment()),

        ListSeparator("ListView"),
        Link("ListView", ListFragment()),
        Link("ListView - Placeholder", ListPlaceholderFragment()),

        ListSeparator("ExpandableListView"),
        Link("ExpandableListView", ExpandableListFragment()),
        Link("ExpandableListView - Placeholder", ExpandableListPlaceholderFragment()),

        ListSeparator("ViewPager"),
        Link("ViewPager - View", PagerViewFragment()),
        Link("ViewPager - View - Placeholder", PagerViewPlaceholderFragment()),
        Link("ViewPager - Fragment", PagerFragmentFragment()),
        Link("ViewPager - Fragment - Placeholder", PagerFragmentPlaceholderFragment()),

        ListSeparator("ViewPager2"),
        Link("ViewPager2", Pager2Fragment()),
        Link("ViewPager2 - List", Pager2ListFragment()),
        Link("ViewPager2 - Paging", Pager2PagingFragment()),
        Link("ViewPager2 - Placeholder", Pager2PlaceholderFragment()),
    )

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
                links
            )
        }
    }
}