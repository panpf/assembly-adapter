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

        val itemFactory = TestBindingExpandableChildItemFactory()
        val item =
            itemFactory.dispatchCreateItem(parent)
                    as BindingExpandableChildItemFactory.BindingExpandableChildItem<Strings, String, ItemBindingTestBinding>

        Assert.assertEquals("", item.binding.testItemTitleText.text)
        Assert.assertEquals(30f, item.binding.testItemTitleText.textSize)

        item.dispatchChildBindData(0, 0, Strings("test_data"), true, 1, 1, "test_value")
        Assert.assertEquals("test_value", item.binding.testItemTitleText.text)
    }

    private data class Strings(val name: String = "") : ExpandableGroup {

        override fun getChildCount(): Int = name.length

        override fun getChild(childPosition: Int): Any {
            return name[childPosition].toString()
        }
    }

    private class TestBindingExpandableChildItemFactory :
        BindingExpandableChildItemFactory<Strings, String, ItemBindingTestBinding>(
            String::class
        ) {

        override fun createItemViewBinding(
            context: Context,
            inflater: LayoutInflater,
            parent: ViewGroup
        ) = ItemBindingTestBinding.inflate(inflater, parent, false)

        override fun initItem(
            context: Context,
            binding: ItemBindingTestBinding,
            item: BindingExpandableChildItem<Strings, String, ItemBindingTestBinding>
        ) {
            binding.testItemTitleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, 30f)
        }

        override fun bindItemData(
            context: Context,
            binding: ItemBindingTestBinding,
            item: BindingExpandableChildItem<Strings, String, ItemBindingTestBinding>,
            groupBindingAdapterPosition: Int,
            groupAbsoluteAdapterPosition: Int,
            groupData: Strings,
            isLastChild: Boolean,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: String
        ) {
            binding.testItemTitleText.text = data
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