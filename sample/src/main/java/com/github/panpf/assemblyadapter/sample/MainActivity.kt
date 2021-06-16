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
                    Link("RecyclerView - Linear - Sticky", RecyclerLinearStickyFragment()),
                    Link("RecyclerView - Grid", RecyclerGridFragment()),
                    Link("RecyclerView - StaggeredGrid", RecyclerStaggeredGridFragment()),
                    Link("RecyclerView - Paging - Linear", RecyclerPagingLinearFragment()),
                    Link(
                        "RecyclerView - Paging - Linear - Sticky",
                        RecyclerPagingLinearStickyFragment()
                    ),
                    Link("RecyclerView - Paging - Grid", RecyclerPagingGridFragment()),
                    Link(
                        "RecyclerView - Paging - StaggeredGrid",
                        RecyclerPagingStaggeredGridFragment()
                    ),
                    Link("ListView", ListFragment()),
                    Link("ExpandableListView", ListExpandableFragment()),
                    Link("ViewPager - View", PagerFragment()),
                    Link("ViewPager - Fragment", PagerFragmentFragment()),
                    Link("ViewPager - FragmentState", PagerFragmentStateFragment()),
                    Link("ViewPager2 - Fragment", Pager2FragmentFragment()),
                    Link("ViewPager2 - Fragment - Paging", Pager2FragmentPagingFragment()),
                )
            )
        }
    }
}