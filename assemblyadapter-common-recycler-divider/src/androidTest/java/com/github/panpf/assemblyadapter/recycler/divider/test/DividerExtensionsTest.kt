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
            Assert.assertNull(itemDividerProvider.sideHeaderDividerConfig)
            Assert.assertNull(itemDividerProvider.sideFooterDividerConfig)
        }

        recyclerView.newLinearDividerItemDecoration {
            sideHeaderAndFooterDivider(Divider.space(20))
        }.apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNotNull(itemDividerProvider.sideHeaderDividerConfig)
            Assert.assertNotNull(itemDividerProvider.sideFooterDividerConfig)
        }

        recyclerView.addLinearDividerItemDecoration()
        (recyclerView.getItemDecorationAt(0) as LinearDividerItemDecoration).apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNull(itemDividerProvider.sideHeaderDividerConfig)
            Assert.assertNull(itemDividerProvider.sideFooterDividerConfig)
        }
        recyclerView.removeItemDecorationAt(0)

        recyclerView.addLinearDividerItemDecoration {
            sideHeaderAndFooterDivider(Divider.space(20))
        }
        (recyclerView.getItemDecorationAt(0) as LinearDividerItemDecoration).apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNotNull(itemDividerProvider.sideHeaderDividerConfig)
            Assert.assertNotNull(itemDividerProvider.sideFooterDividerConfig)
        }
        recyclerView.removeItemDecorationAt(0)

        Assert.assertEquals(0, recyclerView.itemDecorationCount)
        recyclerView.addLinearDividerItemDecoration {
            sideHeaderDivider(Divider.space(20))
        }
        Assert.assertEquals(1, recyclerView.itemDecorationCount)
        (recyclerView.getItemDecorationAt(0) as LinearDividerItemDecoration).apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNotNull(itemDividerProvider.sideHeaderDividerConfig)
            Assert.assertNull(itemDividerProvider.sideFooterDividerConfig)
        }
        recyclerView.addLinearDividerItemDecoration(0) {
            sideFooterDivider(Divider.space(20))
        }
        (recyclerView.getItemDecorationAt(0) as LinearDividerItemDecoration).apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNull(itemDividerProvider.sideHeaderDividerConfig)
            Assert.assertNotNull(itemDividerProvider.sideFooterDividerConfig)
        }
        (recyclerView.getItemDecorationAt(1) as LinearDividerItemDecoration).apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNotNull(itemDividerProvider.sideHeaderDividerConfig)
            Assert.assertNull(itemDividerProvider.sideFooterDividerConfig)
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
            Assert.assertNull(itemDividerProvider.sideHeaderDividerConfig)
            Assert.assertNull(itemDividerProvider.sideFooterDividerConfig)
        }

        recyclerView.newGridDividerItemDecoration {
            sideHeaderAndFooterDivider(Divider.space(20))
        }.apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNotNull(itemDividerProvider.sideHeaderDividerConfig)
            Assert.assertNotNull(itemDividerProvider.sideFooterDividerConfig)
        }

        recyclerView.addGridDividerItemDecoration()
        (recyclerView.getItemDecorationAt(0) as GridDividerItemDecoration).apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNull(itemDividerProvider.sideHeaderDividerConfig)
            Assert.assertNull(itemDividerProvider.sideFooterDividerConfig)
        }
        recyclerView.removeItemDecorationAt(0)

        recyclerView.addGridDividerItemDecoration {
            sideHeaderAndFooterDivider(Divider.space(20))
        }
        (recyclerView.getItemDecorationAt(0) as GridDividerItemDecoration).apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNotNull(itemDividerProvider.sideHeaderDividerConfig)
            Assert.assertNotNull(itemDividerProvider.sideFooterDividerConfig)
        }
        recyclerView.removeItemDecorationAt(0)

        Assert.assertEquals(0, recyclerView.itemDecorationCount)
        recyclerView.addGridDividerItemDecoration {
            sideHeaderDivider(Divider.space(20))
        }
        Assert.assertEquals(1, recyclerView.itemDecorationCount)
        (recyclerView.getItemDecorationAt(0) as GridDividerItemDecoration).apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNotNull(itemDividerProvider.sideHeaderDividerConfig)
            Assert.assertNull(itemDividerProvider.sideFooterDividerConfig)
        }
        recyclerView.addGridDividerItemDecoration(0) {
            sideFooterDivider(Divider.space(20))
        }
        (recyclerView.getItemDecorationAt(0) as GridDividerItemDecoration).apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNull(itemDividerProvider.sideHeaderDividerConfig)
            Assert.assertNotNull(itemDividerProvider.sideFooterDividerConfig)
        }
        (recyclerView.getItemDecorationAt(1) as GridDividerItemDecoration).apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNotNull(itemDividerProvider.sideHeaderDividerConfig)
            Assert.assertNull(itemDividerProvider.sideFooterDividerConfig)
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
            Assert.assertNull(itemDividerProvider.sideHeaderDividerConfig)
            Assert.assertNull(itemDividerProvider.sideFooterDividerConfig)
        }

        recyclerView.newStaggeredGridDividerItemDecoration {
            sideHeaderAndFooterDivider(Divider.space(20))
        }.apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNotNull(itemDividerProvider.sideHeaderDividerConfig)
            Assert.assertNotNull(itemDividerProvider.sideFooterDividerConfig)
        }

        recyclerView.addStaggeredGridDividerItemDecoration()
        (recyclerView.getItemDecorationAt(0) as StaggeredGridDividerItemDecoration).apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNull(itemDividerProvider.sideHeaderDividerConfig)
            Assert.assertNull(itemDividerProvider.sideFooterDividerConfig)
        }
        recyclerView.removeItemDecorationAt(0)

        recyclerView.addStaggeredGridDividerItemDecoration {
            sideHeaderAndFooterDivider(Divider.space(20))
        }
        (recyclerView.getItemDecorationAt(0) as StaggeredGridDividerItemDecoration).apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNotNull(itemDividerProvider.sideHeaderDividerConfig)
            Assert.assertNotNull(itemDividerProvider.sideFooterDividerConfig)
        }
        recyclerView.removeItemDecorationAt(0)

        Assert.assertEquals(0, recyclerView.itemDecorationCount)
        recyclerView.addStaggeredGridDividerItemDecoration {
            sideHeaderDivider(Divider.space(20))
        }
        Assert.assertEquals(1, recyclerView.itemDecorationCount)
        (recyclerView.getItemDecorationAt(0) as StaggeredGridDividerItemDecoration).apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNotNull(itemDividerProvider.sideHeaderDividerConfig)
            Assert.assertNull(itemDividerProvider.sideFooterDividerConfig)
        }
        recyclerView.addStaggeredGridDividerItemDecoration(0) {
            sideFooterDivider(Divider.space(20))
        }
        (recyclerView.getItemDecorationAt(0) as StaggeredGridDividerItemDecoration).apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNull(itemDividerProvider.sideHeaderDividerConfig)
            Assert.assertNotNull(itemDividerProvider.sideFooterDividerConfig)
        }
        (recyclerView.getItemDecorationAt(1) as StaggeredGridDividerItemDecoration).apply {
            Assert.assertNotNull(itemDividerProvider.dividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNotNull(itemDividerProvider.sideHeaderDividerConfig)
            Assert.assertNull(itemDividerProvider.sideFooterDividerConfig)
        }
    }
}