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
package com.github.panpf.assemblyadapter.recycler.test.divider

import androidx.recyclerview.widget.RecyclerView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.recycler.divider.*
import org.junit.Assert
import org.junit.Test

class AssemblyDividerExtensionsTest {

    @Test
    fun testLinear() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val recyclerView = RecyclerView(context)

        recyclerView.newAssemblyLinearDividerItemDecoration().apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }

        recyclerView.newAssemblyLinearDividerItemDecoration {
            sideHeaderAndFooterDivider(Divider.space(20))
        }.apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNotNull(sideHeaderDividerConfig)
            Assert.assertNotNull(sideFooterDividerConfig)
        }

        recyclerView.addAssemblyLinearDividerItemDecoration()
        (recyclerView.getItemDecorationAt(0) as AssemblyLinearDividerItemDecoration).apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }
        recyclerView.removeItemDecorationAt(0)

        recyclerView.addAssemblyLinearDividerItemDecoration {
            sideHeaderAndFooterDivider(Divider.space(20))
        }
        (recyclerView.getItemDecorationAt(0) as AssemblyLinearDividerItemDecoration).apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNotNull(sideHeaderDividerConfig)
            Assert.assertNotNull(sideFooterDividerConfig)
        }
        recyclerView.removeItemDecorationAt(0)

        Assert.assertEquals(0, recyclerView.itemDecorationCount)
        recyclerView.addAssemblyLinearDividerItemDecoration {
            sideHeaderDivider(Divider.space(20))
        }
        Assert.assertEquals(1, recyclerView.itemDecorationCount)
        (recyclerView.getItemDecorationAt(0) as AssemblyLinearDividerItemDecoration).apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNotNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }
        recyclerView.addAssemblyLinearDividerItemDecoration(0) {
            sideFooterDivider(Divider.space(20))
        }
        (recyclerView.getItemDecorationAt(0) as AssemblyLinearDividerItemDecoration).apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNotNull(sideFooterDividerConfig)
        }
        (recyclerView.getItemDecorationAt(1) as AssemblyLinearDividerItemDecoration).apply {
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

        recyclerView.newAssemblyGridDividerItemDecoration().apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }

        recyclerView.newAssemblyGridDividerItemDecoration {
            sideHeaderAndFooterDivider(Divider.space(20))
        }.apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNotNull(sideHeaderDividerConfig)
            Assert.assertNotNull(sideFooterDividerConfig)
        }

        recyclerView.addAssemblyGridDividerItemDecoration()
        (recyclerView.getItemDecorationAt(0) as AssemblyGridDividerItemDecoration).apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }
        recyclerView.removeItemDecorationAt(0)

        recyclerView.addAssemblyGridDividerItemDecoration {
            sideHeaderAndFooterDivider(Divider.space(20))
        }
        (recyclerView.getItemDecorationAt(0) as AssemblyGridDividerItemDecoration).apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNotNull(sideHeaderDividerConfig)
            Assert.assertNotNull(sideFooterDividerConfig)
        }
        recyclerView.removeItemDecorationAt(0)

        Assert.assertEquals(0, recyclerView.itemDecorationCount)
        recyclerView.addAssemblyGridDividerItemDecoration {
            sideHeaderDivider(Divider.space(20))
        }
        Assert.assertEquals(1, recyclerView.itemDecorationCount)
        (recyclerView.getItemDecorationAt(0) as AssemblyGridDividerItemDecoration).apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNotNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }
        recyclerView.addAssemblyGridDividerItemDecoration(0) {
            sideFooterDivider(Divider.space(20))
        }
        (recyclerView.getItemDecorationAt(0) as AssemblyGridDividerItemDecoration).apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNotNull(sideFooterDividerConfig)
        }
        (recyclerView.getItemDecorationAt(1) as AssemblyGridDividerItemDecoration).apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNotNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }
    }

    @Test
    fun testStaggeredGrid() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val recyclerView = RecyclerView(context)

        recyclerView.newAssemblyStaggeredGridDividerItemDecoration().apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }

        recyclerView.newAssemblyStaggeredGridDividerItemDecoration {
            sideHeaderAndFooterDivider(Divider.space(20))
        }.apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNotNull(sideHeaderDividerConfig)
            Assert.assertNotNull(sideFooterDividerConfig)
        }

        recyclerView.addAssemblyStaggeredGridDividerItemDecoration()
        (recyclerView.getItemDecorationAt(0) as AssemblyStaggeredGridDividerItemDecoration).apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }
        recyclerView.removeItemDecorationAt(0)

        recyclerView.addAssemblyStaggeredGridDividerItemDecoration {
            sideHeaderAndFooterDivider(Divider.space(20))
        }
        (recyclerView.getItemDecorationAt(0) as AssemblyStaggeredGridDividerItemDecoration).apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNotNull(sideHeaderDividerConfig)
            Assert.assertNotNull(sideFooterDividerConfig)
        }
        recyclerView.removeItemDecorationAt(0)

        Assert.assertEquals(0, recyclerView.itemDecorationCount)
        recyclerView.addAssemblyStaggeredGridDividerItemDecoration {
            sideHeaderDivider(Divider.space(20))
        }
        Assert.assertEquals(1, recyclerView.itemDecorationCount)
        (recyclerView.getItemDecorationAt(0) as AssemblyStaggeredGridDividerItemDecoration).apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNotNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }
        recyclerView.addAssemblyStaggeredGridDividerItemDecoration(0) {
            sideFooterDivider(Divider.space(20))
        }
        (recyclerView.getItemDecorationAt(0) as AssemblyStaggeredGridDividerItemDecoration).apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNotNull(sideFooterDividerConfig)
        }
        (recyclerView.getItemDecorationAt(1) as AssemblyStaggeredGridDividerItemDecoration).apply {
            Assert.assertNotNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNotNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }
    }
}