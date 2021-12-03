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
package com.github.panpf.assemblyadapter.common.recycler.divider.test

import androidx.recyclerview.widget.RecyclerView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.recycler.divider.Divider
import com.github.panpf.assemblyadapter.recycler.divider.GridDividerItemDecoration
import com.github.panpf.assemblyadapter.recycler.divider.LinearDividerItemDecoration
import com.github.panpf.assemblyadapter.recycler.divider.StaggeredGridDividerItemDecoration
import com.github.panpf.assemblyadapter.recycler.divider.addGridDividerItemDecoration
import com.github.panpf.assemblyadapter.recycler.divider.addLinearDividerItemDecoration
import com.github.panpf.assemblyadapter.recycler.divider.addStaggeredGridDividerItemDecoration
import com.github.panpf.assemblyadapter.recycler.divider.newGridDividerItemDecoration
import com.github.panpf.assemblyadapter.recycler.divider.newLinearDividerItemDecoration
import com.github.panpf.assemblyadapter.recycler.divider.newStaggeredGridDividerItemDecoration
import org.junit.Assert
import org.junit.Test

class DividerExtensionsTest {

    @Test
    fun testLinear() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val recyclerView = RecyclerView(context)

        recyclerView.newLinearDividerItemDecoration().apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }

        recyclerView.newLinearDividerItemDecoration {
            sideHeaderAndFooterDivider(Divider.space(20))
        }.apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNotNull(sideHeaderDividerConfig)
            Assert.assertNotNull(sideFooterDividerConfig)
        }

        recyclerView.addLinearDividerItemDecoration()
        (recyclerView.getItemDecorationAt(0) as LinearDividerItemDecoration).apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }
        recyclerView.removeItemDecorationAt(0)

        recyclerView.addLinearDividerItemDecoration {
            sideHeaderAndFooterDivider(Divider.space(20))
        }
        (recyclerView.getItemDecorationAt(0) as LinearDividerItemDecoration).apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNotNull(sideHeaderDividerConfig)
            Assert.assertNotNull(sideFooterDividerConfig)
        }
        recyclerView.removeItemDecorationAt(0)

        Assert.assertEquals(0, recyclerView.itemDecorationCount)
        recyclerView.addLinearDividerItemDecoration {
            sideHeaderDivider(Divider.space(20))
        }
        Assert.assertEquals(1, recyclerView.itemDecorationCount)
        (recyclerView.getItemDecorationAt(0) as LinearDividerItemDecoration).apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNotNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }
        recyclerView.addLinearDividerItemDecoration(0) {
            sideFooterDivider(Divider.space(20))
        }
        (recyclerView.getItemDecorationAt(0) as LinearDividerItemDecoration).apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNotNull(sideFooterDividerConfig)
        }
        (recyclerView.getItemDecorationAt(1) as LinearDividerItemDecoration).apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNotNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }
    }

    @Test
    fun testGrid() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val recyclerView = RecyclerView(context)

        recyclerView.newGridDividerItemDecoration().apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(sideDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }

        recyclerView.newGridDividerItemDecoration {
            sideDivider(Divider.space(20))
            sideHeaderAndFooterDivider(Divider.space(20))
        }.apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNotNull(sideDividerConfig)
            Assert.assertNotNull(sideHeaderDividerConfig)
            Assert.assertNotNull(sideFooterDividerConfig)
        }

        recyclerView.addGridDividerItemDecoration()
        (recyclerView.getItemDecorationAt(0) as GridDividerItemDecoration).apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(sideDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }
        recyclerView.removeItemDecorationAt(0)

        recyclerView.addGridDividerItemDecoration {
            sideDivider(Divider.space(20))
            sideHeaderAndFooterDivider(Divider.space(20))
        }
        (recyclerView.getItemDecorationAt(0) as GridDividerItemDecoration).apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNotNull(sideDividerConfig)
            Assert.assertNotNull(sideHeaderDividerConfig)
            Assert.assertNotNull(sideFooterDividerConfig)
        }
        recyclerView.removeItemDecorationAt(0)

        Assert.assertEquals(0, recyclerView.itemDecorationCount)
        recyclerView.addGridDividerItemDecoration {
            sideDivider(Divider.space(20))
            sideHeaderDivider(Divider.space(20))
        }
        Assert.assertEquals(1, recyclerView.itemDecorationCount)
        (recyclerView.getItemDecorationAt(0) as GridDividerItemDecoration).apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNotNull(sideDividerConfig)
            Assert.assertNotNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }
        recyclerView.addGridDividerItemDecoration(0) {
            sideDivider(Divider.space(20))
            sideFooterDivider(Divider.space(20))
        }
        (recyclerView.getItemDecorationAt(0) as GridDividerItemDecoration).apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNotNull(sideDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNotNull(sideFooterDividerConfig)
        }
        (recyclerView.getItemDecorationAt(1) as GridDividerItemDecoration).apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNotNull(sideDividerConfig)
            Assert.assertNotNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }
    }

    @Test
    fun testStaggeredGrid() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val recyclerView = RecyclerView(context)

        recyclerView.newStaggeredGridDividerItemDecoration().apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(sideDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }

        recyclerView.newStaggeredGridDividerItemDecoration {
            sideDivider(Divider.space(20))
            sideHeaderAndFooterDivider(Divider.space(20))
        }.apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNotNull(sideDividerConfig)
            Assert.assertNotNull(sideHeaderDividerConfig)
            Assert.assertNotNull(sideFooterDividerConfig)
        }

        recyclerView.addStaggeredGridDividerItemDecoration()
        (recyclerView.getItemDecorationAt(0) as StaggeredGridDividerItemDecoration).apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(sideDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }
        recyclerView.removeItemDecorationAt(0)

        recyclerView.addStaggeredGridDividerItemDecoration {
            sideDivider(Divider.space(20))
            sideHeaderAndFooterDivider(Divider.space(20))
        }
        (recyclerView.getItemDecorationAt(0) as StaggeredGridDividerItemDecoration).apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNotNull(sideDividerConfig)
            Assert.assertNotNull(sideHeaderDividerConfig)
            Assert.assertNotNull(sideFooterDividerConfig)
        }
        recyclerView.removeItemDecorationAt(0)

        Assert.assertEquals(0, recyclerView.itemDecorationCount)
        recyclerView.addStaggeredGridDividerItemDecoration {
            sideDivider(Divider.space(20))
            sideHeaderDivider(Divider.space(20))
        }
        Assert.assertEquals(1, recyclerView.itemDecorationCount)
        (recyclerView.getItemDecorationAt(0) as StaggeredGridDividerItemDecoration).apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNotNull(sideDividerConfig)
            Assert.assertNotNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }
        recyclerView.addStaggeredGridDividerItemDecoration(0) {
            sideDivider(Divider.space(20))
            sideFooterDivider(Divider.space(20))
        }
        (recyclerView.getItemDecorationAt(0) as StaggeredGridDividerItemDecoration).apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNotNull(sideDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNotNull(sideFooterDividerConfig)
        }
        (recyclerView.getItemDecorationAt(1) as StaggeredGridDividerItemDecoration).apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNotNull(sideDividerConfig)
            Assert.assertNotNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }
    }
}