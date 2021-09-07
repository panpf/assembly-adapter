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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.viewpager2.widget.ViewPager2
import com.github.panpf.assemblyadapter.pager.FragmentItemFactory
import com.github.panpf.assemblyadapter.pager2.paging.PagingDataFragmentStateAdapter
import com.github.panpf.assemblyadapter.recycler.DiffKey
import com.github.panpf.assemblyadapter.recycler.KeyEqualsDiffItemCallback
import com.github.panpf.tools4a.test.ktx.getFragmentSync
import com.github.panpf.tools4a.test.ktx.launchFragmentInContainer
import com.github.panpf.tools4j.test.ktx.assertNoThrow
import com.github.panpf.tools4j.test.ktx.assertThrow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PagingDataFragmentStateAdapterTest {

    data class Text(val text: String) : DiffKey {
        override val diffKey: Any = "Text:$text"
    }

    class TextFragmentItemFactory : FragmentItemFactory<Text>(Text::class) {
        override fun createFragment(
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: Text
        ): Fragment = TextFragment()
    }

    class TextFragment : Fragment()

    class TestPagingDataFragmentStateAdapter :
        PagingDataFragmentStateAdapter<Text, RecyclerView.ViewHolder> {
        constructor(
            fragmentManager: FragmentManager,
            lifecycle: Lifecycle,
            diffCallback: DiffUtil.ItemCallback<Text> = KeyEqualsDiffItemCallback(),
            mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
            workerDispatcher: CoroutineDispatcher = Dispatchers.Default
        ) : super(fragmentManager, lifecycle, diffCallback, mainDispatcher, workerDispatcher)

        constructor(
            fragmentActivity: FragmentActivity,
            diffCallback: DiffUtil.ItemCallback<Text> = KeyEqualsDiffItemCallback(),
            mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
            workerDispatcher: CoroutineDispatcher = Dispatchers.Default
        ) : super(fragmentActivity, diffCallback, mainDispatcher, workerDispatcher)

        constructor(
            fragment: Fragment,
            diffCallback: DiffUtil.ItemCallback<Text> = KeyEqualsDiffItemCallback(),
            mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
            workerDispatcher: CoroutineDispatcher = Dispatchers.Default
        ) : super(fragment, diffCallback, mainDispatcher, workerDispatcher)

        override fun createFragment(position: Int): Fragment {
            return TextFragment()
        }
    }

    class TestFragment : Fragment()

    class PagingTestFragment : Fragment() {

        lateinit var pagingDataAdapter: TestPagingDataFragmentStateAdapter

        private var dataList: List<Text>? = null

        private val pagingFlow = Pager(
            PagingConfig(pageSize = 10, enablePlaceholders = false),
            initialKey = 0,
            pagingSourceFactory = { TestPagingSource(dataList) }
        ).flow

        class TestPagingSource(private val dataList: List<Text>?) :
            PagingSource<Int, Text>() {
            override fun getRefreshKey(state: PagingState<Int, Text>): Int = 0

            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Text> {
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
        ): View = ViewPager2(requireContext())

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            pagingDataAdapter = TestPagingDataFragmentStateAdapter(this)

            (view as ViewPager2).apply {
                adapter = pagingDataAdapter

                lifecycleScope.launch {
                    pagingFlow.collect {
                        pagingDataAdapter.submitData(it)
                    }
                }
            }
        }

        fun submitList(list: List<Text>?) {
            dataList = list
            pagingDataAdapter.refresh()
        }
    }

    @Test
    fun testConstructor() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()

        assertNoThrow {
            TestPagingDataFragmentStateAdapter(fragment)
        }
        assertNoThrow {
            TestPagingDataFragmentStateAdapter(fragment.requireActivity())
        }
        assertNoThrow {
            TestPagingDataFragmentStateAdapter(fragment.childFragmentManager, fragment.lifecycle)
        }
    }

    @Test
    fun testMethodGetCount() {
        val fragmentScenario = PagingTestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        val pagingDataAdapter: TestPagingDataFragmentStateAdapter =
            fragment.pagingDataAdapter
        pagingDataAdapter.apply {
            Assert.assertEquals(0, itemCount)

            fragment.submitList(listOf(Text("hello")))
            Thread.sleep(100)
            Assert.assertEquals(1, itemCount)

            fragment.submitList(listOf(Text("hello"), Text("world")))
            Thread.sleep(100)
            Assert.assertEquals(2, itemCount)

            fragment.submitList(null)
            Thread.sleep(100)
            Assert.assertEquals(0, itemCount)
        }
    }

    @Test
    fun testMethodGetItemId() {
        val fragmentScenario = PagingTestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()

        TestPagingDataFragmentStateAdapter(fragment).apply {
            assertThrow(UnsupportedOperationException::class) {
                setHasStableIds(true)
            }
            Assert.assertEquals(-1L, getItemId(-1))
            Assert.assertEquals(0L, getItemId(0))
            Assert.assertEquals(1L, getItemId(1))
        }

        val pagingDataAdapter: TestPagingDataFragmentStateAdapter =
            fragment.pagingDataAdapter
        pagingDataAdapter.apply {
            assertThrow(UnsupportedOperationException::class) {
                setHasStableIds(true)
            }
            Assert.assertEquals(-1L, getItemId(-1))
            Assert.assertEquals(0L, getItemId(0))
            Assert.assertEquals(1L, getItemId(1))

            fragment.submitList(listOf(Text("hello"), Text("world")))
            Thread.sleep(100)
            Assert.assertEquals(-1L, getItemId(-1))
            Assert.assertEquals(0L, getItemId(0))
            Assert.assertEquals(1L, getItemId(1))
            Assert.assertEquals(2L, getItemId(2))
        }
    }

    @Test
    fun testMethodCreateFragment() {
        val fragmentScenario = PagingTestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        val pagingDataAdapter: TestPagingDataFragmentStateAdapter =
            fragment.pagingDataAdapter
        pagingDataAdapter.apply {
            fragment.submitList(listOf(Text("hello"), Text("world")))
            Thread.sleep(100)

            Assert.assertTrue(createFragment(0) is TextFragment)
            Assert.assertTrue(createFragment(1) is TextFragment)
        }
    }
}