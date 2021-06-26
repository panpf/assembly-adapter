package com.github.panpf.assemblyadapter.sample

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.sample.bean.Link
import com.github.panpf.assemblyadapter.sample.databinding.AcivityMainBinding
import com.github.panpf.assemblyadapter.sample.item.LinkItemFactory
import com.github.panpf.assemblyadapter.sample.ui.list.ExpandableListFragment
import com.github.panpf.assemblyadapter.sample.ui.list.ExpandableListPlaceholderFragment
import com.github.panpf.assemblyadapter.sample.ui.list.ListFragment
import com.github.panpf.assemblyadapter.sample.ui.list.ListPlaceholderFragment
import com.github.panpf.assemblyadapter.sample.ui.pager.PagerFragmentFragment
import com.github.panpf.assemblyadapter.sample.ui.pager.PagerFragmentStateFragment
import com.github.panpf.assemblyadapter.sample.ui.pager.PagerViewFragment
import com.github.panpf.assemblyadapter.sample.ui.pager2.Pager2FragmentFragment
import com.github.panpf.assemblyadapter.sample.ui.pager2.Pager2FragmentPagingFragment
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
                listOf(LinkItemFactory(this@MainActivity)),
                listOf(
                    Link("RecyclerView - Linear", RecyclerLinearFragment()),
                    Link("RecyclerView - Linear - Sticky", RecyclerLinearStickyFragment()),
                    Link("RecyclerView - Grid", RecyclerGridFragment()),
                    Link("RecyclerView - Grid - Staggered", RecyclerGridStaggeredFragment()),
                    Link("RecyclerView - Placeholder", RecyclerPlaceholderFragment()),
                    Link("RecyclerView - Paging - Linear", RecyclerPagingLinearFragment()),
                    Link(
                        "RecyclerView - Paging - Linear - Sticky",
                        RecyclerPagingLinearStickyFragment()
                    ),
                    Link("RecyclerView - Paging - Grid", RecyclerPagingGridFragment()),
                    Link(
                        "RecyclerView - Paging - Grid - Staggered",
                        RecyclerPagingGridStaggeredFragment()
                    ),
                    Link("ListView", ListFragment()),
                    Link("ListView - Placeholder", ListPlaceholderFragment()),
                    Link("ExpandableListView", ExpandableListFragment()),
                    Link("ExpandableListView - Placeholder", ExpandableListPlaceholderFragment()),
                    Link("ViewPager - View", PagerViewFragment()),
                    Link("ViewPager - Fragment", PagerFragmentFragment()),
                    Link("ViewPager - Fragment - State", PagerFragmentStateFragment()),
                    Link("ViewPager2 - Fragment", Pager2FragmentFragment()),
                    Link("ViewPager2 - Fragment - Paging", Pager2FragmentPagingFragment()),
                )
            )
        }
    }
}