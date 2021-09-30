package com.github.panpf.assemblyadapter.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.sample.NavMainDirections
import com.github.panpf.assemblyadapter.sample.base.ToolbarFragment
import com.github.panpf.assemblyadapter.sample.bean.Link
import com.github.panpf.assemblyadapter.sample.bean.ListSeparator
import com.github.panpf.assemblyadapter.sample.databinding.FragmentMainBinding
import com.github.panpf.assemblyadapter.sample.item.LinkItemFactory
import com.github.panpf.assemblyadapter.sample.item.ListSeparatorItemFactory

class MainFragment : ToolbarFragment<FragmentMainBinding>() {

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ) = FragmentMainBinding.inflate(inflater, parent, false)

    override fun onInitData(
        toolbar: Toolbar,
        binding: FragmentMainBinding,
        savedInstanceState: Bundle?
    ) {
        binding.mainRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = AssemblyRecyclerAdapter(
                listOf(
                    LinkItemFactory(requireActivity()),
                    ListSeparatorItemFactory(requireActivity())
                ),
                listOf(
                    ListSeparator("RecyclerView - Linear"),
                    Link(
                        "RecyclerView - Linear",
                        NavMainDirections.actionGlobalRecyclerLinearFragment(
                            "RecyclerView",
                            "Linear"
                        )
                    ),
                    Link(
                        "RecyclerView - Linear - Paging",
                        NavMainDirections.actionGlobalRecyclerLinearPagingFragment(
                            "RecyclerView",
                            "Linear - Paging"
                        )
                    ),
                    Link(
                        "RecyclerView - Linear - Placeholder",
                        NavMainDirections.actionGlobalRecyclerLinearPlaceholderFragment(
                            "RecyclerView",
                            "Linear - Placeholder"
                        )
                    ),
                    Link(
                        "RecyclerView - Linear - Divider - Vertical",
                        NavMainDirections.actionGlobalRecyclerLinearDividerVerFragment(
                            "RecyclerView",
                            "Linear - Divider - Vertical"
                        )
                    ),
                    Link(
                        "RecyclerView - Linear - Divider - Horizontal",
                        NavMainDirections.actionGlobalRecyclerLinearDividerHorFragment(
                            "RecyclerView",
                            "Linear - Divider - Horizontal"
                        )
                    ),

                    ListSeparator("RecyclerView - Grid"),
                    Link(
                        "RecyclerView - Grid",
                        NavMainDirections.actionGlobalRecyclerGridFragment("RecyclerView", "Grid")
                    ),
                    Link(
                        "RecyclerView - Grid - Paging",
                        NavMainDirections.actionGlobalRecyclerGridPagingFragment(
                            "RecyclerView",
                            "Grid - Paging"
                        )
                    ),
                    Link(
                        "RecyclerView - Grid - Divider - Vertical",
                        NavMainDirections.actionGlobalRecyclerGridDividerVerFragment(
                            "RecyclerView",
                            "Grid - Divider - Vertical"
                        )
                    ),
                    Link(
                        "RecyclerView - Grid - Divider - Horizontal",
                        NavMainDirections.actionGlobalRecyclerGridDividerHorFragment(
                            "RecyclerView",
                            "Grid - Divider - Horizontal"
                        )
                    ),

                    ListSeparator("RecyclerView - StaggeredGrid"),
                    Link(
                        "RecyclerView - StaggeredGrid",
                        NavMainDirections.actionGlobalRecyclerStaggeredGridFragment(
                            "RecyclerView",
                            "StaggeredGrid"
                        )
                    ),
                    Link(
                        "RecyclerView - StaggeredGrid - Paging",
                        NavMainDirections.actionGlobalRecyclerStaggeredGridPagingFragment(
                            "RecyclerView",
                            "StaggeredGrid - Paging"
                        )
                    ),
                    Link(
                        "RecyclerView - StaggeredGrid - Divider - Vertical",
                        NavMainDirections.actionGlobalRecyclerStaggeredGridDividerVerFragment(
                            "RecyclerView",
                            "StaggeredGrid - Divider - Vertical"
                        )
                    ),
                    Link(
                        "RecyclerView - StaggeredGrid - Divider - Horizontal",
                        NavMainDirections.actionGlobalRecyclerStaggeredGridDividerHorFragment(
                            "RecyclerView",
                            "StaggeredGrid - Divider - Horizontal"
                        )
                    ),

                    ListSeparator("RecyclerView - ListAdapter"),
                    Link(
                        "RecyclerView - ListAdapter - Linear",
                        NavMainDirections.actionGlobalRecyclerListAdapterLinearFragment(
                            "RecyclerView",
                            "ListAdapter - Linear"
                        )
                    ),
                    Link(
                        "RecyclerView - ListAdapter - Placeholder",
                        NavMainDirections.actionGlobalRecyclerListAdapterPlaceholderFragment(
                            "RecyclerView",
                            "ListAdapter - Placeholder"
                        )
                    ),

                    ListSeparator("ListView"),
                    Link(
                        "ListView",
                        NavMainDirections.actionGlobalListFragment("ListView")
                    ),
                    Link(
                        "ListView - Placeholder",
                        NavMainDirections.actionGlobalListPlaceholderFragment(
                            "ListView",
                            "Placeholder"
                        )
                    ),

                    ListSeparator("ExpandableListView"),
                    Link(
                        "ExpandableListView",
                        NavMainDirections.actionGlobalExpandableListFragment("ExpandableListView")
                    ),
                    Link(
                        "ExpandableListView - Placeholder",
                        NavMainDirections.actionGlobalExpandableListPlaceholderFragment(
                            "ExpandableListView",
                            "Placeholder"
                        )
                    ),

                    ListSeparator("ViewPager"),
                    Link(
                        "ViewPager - View",
                        NavMainDirections.actionGlobalPagerViewFragment("ViewPager", "View")
                    ),
                    Link(
                        "ViewPager - View - Array",
                        NavMainDirections.actionGlobalPagerViewArrayFragment(
                            "ViewPager",
                            "View - Array"
                        )
                    ),
                    Link(
                        "ViewPager - View - Placeholder",
                        NavMainDirections.actionGlobalPagerViewPlaceholderFragment(
                            "ViewPager",
                            "View - Placeholder"
                        )
                    ),
                    Link(
                        "ViewPager - Fragment",
                        NavMainDirections.actionGlobalPagerFragmentFragment("ViewPager", "Fragment")
                    ),
                    Link(
                        "ViewPager - Fragment - Array",
                        NavMainDirections.actionGlobalPagerFragmentArrayFragment(
                            "ViewPager",
                            "Fragment - Array"
                        )
                    ),
                    Link(
                        "ViewPager - Fragment - Placeholder",
                        NavMainDirections.actionGlobalPagerFragmentPlaceholderFragment(
                            "ViewPager",
                            "Fragment - Placeholder"
                        )
                    ),

                    ListSeparator("ViewPager2"),
                    Link(
                        "ViewPager2",
                        NavMainDirections.actionGlobalPager2Fragment("ViewPager2")
                    ),
                    Link(
                        "ViewPager2 - Array",
                        NavMainDirections.actionGlobalPager2ArrayFragment("ViewPager2", "Array")
                    ),
                    Link(
                        "ViewPager2 - List",
                        NavMainDirections.actionGlobalPager2ListFragment("ViewPager2", "List")
                    ),
                    Link(
                        "ViewPager2 - Paging",
                        NavMainDirections.actionGlobalPager2PagingFragment("ViewPager2", "Paging")
                    ),
                    Link(
                        "ViewPager2 - Placeholder",
                        NavMainDirections.actionGlobalPager2PlaceholderFragment(
                            "ViewPager2",
                            "Placeholder"
                        )
                    ),
                )
            )
        }
    }
}