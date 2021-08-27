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
package com.github.panpf.assemblyadapter.recycler.divider.test

import androidx.recyclerview.widget.RecyclerView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.recycler.divider.*
import com.github.panpf.assemblyadapter.recycler.divider.internal.GridItemDividerProvider
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDividerConfig
import com.github.panpf.assemblyadapter.recycler.divider.internal.LinearItemDividerProvider
import com.github.panpf.assemblyadapter.recycler.divider.internal.StaggeredGridItemDividerProvider
import com.github.panpf.tools4j.reflect.ktx.getFieldValue
import org.junit.Assert
import org.junit.Test

class DividerExtensionsTest {

    @Test
    fun testLinear() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val recyclerView = RecyclerView(context)

        recyclerView.newLinearDividerItemDecoration().apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }

        recyclerView.newLinearDividerItemDecoration {
            headerAndFooterSideDivider(Divider.space(20))
        }.apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNotNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNotNull(itemDividerProvider.footerSideDividerConfig)
        }

        recyclerView.addLinearDividerItemDecoration()
        (recyclerView.getItemDecorationAt(0) as LinearDividerItemDecoration).apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }
        recyclerView.removeItemDecorationAt(0)

        recyclerView.addLinearDividerItemDecoration {
            headerAndFooterSideDivider(Divider.space(20))
        }
        (recyclerView.getItemDecorationAt(0) as LinearDividerItemDecoration).apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNotNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNotNull(itemDividerProvider.footerSideDividerConfig)
        }
        recyclerView.removeItemDecorationAt(0)

        Assert.assertEquals(0, recyclerView.itemDecorationCount)
        recyclerView.addLinearDividerItemDecoration {
            headerSideDivider(Divider.space(20))
        }
        Assert.assertEquals(1, recyclerView.itemDecorationCount)
        (recyclerView.getItemDecorationAt(0) as LinearDividerItemDecoration).apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNotNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }
        recyclerView.addLinearDividerItemDecoration(0) {
            footerSideDivider(Divider.space(20))
        }
        (recyclerView.getItemDecorationAt(0) as LinearDividerItemDecoration).apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNotNull(itemDividerProvider.footerSideDividerConfig)
        }
        (recyclerView.getItemDecorationAt(1) as LinearDividerItemDecoration).apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNotNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }
    }

    @Test
    fun testGrid() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val recyclerView = RecyclerView(context)

        recyclerView.newGridDividerItemDecoration().apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }

        recyclerView.newGridDividerItemDecoration {
            headerAndFooterSideDivider(Divider.space(20))
        }.apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNotNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNotNull(itemDividerProvider.footerSideDividerConfig)
        }

        recyclerView.addGridDividerItemDecoration()
        (recyclerView.getItemDecorationAt(0) as GridDividerItemDecoration).apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }
        recyclerView.removeItemDecorationAt(0)

        recyclerView.addGridDividerItemDecoration {
            headerAndFooterSideDivider(Divider.space(20))
        }
        (recyclerView.getItemDecorationAt(0) as GridDividerItemDecoration).apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNotNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNotNull(itemDividerProvider.footerSideDividerConfig)
        }
        recyclerView.removeItemDecorationAt(0)

        Assert.assertEquals(0, recyclerView.itemDecorationCount)
        recyclerView.addGridDividerItemDecoration {
            headerSideDivider(Divider.space(20))
        }
        Assert.assertEquals(1, recyclerView.itemDecorationCount)
        (recyclerView.getItemDecorationAt(0) as GridDividerItemDecoration).apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNotNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }
        recyclerView.addGridDividerItemDecoration(0) {
            footerSideDivider(Divider.space(20))
        }
        (recyclerView.getItemDecorationAt(0) as GridDividerItemDecoration).apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNotNull(itemDividerProvider.footerSideDividerConfig)
        }
        (recyclerView.getItemDecorationAt(1) as GridDividerItemDecoration).apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNotNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }
    }

    @Test
    fun testStaggeredGrid() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val recyclerView = RecyclerView(context)

        recyclerView.newStaggeredGridDividerItemDecoration().apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }

        recyclerView.newStaggeredGridDividerItemDecoration {
            headerAndFooterSideDivider(Divider.space(20))
        }.apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNotNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNotNull(itemDividerProvider.footerSideDividerConfig)
        }

        recyclerView.addStaggeredGridDividerItemDecoration()
        (recyclerView.getItemDecorationAt(0) as StaggeredGridDividerItemDecoration).apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }
        recyclerView.removeItemDecorationAt(0)

        recyclerView.addStaggeredGridDividerItemDecoration {
            headerAndFooterSideDivider(Divider.space(20))
        }
        (recyclerView.getItemDecorationAt(0) as StaggeredGridDividerItemDecoration).apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNotNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNotNull(itemDividerProvider.footerSideDividerConfig)
        }
        recyclerView.removeItemDecorationAt(0)

        Assert.assertEquals(0, recyclerView.itemDecorationCount)
        recyclerView.addStaggeredGridDividerItemDecoration {
            headerSideDivider(Divider.space(20))
        }
        Assert.assertEquals(1, recyclerView.itemDecorationCount)
        (recyclerView.getItemDecorationAt(0) as StaggeredGridDividerItemDecoration).apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNotNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }
        recyclerView.addStaggeredGridDividerItemDecoration(0) {
            footerSideDivider(Divider.space(20))
        }
        (recyclerView.getItemDecorationAt(0) as StaggeredGridDividerItemDecoration).apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNotNull(itemDividerProvider.footerSideDividerConfig)
        }
        (recyclerView.getItemDecorationAt(1) as StaggeredGridDividerItemDecoration).apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNotNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }
    }
}