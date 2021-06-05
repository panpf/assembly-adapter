package com.github.panpf.assemblyadapter.sample.old

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.panpf.assemblyadapter.sample.old.bean.Link
import com.github.panpf.assemblyadapter.sample.old.databinding.AcivityMainBinding
import com.github.panpf.assemblyadapter.sample.old.ui.*
import com.github.panpf.assemblyadapter.sample.old.ui.list.LinkItem
import me.panpf.adapter.AssemblyRecyclerAdapter

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
                    Link(
                        "AssemblyRecyclerAdapter - LinearLayoutManager",
                        RecyclerLinearLayoutSampleFragment()
                    ),
                    Link(
                        "AssemblyRecyclerAdapter - GridLayoutManager",
                        RecyclerGridLayoutSampleFragment()
                    ),
                    Link("AssemblyPagedListAdapter", RecyclerPagedSampleFragment()),
                    Link("AssemblyPagingDataAdapter", PagingDataAdapterSampleFragment()),
                    Link("AssemblyBaseAdapter - ListView", ListViewFragment()),
                    Link("AssemblyBaseAdapter - Spinner", SpinnerFragment()),
                    Link("AssemblyBaseExpandableListAdapter", ExpandableListViewFragment()),
                    Link("AssemblyPagerAdapter", ViewPagerPagerAdapterSampleFragment()),
                    Link(
                        "AssemblyFragmentPagerAdapter",
                        ViewPagerFragmentStatePagerAdapterSampleFragment()
                    ),
                    Link(
                        "AssemblyFragmentStatePagerAdapter",
                        ViewPagerFragmentStatePagerAdapterSampleFragment()
                    ),
                )
            ).apply {
                addItemFactory(LinkItem.Factory())
            }
        }
    }
}