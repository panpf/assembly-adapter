package com.github.panpf.assemblyadapter.sample.old.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.panpf.assemblyadapter.sample.old.bean.Link
import com.github.panpf.assemblyadapter.sample.old.databinding.ItemLinkBinding
import com.github.panpf.assemblyadapter.sample.old.base.FragmentContainerActivity
import me.panpf.adapter.AssemblyItem
import me.panpf.adapter.AssemblyItemFactory

class LinkItem(val binding: ItemLinkBinding) : AssemblyItem<Link>(binding.root) {

    init {
        itemView.setOnClickListener {
            val data = data ?: return@setOnClickListener
            val title = data.title.split(" - ")[0]
            val subTitle = data.title.split(" - ").getOrNull(1)
            context.startActivity(
                FragmentContainerActivity.createIntent(
                    context,
                    title = title,
                    subTitle = subTitle,
                    data.fragment
                )
            )
        }
    }

    override fun onSetData(position: Int, data: Link?) {
        binding.linkItemTitleText.text = data?.title
    }

    class Factory : AssemblyItemFactory<Link>() {

        override fun match(data: Any?): Boolean {
            return data is Link
        }

        override fun createAssemblyItem(parent: ViewGroup): AssemblyItem<Link> {
            return LinkItem(
                ItemLinkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }
}