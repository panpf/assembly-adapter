package com.github.panpf.assemblyadapter.sample.item

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.updateLayoutParams
import com.github.panpf.assemblyadapter.BindingAssemblyItemFactory
import com.github.panpf.assemblyadapter.sample.bean.PinyinGroup
import com.github.panpf.assemblyadapter.sample.databinding.ItemPinyinGroupBinding

open class PinyinGroupItemFactory(
    private val activity: Activity,
    private val hideStartMargin: Boolean = false
) : BindingAssemblyItemFactory<PinyinGroup, ItemPinyinGroupBinding>() {

    override fun match(data: Any?): Boolean {
        return data is PinyinGroup
    }

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup
    ): ItemPinyinGroupBinding {
        return ItemPinyinGroupBinding.inflate(inflater, parent, false)
    }

    override fun initItem(
        context: Context,
        binding: ItemPinyinGroupBinding,
        item: BindingAssemblyItem<PinyinGroup, ItemPinyinGroupBinding>
    ) {
        super.initItem(context, binding, item)
        if (hideStartMargin) {
            binding.pinyinGroupItemTitleText.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = 0
                rightMargin = 0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    marginStart = 0
                    marginEnd = 0
                }
            }
        }

        binding.root.setOnClickListener {
            val data = item.data ?: return@setOnClickListener
            val bindingAdapterPosition = item.bindingAdapterPosition
            val absoluteAdapterPosition = item.absoluteAdapterPosition
            Toast.makeText(
                context,
                "${data}: $bindingAdapterPosition/$absoluteAdapterPosition",
                Toast.LENGTH_LONG
            ).show()
        }

        binding.root.setOnLongClickListener {
            val data = item.data ?: return@setOnLongClickListener false
            AlertDialog.Builder(activity).apply {
                setMessage(buildString {
                    append("Group（${data.title}）").appendLine()
                    appendLine()
                    append("bindingAdapterPosition: ${item.bindingAdapterPosition}").appendLine()
                    append("absoluteAdapterPosition: ${item.absoluteAdapterPosition}")
                })
            }.show()
            true
        }
    }

    override fun bindData(
        context: Context, binding: ItemPinyinGroupBinding,
        item: BindingAssemblyItem<PinyinGroup, ItemPinyinGroupBinding>,
        bindingAdapterPosition: Int, data: PinyinGroup?
    ) {
        binding.pinyinGroupItemTitleText.text = data?.title
    }
}
