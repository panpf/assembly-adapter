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
package com.github.panpf.assemblyadapter.pager2.paging.test

import android.R
import androidx.fragment.app.FragmentActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.panpf.assemblyadapter.pager.ViewFragmentItemFactory
import com.github.panpf.assemblyadapter.pager2.paging.AssemblyPagingDataFragmentStateAdapter
import com.github.panpf.assemblyadapter.recycler.DiffKey
import com.github.panpf.tools4j.test.ktx.assertNoThrow
import com.github.panpf.tools4j.test.ktx.assertThrow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AssemblyPagingDataFragmentStateAdapterTest {

    data class HaveKeyData(val name: String) : DiffKey {
        override val diffKey: String = name
    }

    data class NoKeyData(val time: Long)

    private val haveKeyItemFactory =
        ViewFragmentItemFactory(HaveKeyData::class, R.layout.activity_list_item)
    private val noKeyItemFactory =
        ViewFragmentItemFactory(NoKeyData::class, R.layout.activity_list_item)

    @Test
    fun testDataClassDiffKey() {
        val activity = runBlocking {
            withContext(Dispatchers.Main) {
                FragmentActivity()
            }
        }

        assertNoThrow {
            AssemblyPagingDataFragmentStateAdapter<Any>(
                activity, listOf(haveKeyItemFactory)
            )
        }
        assertNoThrow {
            AssemblyPagingDataFragmentStateAdapter<Any>(
                activity, listOf(haveKeyItemFactory, haveKeyItemFactory)
            )
        }

        assertThrow(IllegalArgumentException::class) {
            AssemblyPagingDataFragmentStateAdapter<Any>(
                activity, listOf(haveKeyItemFactory, noKeyItemFactory),
            )
        }
        assertThrow(IllegalArgumentException::class) {
            AssemblyPagingDataFragmentStateAdapter<Any>(
                activity, listOf(noKeyItemFactory, haveKeyItemFactory),
            )
        }
        assertThrow(IllegalArgumentException::class) {
            AssemblyPagingDataFragmentStateAdapter<Any>(
                activity, listOf(noKeyItemFactory, noKeyItemFactory),
            )
        }
        assertThrow(IllegalArgumentException::class) {
            AssemblyPagingDataFragmentStateAdapter<Any>(
                activity, listOf(noKeyItemFactory),
            )
        }
    }
}