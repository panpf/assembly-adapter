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
package com.github.panpf.assemblyadapter.recycler.paging.test

import android.widget.TextView
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.NotFoundMatchedItemFactoryException
import com.github.panpf.assemblyadapter.ViewItemFactory
import com.github.panpf.assemblyadapter.recycler.paging.AssemblyLoadStateAdapter
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AssemblyLoadStateAdapterTest {

    class LoadStateItemFactory : ViewItemFactory<LoadState>(LoadState::class, { context, _, _ ->
        TextView(context)
    })

    @Test
    fun testConstructor() {
        AssemblyLoadStateAdapter(LoadStateItemFactory())
        AssemblyLoadStateAdapter(LoadStateItemFactory(), true)
    }

    @Test
    fun testMethodGetItemData() {
        AssemblyLoadStateAdapter(LoadStateItemFactory()).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItemData(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemData(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemData(1)
            }

            loadState = LoadState.Loading
            Assert.assertEquals(LoadState.Loading, getItemData(0))
        }
    }

    @Test
    fun testMethodCreateAndBindViewHolder() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = RecyclerView(context).apply {
            layoutManager = LinearLayoutManager(context)
        }
        AssemblyLoadStateAdapter(LoadStateItemFactory()).apply {
            loadState = LoadState.Loading

            onCreateViewHolder(parent, -1)
            Assert.assertTrue(onCreateViewHolder(parent, 0).itemView is TextView)

            onBindViewHolder(onCreateViewHolder(parent, 0), 0)
        }
    }

    @Test
    fun testMethodGetItemFactoryByPosition() {
        val itemFactory = LoadStateItemFactory()
        AssemblyLoadStateAdapter(itemFactory).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByPosition(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByPosition(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByPosition(1)
            }

            loadState = LoadState.Loading
            Assert.assertSame(itemFactory, getItemFactoryByPosition(0))
        }
    }

    @Test
    fun testMethodGetItemFactoryByData() {
        val itemFactory = LoadStateItemFactory()

        AssemblyLoadStateAdapter(itemFactory).apply {
            Assert.assertSame(itemFactory, getItemFactoryByData(LoadState.Loading))
        }
    }

    @Test
    fun testMethodGetItemFactoryByClass() {
        val itemFactory = LoadStateItemFactory()

        AssemblyLoadStateAdapter(itemFactory).apply {
            Assert.assertSame(
                itemFactory,
                getItemFactoryByClass(LoadStateItemFactory::class.java)
            )
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                getItemFactoryByClass(ViewItemFactory::class.java)
            }
        }
    }

    @Test
    fun testDisplayLoadStateAsItem() {
        AssemblyLoadStateAdapter(LoadStateItemFactory()).apply {
            Assert.assertTrue(displayLoadStateAsItem(LoadState.Loading))
            Assert.assertTrue(displayLoadStateAsItem(LoadState.Error(Exception())))
            Assert.assertFalse(displayLoadStateAsItem(LoadState.NotLoading(false)))
            Assert.assertFalse(displayLoadStateAsItem(LoadState.NotLoading(true)))
        }

        AssemblyLoadStateAdapter(
            itemFactory = LoadStateItemFactory(),
            alwaysShowWhenEndOfPaginationReached = true
        ).apply {
            Assert.assertTrue(displayLoadStateAsItem(LoadState.Loading))
            Assert.assertTrue(displayLoadStateAsItem(LoadState.Error(Exception())))
            Assert.assertFalse(displayLoadStateAsItem(LoadState.NotLoading(false)))
            Assert.assertTrue(displayLoadStateAsItem(LoadState.NotLoading(true)))
        }
    }
}