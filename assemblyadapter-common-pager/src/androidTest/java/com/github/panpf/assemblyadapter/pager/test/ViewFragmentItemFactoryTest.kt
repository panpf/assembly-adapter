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