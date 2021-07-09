package com.github.panpf.assemblyadapter.sample.item

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.github.panpf.assemblyadapter.Placeholder
import com.github.panpf.assemblyadapter.ViewItemFactory
import com.github.panpf.assemblyadapter.sample.R

class AppGroupPlaceholderItemFactory(private val activity: Activity) :
    ViewItemFactory<Placeholder>(Placeholder::class, R.layout.item_app_group_placeholder) {
    override fun initItem(context: Context, itemView: View, item: Item<Placeholder>) {
        super.initItem(context, itemView, item)

        itemView.setOnLongClickListener {
            AlertDialog.Builder(activity).apply {
                setMessage(buildString {
                    append("Placeholder").appendLine()
                    appendLine()
                    append("bindingAdapterPosition: ${item.bindingAdapterPosition}").appendLine()
                    append("absoluteAdapterPosition: ${item.absoluteAdapterPosition}").appendLine()
                    append("data: Placeholder").appendLine()
                })
            }.show()
            true
        }
    }
}