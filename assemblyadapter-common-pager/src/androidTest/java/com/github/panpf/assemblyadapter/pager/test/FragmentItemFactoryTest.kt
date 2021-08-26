package com.github.panpf.assemblyadapter.pager.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.panpf.assemblyadapter.common.pager.test.R
import com.github.panpf.assemblyadapter.pager.FragmentItemFactory
import org.junit.Assert
import org.junit.Test
import kotlin.reflect.KClass

class FragmentItemFactoryTest {

    @Test
    fun testMethodMatchData() {
        val testItemFactory = TestFragmentItemFactory(String::class)

        Assert.assertFalse(testItemFactory.matchData(1))
        Assert.assertFalse(testItemFactory.matchData(false))
        Assert.assertTrue(testItemFactory.matchData("string"))
    }

    @Test
    fun testMethodDispatchCreateFragment() {
        val testItemFactory = TestFragmentItemFactory(String::class)

        val fragment = testItemFactory.dispatchCreateFragment(0, 0, "test_data")
        Assert.assertTrue(fragment is TestFragment)
    }


    private class TestFragmentItemFactory(dataClass: KClass<String>) :
        FragmentItemFactory<String>(dataClass) {

        override fun createFragment(
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: String
        ): Fragment = TestFragment.create(data)
    }

    private class TestFragment : Fragment() {

        companion object {
            fun create(data: String) = TestFragment().apply {
                arguments = Bundle().apply {
                    putString("data", data)
                }
            }
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? = inflater.inflate(R.layout.fragment_test, container, false).apply {
            findViewById<TextView>(R.id.testTitleText).text =
                arguments!!.getString("data")
        }
    }
}