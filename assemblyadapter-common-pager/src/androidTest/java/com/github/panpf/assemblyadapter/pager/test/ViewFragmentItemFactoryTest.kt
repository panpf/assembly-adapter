package com.github.panpf.assemblyadapter.pager.test

import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.common.pager.test.R
import com.github.panpf.assemblyadapter.pager.ViewFragmentItemFactory
import org.junit.Assert
import org.junit.Test

class ViewFragmentItemFactoryTest {

    @Test
    fun test() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)

        val itemFactory = ViewFragmentItemFactory(
            String::class,
            R.layout.fragment_test
        )
        val fragment = itemFactory.dispatchCreateFragment(
            0,
            0,
            "test_data"
        ) as ViewFragmentItemFactory.ViewFragment

        val itemView = fragment.onCreateView(LayoutInflater.from(context), parent, null)

        Assert.assertEquals(
            "",
            itemView.findViewById<TextView>(R.id.testTitleText).text
        )
        Assert.assertEquals(
            14f,
            itemView.findViewById<TextView>(R.id.testTitleText).textSize
        )
    }
}