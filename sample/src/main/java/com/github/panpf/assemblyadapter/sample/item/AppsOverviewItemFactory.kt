package com.github.panpf.assemblyadapter.sample.item

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.updatePadding
import com.github.panpf.assemblyadapter.Item
import com.github.panpf.assemblyadapter.BindingItemFactory
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.bean.AppsOverview
import com.github.panpf.assemblyadapter.sample.databinding.ItemAppsOverviewBinding

class AppsOverviewItemFactory(
    private val activity: Activity,
    private val hideStartMargin: Boolean = false
) :
    BindingItemFactory<AppsOverview, ItemAppsOverviewBinding>() {

    override fun match(data: Any): Boolean {
        return data is AppsOverview
    }

    override fun createItemViewBinding(
        inflater: LayoutInflater, parent: ViewGroup
    ): ItemAppsOverviewBinding {
        return ItemAppsOverviewBinding.inflate(inflater, parent, false)
    }

    override fun initItem(
        context: Context,
        binding: ItemAppsOverviewBinding,
        item: Item<AppsOverview>
    ) {
        super.initItem(context, binding, item)
        if (hideStartMargin) {
            binding.root.updatePadding(left = 0, right = 0)
        }

        binding.root.setOnLongClickListener {
            AlertDialog.Builder(activity).apply {
                setMessage(buildString {
                    append("AppsOverview").appendLine()
                    appendLine()
                    append("bindingAdapterPosition: ${item.bindingAdapterPosition}").appendLine()
                    append("absoluteAdapterPosition: ${item.absoluteAdapterPosition}")
                })
            }.show()
            true
        }
    }

    override fun bindItemData(
        context: Context,
        binding: ItemAppsOverviewBinding,
        item: Item<AppsOverview>,
        bindingAdapterPosition: Int,
        data: AppsOverview
    ) {
        binding.appsOverviewItemContentText.text = context.getString(
            R.string.apps_overview_item, data.count, data.userAppCount, data.groupCount
        )
    }
}
