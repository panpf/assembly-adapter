package com.github.panpf.assemblyadapter.list.test.expandable

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import androidx.viewbinding.ViewBinding
import com.github.panpf.assemblyadapter.list.expandable.BindingExpandableChildItemFactory
import com.github.panpf.assemblyadapter.list.expandable.ExpandableGroup
import com.github.panpf.assemblyadapter.list.test.R
import org.junit.Assert
import org.junit.Test

class BindingExpandableChildItemFactoryTest {

    @Test
    fun test() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)

        val itemFactory = TextBindingExpandableChildItemFactory()
        val item =
            itemFactory.dispatchCreateItem(parent)
                    as BindingExpandableChildItemFactory.BindingExpandableChildItem<TextGroup, Text, ItemBindingTestBinding>

        Assert.assertEquals("", item.binding.testItemTitleText.text)
        Assert.assertEquals(30f, item.binding.testItemTitleText.textSize)

        item.dispatchChildBindData(0, 0, TextGroup("hello", "world"), true, 1, 1, Text("world"))
        Assert.assertEquals("world", item.binding.testItemTitleText.text)
    }

    private data class Text(val text: String)

    private data class TextGroup(val list: List<Text>) : ExpandableGroup {

        @Suppress("unused")
        val listJoinToString: String
            get() = list.joinToString(prefix = "[", postfix = "]") { it.text }

        constructor(vararg texts: String) : this(texts.map { Text(it) }.toList())

        override fun getChildCount(): Int = list.size

        override fun getChild(childPosition: Int): Any {
            // Shield the differences in exceptions thrown by different versions of the ArrayList get method
            return list.getOrNull(childPosition)
                ?: throw IndexOutOfBoundsException("Index: $childPosition, Size: ${list.size}")
        }
    }

    private class TextBindingExpandableChildItemFactory :
        BindingExpandableChildItemFactory<TextGroup, Text, ItemBindingTestBinding>(
            Text::class
        ) {

        override fun createItemViewBinding(
            context: Context,
            inflater: LayoutInflater,
            parent: ViewGroup
        ) = ItemBindingTestBinding.inflate(inflater, parent, false)

        override fun initItem(
            context: Context,
            binding: ItemBindingTestBinding,
            item: BindingExpandableChildItem<TextGroup, Text, ItemBindingTestBinding>
        ) {
            binding.testItemTitleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, 30f)
        }

        override fun bindItemData(
            context: Context,
            binding: ItemBindingTestBinding,
            item: BindingExpandableChildItem<TextGroup, Text, ItemBindingTestBinding>,
            groupBindingAdapterPosition: Int,
            groupAbsoluteAdapterPosition: Int,
            groupData: TextGroup,
            isLastChild: Boolean,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: Text
        ) {
            binding.testItemTitleText.text = data.text
        }
    }

    private class ItemBindingTestBinding(
        private val root: LinearLayout,
        val testItemTitleText: TextView
    ) : ViewBinding {

        override fun getRoot(): View = root

        companion object {
            fun inflate(
                inflater: LayoutInflater,
                parent: ViewGroup?,
                attach: Boolean
            ): ItemBindingTestBinding {
                val itemView = inflater.inflate(R.layout.item_test, parent, attach)
                return ItemBindingTestBinding(
                    itemView as LinearLayout,
                    itemView.findViewById(R.id.testItemTitleText)
                )
            }
        }
    }
}