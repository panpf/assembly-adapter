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
                    Link(
                        "RecyclerView - Linear",
                        RecyclerViewLinearFragment()
                    ),
                    Link(
                        "RecyclerView - Linear - Sticky",
                        RecyclerViewLinearStickyFragment()
                    ),
                    Link(
                        "RecyclerView - Grid",
                        RecyclerViewGridFragment()
                    ),
                    Link(
                        "RecyclerView - Grid - Sticky",
                        RecyclerViewGridStickyFragment()
                    ),
//                    Link(
//                        "ListView",
//                        ListViewFragment()
//                    ),
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