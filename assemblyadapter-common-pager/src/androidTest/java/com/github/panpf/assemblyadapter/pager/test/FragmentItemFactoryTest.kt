/*
 * Copyright (C) 2021 panpf <panpfpanpf@outlook.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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