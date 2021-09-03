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

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.ViewItemFactory
import com.github.panpf.assemblyadapter.recycler.DiffKey
import com.github.panpf.assemblyadapter.recycler.InstanceDiffItemCallback
import com.github.panpf.assemblyadapter.recycler.KeyEqualsDiffItemCallback
import com.github.panpf.assemblyadapter.recycler.paging.AssemblyPagingDataAdapter
import com.github.panpf.tools4a.test.ktx.getFragmentSync
import com.github.panpf.tools4a.test.ktx.launchFragmentInContainer
import com.github.panpf.tools4j.test.ktx.assertThrow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AssemblyPagingDataAdapterTest {

    data class Text(val text: String) : DiffKey {
        override val diffKey: Any = "Text:$text"
    }

    class TextItemFactory : ViewItemFactory<Text>(Text::class, { context, _, _ ->
        TextView(context)
    })

    data class Image(val resId: Int) : DiffKey {
        override val diffKey: Any = "Image:$resId"
    }

    class ImageItemFactory : ViewItemFactory<Image>(Image::class, { context, _, _ ->
        ImageView(context)
    })

    private data class NoDiffKey(val name: String = "")

    private class NoDiffKeyItemFactory :
        ViewItemFactory<NoDiffKey>(NoDiffKey::class, { context, _, _ ->
            ImageView(context)
        })

    class PagingTestFragment : Fragment() {

        val pagingDataAdapter = AssemblyPagingDataAdapter<Any>(
            listOf(TextItemFactory(), ImageItemFactory())
        )

        private var dataList: List<Any>? = null

        private val pagingFlow = Pager(
            PagingConfig(pageSize = 10, enablePlaceholders = false),
            initialKey = 0,
            pagingSourceFactory = { TestPagingSource(dataList) }
        ).flow

        class TestPagingSource(private val dataList: List<Any>?) :
            PagingSource<Int, Any>() {
            override fun getRefreshKey(state: PagingState<Int, Any>): Int = 0

            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Any> {
                return LoadResult.Page(
                    dataList ?: emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View = RecyclerView(requireContext())

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            (view as RecyclerView).apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = pagingDataAdapter

                lifecycleScope.launch {
                    pagingFlow.collect {
                        pagingDataAdapter.submitData(it)
                    }
                }
            }
        }

        fun submitList(list: List<Any>?) {
            dataList = list
            pagingDataAdapter.refresh()
        }
    }

    @Test
    fun testConstructor() {
        /**
         * ItemFactory, List, ItemCallback
         */
        // ItemFactory, ItemCallback
        assertThrow(IllegalArgumentException::class) {
            AssemblyPagingDataAdapter<Text>(listOf(), KeyEqualsDiffItemCallback())
        }
        AssemblyPagingDataAdapter<Text>(listOf(TextItemFactory()), KeyEqualsDiffItemCallback())

        // ItemCallback
        assertThrow(IllegalArgumentException::class) {
            AssemblyPagingDataAdapter(listOf(NoDiffKeyItemFactory()), KeyEqualsDiffItemCallback())
        }
        AssemblyPagingDataAdapter(listOf(NoDiffKeyItemFactory()), InstanceDiffItemCallback())

        // ItemFactory
        AssemblyPagingDataAdapter<Any>(listOf(TextItemFactory()))
        assertThrow(IllegalArgumentException::class) {
            AssemblyPagingDataAdapter<Any>(listOf(NoDiffKeyItemFactory()))
        }
    }


    @Test
    fun testPropertyCurrentListAndSubmitList() {
        val fragmentScenario = PagingTestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        val pagingDataAdapter: AssemblyPagingDataAdapter<Any> = fragment.pagingDataAdapter
        pagingDataAdapter.apply {
            Assert.assertEquals("", currentList.joinToString())

            fragment.submitList(listOf(Text("hello")))
            Thread.sleep(30)
            Assert.assertEquals("Text(text=hello)", currentList.joinToString())

            fragment.submitList(listOf(Text("hello"), Text("world")))
            Thread.sleep(30)
            Assert.assertEquals("Text(text=hello), Text(text=world)", currentList.joinToString())

            fragment.submitList(null)
            Thread.sleep(30)
            Assert.assertEquals("", currentList.joinToString())
        }
    }

    @Test
    fun testMethodGetCount() {
        val fragmentScenario = PagingTestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        val pagingDataAdapter: AssemblyPagingDataAdapter<Any> = fragment.pagingDataAdapter
        pagingDataAdapter.apply {
            Assert.assertEquals(0, itemCount)

            fragment.submitList(listOf(Text("hello")))
            Thread.sleep(30)
            Assert.assertEquals(1, itemCount)

            fragment.submitList(listOf(Text("hello"), Text("world")))
            Thread.sleep(30)
            Assert.assertEquals(2, itemCount)

            fragment.submitList(null)
            Thread.sleep(30)
            Assert.assertEquals(0, itemCount)
        }
    }

    @Test
    fun testMethodGetItem() {
        val fragmentScenario = PagingTestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        val pagingDataAdapter: AssemblyPagingDataAdapter<Any> = fragment.pagingDataAdapter
        pagingDataAdapter.apply {
            assertThrow(IndexOutOfBoundsException::class) {
                peek(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                peek(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                peek(1)
            }

            fragment.submitList(listOf(Text("hello"), Text("world")))
            Thread.sleep(30)
            Assert.assertEquals(Text("hello"), peek(0))
            Assert.assertEquals(Text("world"), peek(1))
        }
    }

    @Test
    fun testMethodGetItemId() {
        AssemblyPagingDataAdapter<Text>(listOf(TextItemFactory())).apply {
            assertThrow(UnsupportedOperationException::class) {
                setHasStableIds(true)
            }
            Assert.assertEquals(-1L, getItemId(-1))
            Assert.assertEquals(-1L, getItemId(0))
            Assert.assertEquals(-1L, getItemId(1))
        }

        val fragmentScenario = PagingTestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        val pagingDataAdapter: AssemblyPagingDataAdapter<Any> = fragment.pagingDataAdapter
        pagingDataAdapter.apply {
            assertThrow(UnsupportedOperationException::class) {
                setHasStableIds(true)
            }
            Assert.assertEquals(-1L, getItemId(-1))
            Assert.assertEquals(-1L, getItemId(0))
            Assert.assertEquals(-1L, getItemId(1))

            fragment.submitList(listOf(Text("hello"), Text("world")))
            Thread.sleep(30)
            Assert.assertEquals(-1L, getItemId(-1))
            Assert.assertEquals(-1L, getItemId(0))
            Assert.assertEquals(-1L, getItemId(1))
            Assert.assertEquals(-1L, getItemId(2))
        }
    }

    @Test
    fun testMethodGetItemViewType() {
        val fragmentScenario = PagingTestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        val pagingDataAdapter: AssemblyPagingDataAdapter<Any> = fragment.pagingDataAdapter
        pagingDataAdapter.apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItemViewType(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemViewType(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemViewType(1)
            }

            fragment.submitList(listOf(Image(R.drawable.alert_dark_frame), Text("hello")))
            Thread.sleep(30)
            Assert.assertEquals(1, getItemViewType(0))
            Assert.assertEquals(0, getItemViewType(1))
        }
    }

    @Test
    fun testMethodCreateAndBindViewHolder() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)
        val fragmentScenario = PagingTestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        val pagingDataAdapter: AssemblyPagingDataAdapter<Any> = fragment.pagingDataAdapter
        pagingDataAdapter.apply {
            fragment.submitList(listOf(Text("hello"), Image(R.drawable.alert_dark_frame)))
            Thread.sleep(30)

            assertThrow(IllegalArgumentException::class) {
                onCreateViewHolder(parent, -1)
            }
            Assert.assertTrue(onCreateViewHolder(parent, getItemViewType(0)).itemView is TextView)
            Assert.assertTrue(onCreateViewHolder(parent, getItemViewType(1)).itemView is ImageView)

            assertThrow(IllegalArgumentException::class) {
                onBindViewHolder(object : RecyclerView.ViewHolder(TextView(context)) {}, 0)
            }
            onBindViewHolder(onCreateViewHolder(parent, getItemViewType(0)), 0)
            onBindViewHolder(onCreateViewHolder(parent, getItemViewType(1)), 1)
        }
    }

    @Test
    fun testMethodGetItemFactoryByPosition() {
        val fragmentScenario = PagingTestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        val pagingDataAdapter: AssemblyPagingDataAdapter<Any> = fragment.pagingDataAdapter
        pagingDataAdapter.apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByPosition(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByPosition(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByPosition(1)
            }

            fragment.submitList(listOf(Image(R.drawable.alert_dark_frame), Text("hello")))
            Thread.sleep(30)
            Assert.assertEquals(ImageItemFactory::class, getItemFactoryByPosition(0)::class)
            Assert.assertEquals(TextItemFactory::class, getItemFactoryByPosition(1)::class)
        }
    }
}