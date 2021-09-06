package com.github.panpf.assemblyadapter.common.item.test

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
import com.github.panpf.assemblyadapter.BindingItemFactory
import org.junit.Assert
import org.junit.Test

class BindingItemFactoryTest {

    @Test
    fun test() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)

        val itemFactory = TestBindingItemFactory()
        val item =
            itemFactory.dispatchCreateItem(parent) as BindingItemFactory.BindingItem<String, ItemBindingTestBinding>

        Assert.assertEquals("", item.binding.bindingTestItemTitleText.text)
        Assert.assertEquals(30f, item.binding.bindingTestItemTitleText.textSize)

        item.dispatchBindData(0, 0, "hello")
        Assert.assertEquals("hello", item.binding.bindingTestItemTitleText.text)
    }

    private class TestBindingItemFactory :
        BindingItemFactory<String, ItemBindingTestBinding>(String::class) {

        override fun createItemViewBinding(
            context: Context,
            inflater: LayoutInflater,
            parent: ViewGroup
        ) = ItemBindingTestBinding.inflate(inflater, parent, false)

        override fun initItem(
            context: Context,
            binding: ItemBindingTestBinding,
            item: BindingItem<String, ItemBindingTestBinding>
        ) {
            binding.bindingTestItemTitleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, 30f)
        }

        override fun bindItemData(
            context: Context,
            binding: ItemBindingTestBinding,
            item: BindingItem<String, ItemBindingTestBinding>,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: String
        ) {
            binding.bindingTestItemTitleText.text = data
        }
    }

    private class ItemBindingTestBinding(
        private val root: LinearLayout,
        val bindingTestItemTitleText: TextView
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