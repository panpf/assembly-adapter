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
package com.github.panpf.assemblyadapter.list.expandable.test

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.list.expandable.SimpleExpandableGroupItemFactory
import com.github.panpf.assemblyadapter.list.expandable.ViewExpandableGroupItemFactory
import com.github.panpf.assemblyadapter.list.test.R
import org.junit.Assert
import org.junit.Test

class ViewExpandableGroupItemFactoryTest {

    @Test
    fun test() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)

        val itemFactory =
            ViewExpandableGroupItemFactory(TestExpandableGroup::class, R.layout.item_test)
        val item =
            itemFactory.dispatchCreateItem(parent) as SimpleExpandableGroupItemFactory.SimpleExpandableGroupItem<TestExpandableGroup>

        Assert.assertEquals(
            "",
            item.itemView.findViewById<TextView>(R.id.testItemTitleText).text
        )
        Assert.assertEquals(
            14f,
            item.itemView.findViewById<TextView>(R.id.testItemTitleText).textSize
        )

        item.dispatchGroupBindData(true, 0, 0, TestExpandableGroup("test_data"))
        Assert.assertEquals(
            "",
            item.itemView.findViewById<TextView>(R.id.testItemTitleText).text
        )


        val itemFactory2 = ViewExpandableGroupItemFactory(
            TestExpandableGroup::class,
            LayoutInflater.from(context).inflate(R.layout.item_test, parent, false)
        )
        val item2 =
            itemFactory2.dispatchCreateItem(parent) as SimpleExpandableGroupItemFactory.SimpleExpandableGroupItem<TestExpandableGroup>

        Assert.assertEquals(
            "",
            item2.itemView.findViewById<TextView>(R.id.testItemTitleText).text
        )
        Assert.assertEquals(
            14f,
            item2.itemView.findViewById<TextView>(R.id.testItemTitleText).textSize
        )

        item2.dispatchGroupBindData(true, 0, 0, TestExpandableGroup("test_data"))
        Assert.assertEquals(
            "",
            item2.itemView.findViewById<TextView>(R.id.testItemTitleText).text
        )


        val itemFactory3 =
            ViewExpandableGroupItemFactory(TestExpandableGroup::class) { _, inflater, parent: ViewGroup ->
                inflater.inflate(R.layout.item_test, parent, false)
            }
        val item3 =
            itemFactory3.dispatchCreateItem(parent) as SimpleExpandableGroupItemFactory.SimpleExpandableGroupItem<TestExpandableGroup>

        Assert.assertEquals(
            "",
            item3.itemView.findViewById<TextView>(R.id.testItemTitleText).text
        )
        Assert.assertEquals(
            14f,
            item3.itemView.findViewById<TextView>(R.id.testItemTitleText).textSize
        )

        item3.dispatchGroupBindData(true, 0, 0, TestExpandableGroup("test_data"))
        Assert.assertEquals(
            "",
            item3.itemView.findViewById<TextView>(R.id.testItemTitleText).text
        )
    }
}