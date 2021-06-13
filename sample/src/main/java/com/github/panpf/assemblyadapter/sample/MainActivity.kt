package com.github.panpf.assemblyadapter.sample

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.sample.bean.Link
import com.github.panpf.assemblyadapter.sample.databinding.AcivityMainBinding
import com.github.panpf.assemblyadapter.sample.ui.*
import com.github.panpf.assemblyadapter.sample.ui.list.LinkItemFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = AcivityMainBinding.inflate(LayoutInflater.from(this), null, false)
        setContentView(binding.root)

        setSupportActionBar(binding.mainToolbar)

        binding.mainRecycler.apply {
            layoutManager = LinearLayoutManager(baseContext)
            adapter = AssemblyRecyclerAdapter(
                listOf(LinkItemFactory()),
                listOf(
                    Link("RecyclerView - Linear", RecyclerLinearFragment()),
                    Link("RecyclerView - Grid", RecyclerGridFragment()),
                    Link("RecyclerView - StaggeredGrid", RecyclerStaggeredGridFragment()),
                    Link("RecyclerView - Paging - Linear", RecyclerPagingLinearFragment()),
                    Link("RecyclerView - Paging - Grid", RecyclerPagingGridFragment()),
                    Link("RecyclerView - Paging - StaggeredGrid", RecyclerPagingStaggeredGridFragment()),
                    Link("Sticky - RecyclerView - Linear", StickyRecyclerLinearFragment()),
//                    Link("RecyclerView - Grid - Sticky", RecyclerGridStickyFragment()),
//                    Link("RecyclerView - StaggeredGrid - Sticky", RecyclerStaggeredGridStickyFragment()),
                    Link("Sticky - RecyclerView - Paging - Linear", StickyRecyclerPagingLinearFragment()),
//                    Link("RecyclerView - Paging - Grid - Sticky", RecyclerPagingGridStickyFragment()),
//                    Link("RecyclerView - Paging - StaggeredGrid - Sticky", RecyclerPagingStaggeredGridStickyFragment()),
                    Link("ListView", ListFragment()),
                    Link("ExpandableListView", ListExpandableFragment()),
//                    Link("AssemblyPagingDataAdapter", PagingDataAdapterSampleFragment()),
//                    Link("AssemblyBaseAdapter - ListView", ListViewFragment()),
//                    Link("AssemblyBaseAdapter - Spinner", SpinnerFragment()),
//                    Link("AssemblyBaseExpandableListAdapter", ExpandableListViewFragment()),
//                    Link("AssemblyPagerAdapter", ViewPagerPagerAdapterSampleFragment()),
//                    Link(
//                        "AssemblyFragmentPagerAdapter",
//                        ViewPagerFragmentStatePagerAdapterSampleFragment()
//                    ),
//                    Link(
//                        "AssemblyFragmentStatePagerAdapter",
//                        ViewPagerFragmentStatePagerAdapterSampleFragment()
//                    ),
                )
            )
        }
    }
}