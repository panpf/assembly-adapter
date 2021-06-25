package com.github.panpf.assemblyadapter.sample.item

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.github.panpf.assemblyadapter.BindingAssemblyItemFactory
import com.github.panpf.assemblyadapter.sample.base.FragmentContainerActivity
import com.github.panpf.assemblyadapter.sample.bean.Link
import com.github.panpf.assemblyadapter.sample.databinding.ItemLinkBinding

class LinkItemFactory(private val activity: Activity) :
    BindingAssemblyItemFactory<Link, ItemLinkBinding>() {

    override fun match(data: Any?): Boolean {
        return data is Link
    }

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup
    ): ItemLinkBinding {
        return ItemLinkBinding.inflate(inflater, parent, false)
    }

    override fun initItem(
        context: Context, binding: ItemLinkBinding,
        item: BindingAssemblyItem<Link, ItemLinkBinding>
    ) {
        super.initItem(context, binding, item)
        binding.root.setOnClickListener {
            val data = item.data ?: return@setOnClickListener
            val title = data.title.substringBefore(" - ", data.title)
            val subTitle = data.title.substringAfter(" - ", "")
            context.startActivity(
                FragmentContainerActivity.createIntent(context, title, subTitle, data.fragment)
            )
        }

        binding.root.setOnLongClickListener {
            val data = item.data ?: return@setOnLongClickListener false
            AlertDialog.Builder(activity).apply {
                setMessage(buildString {
                    append("Item（${data.title}）").appendLine()
                    appendLine()
                    append("bindingAdapterPosition: ${item.bindingAdapterPosition}").appendLine()
                    append("absoluteAdapterPosition: ${item.absoluteAdapterPosition}")
                })
            }.show()
            true
        }
    }

    override fun bindData(
        context: Context, binding: ItemLinkBinding,
        item: BindingAssemblyItem<Link, ItemLinkBinding>,
        bindingAdapterPosition: Int, data: Link?
    ) {
        binding.linkItemTitleText.text = data?.title
    }
}