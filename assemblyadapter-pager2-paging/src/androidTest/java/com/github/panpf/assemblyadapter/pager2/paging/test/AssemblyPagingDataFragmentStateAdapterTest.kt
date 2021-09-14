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
import androidx.lifecycle.lifecycleScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.viewpager2.widget.ViewPager2
import com.github.panpf.assemblyadapter.NotFoundMatchedItemFactoryException
import com.github.panpf.assemblyadapter.pager.FragmentItemFactory
import com.github.panpf.assemblyadapter.pager.ViewFragmentItemFactory
import com.github.panpf.assemblyadapter.pager2.paging.AssemblyPagingDataFragmentStateAdapter
import com.github.panpf.assemblyadapter.recycler.DiffKey
import com.github.panpf.tools4a.test.ktx.getFragmentSync
import com.github.panpf.tools4a.test.ktx.launchFragmentInContainer
import com.github.panpf.tools4j.test.ktx.assertNoThrow
import com.github.panpf.tools4j.test.ktx.assertThrow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class AssemblyPagingDataFragmentStateAdapterTest {

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

    data class Image(val resId: Int) : DiffKey {
        override val diffKey: Any = "Image:$resId"
    }

    class ImageFragmentItemFactory : FragmentItemFactory<Image>(Image::class) {
        override fun createFragment(
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: Image
        ): Fragment = ImageFragment()
    }

    class ImageFragment : Fragment()

    private data class NoDiffKey(val name: String = "")

    private class NoDiffKeyItemFactory : FragmentItemFactory<NoDiffKey>(NoDiffKey::class) {
        override fun createFragment(
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: NoDiffKey
        ): Fragment = NoDiffKeyFragment()
    }

    class NoDiffKeyFragment : Fragment()

    class TestFragment : Fragment()

    class PagingTestFragment : Fragment() {

        lateinit var pagingDataAdapter: AssemblyPagingDataFragmentStateAdapter<Any>

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
        ): View = ViewPager2(requireContext())

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            pagingDataAdapter = AssemblyPagingDataFragmentStateAdapter(
                this, listOf(TextFragmentItemFactory(), ImageFragmentItemFactory())
            )

            (view as ViewPager2).apply {
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
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()

        assertThrow(IllegalArgumentException::class) {
            AssemblyPagingDataFragmentStateAdapter<Any>(
                fragment, listOf(),
            )
        }
        assertThrow(IllegalArgumentException::class) {
            AssemblyPagingDataFragmentStateAdapter<Any>(
                fragment.requireActivity(), listOf(),
            )
        }
        assertThrow(IllegalArgumentException::class) {
            AssemblyPagingDataFragmentStateAdapter<Any>(
                fragment.childFragmentManager, fragment.lifecycle, listOf(),
            )
        }

        assertNoThrow {
            AssemblyPagingDataFragmentStateAdapter<Any>(
                fragment, listOf(TextFragmentItemFactory(), ImageFragmentItemFactory())
            )
        }
        assertNoThrow {
            AssemblyPagingDataFragmentStateAdapter<Any>(
                fragment.requireActivity(),
                listOf(TextFragmentItemFactory(), ImageFragmentItemFactory())
            )
        }
        assertNoThrow {
            AssemblyPagingDataFragmentStateAdapter<Any>(
                fragment.childFragmentManager,
                fragment.lifecycle,
                listOf(TextFragmentItemFactory(), ImageFragmentItemFactory())
            )
        }

        assertThrow(IllegalArgumentException::class) {
            AssemblyPagingDataFragmentStateAdapter<Any>(
                fragment, listOf(NoDiffKeyItemFactory(), TextFragmentItemFactory()),
            )
        }
        assertThrow(IllegalArgumentException::class) {
            AssemblyPagingDataFragmentStateAdapter<Any>(
                fragment.requireActivity(),
                listOf(NoDiffKeyItemFactory(), TextFragmentItemFactory()),
            )
        }
        assertThrow(IllegalArgumentException::class) {
            AssemblyPagingDataFragmentStateAdapter<Any>(
                fragment.childFragmentManager,
                fragment.lifecycle,
                listOf(NoDiffKeyItemFactory(), TextFragmentItemFactory()),
            )
        }
        assertThrow(IllegalArgumentException::class) {
            AssemblyPagingDataFragmentStateAdapter<Any>(
                fragment, listOf(NoDiffKeyItemFactory(), NoDiffKeyItemFactory()),
            )
        }
        assertThrow(IllegalArgumentException::class) {
            AssemblyPagingDataFragmentStateAdapter<Any>(
                fragment.requireActivity(), listOf(NoDiffKeyItemFactory(), NoDiffKeyItemFactory()),
            )
        }
        assertThrow(IllegalArgumentException::class) {
            AssemblyPagingDataFragmentStateAdapter<Any>(
                fragment.childFragmentManager,
                fragment.lifecycle,
                listOf(NoDiffKeyItemFactory(), NoDiffKeyItemFactory()),
            )
        }
        assertThrow(IllegalArgumentException::class) {
            AssemblyPagingDataFragmentStateAdapter<Any>(
                fragment, listOf(NoDiffKeyItemFactory()),
            )
        }
        assertThrow(IllegalArgumentException::class) {
            AssemblyPagingDataFragmentStateAdapter<Any>(
                fragment.requireActivity(), listOf(NoDiffKeyItemFactory()),
            )
        }
        assertThrow(IllegalArgumentException::class) {
            AssemblyPagingDataFragmentStateAdapter<Any>(
                fragment.childFragmentManager, fragment.lifecycle, listOf(NoDiffKeyItemFactory()),
            )
        }
    }

    @Test
    fun testPropertyCurrentListAndSubmitList() {
        val fragmentScenario = PagingTestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        val pagingDataAdapter: AssemblyPagingDataFragmentStateAdapter<Any> =
            fragment.pagingDataAdapter
        pagingDataAdapter.apply {
            Assert.assertEquals("", currentList.joinToString())

            fragment.submitList(listOf(Text("hello")))
            Thread.sleep(50)
            Assert.assertEquals("Text(text=hello)", currentList.joinToString())

            fragment.submitList(listOf(Text("hello"), Text("world")))
            Thread.sleep(50)
            Assert.assertEquals("Text(text=hello), Text(text=world)", currentList.joinToString())

            fragment.submitList(null)
            Thread.sleep(50)
            Assert.assertEquals("", currentList.joinToString())
        }
    }

    @Test
    fun testMethodGetCount() {
        val fragmentScenario = PagingTestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        val pagingDataAdapter: AssemblyPagingDataFragmentStateAdapter<Any> =
            fragment.pagingDataAdapter
        pagingDataAdapter.apply {
            Assert.assertEquals(0, itemCount)

            fragment.submitList(listOf(Text("hello")))
            Thread.sleep(50)
            Assert.assertEquals(1, itemCount)

            fragment.submitList(listOf(Text("hello"), Text("world")))
            Thread.sleep(50)
            Assert.assertEquals(2, itemCount)

            fragment.submitList(null)
            Thread.sleep(50)
            Assert.assertEquals(0, itemCount)
        }
    }

    @Test
    fun testMethodGetItemData() {
        val fragmentScenario = PagingTestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        val pagingDataAdapter: AssemblyPagingDataFragmentStateAdapter<Any> =
            fragment.pagingDataAdapter
        pagingDataAdapter.apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItemData(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemData(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemData(1)
            }

            fragment.submitList(listOf(Text("hello"), Text("world")))
            Thread.sleep(50)
            Assert.assertEquals(Text("hello"), getItemData(0))
            Assert.assertEquals(Text("world"), getItemData(1))
        }
    }

    @Test
    fun testMethodGetItemId() {
        val fragmentScenario = PagingTestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()

        AssemblyPagingDataFragmentStateAdapter<Text>(
            fragment,
            listOf(TextFragmentItemFactory())
        ).apply {
            assertThrow(UnsupportedOperationException::class) {
                setHasStableIds(true)
            }
            Assert.assertEquals(-1L, getItemId(-1))
            Assert.assertEquals(0L, getItemId(0))
            Assert.assertEquals(1L, getItemId(1))
        }

        val pagingDataAdapter: AssemblyPagingDataFragmentStateAdapter<Any> =
            fragment.pagingDataAdapter
        pagingDataAdapter.apply {
            assertThrow(UnsupportedOperationException::class) {
                setHasStableIds(true)
            }
            Assert.assertEquals(-1L, getItemId(-1))
            Assert.assertEquals(0L, getItemId(0))
            Assert.assertEquals(1L, getItemId(1))

            fragment.submitList(listOf(Text("hello"), Text("world")))
            Thread.sleep(50)
            Assert.assertEquals(-1L, getItemId(-1))
            Assert.assertEquals(0L, getItemId(0))
            Assert.assertEquals(1L, getItemId(1))
            Assert.assertEquals(2L, getItemId(2))
        }
    }

    @Test
    fun testMethodGetItemViewType() {
        val fragmentScenario = PagingTestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        val pagingDataAdapter: AssemblyPagingDataFragmentStateAdapter<Any> =
            fragment.pagingDataAdapter
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

            fragment.submitList(listOf(Image(android.R.drawable.alert_dark_frame), Text("hello")))
            Thread.sleep(50)
            Assert.assertEquals(1, getItemViewType(0))
            Assert.assertEquals(0, getItemViewType(1))
        }
    }

    @Test
    fun testMethodCreateFragment() {
        val fragmentScenario = PagingTestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        val pagingDataAdapter: AssemblyPagingDataFragmentStateAdapter<Any> =
            fragment.pagingDataAdapter
        pagingDataAdapter.apply {
            fragment.submitList(listOf(Text("hello"), Image(android.R.drawable.alert_dark_frame)))
            Thread.sleep(50)

            Assert.assertTrue(createFragment(0) is TextFragment)
            Assert.assertTrue(createFragment(1) is ImageFragment)
        }
    }

    @Test
    fun testMethodGetItemFactoryByPosition() {
        val fragmentScenario = PagingTestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        val pagingDataAdapter: AssemblyPagingDataFragmentStateAdapter<Any> =
            fragment.pagingDataAdapter
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

            fragment.submitList(listOf(Image(android.R.drawable.alert_dark_frame), Text("hello")))
            Thread.sleep(50)
            Assert.assertEquals(ImageFragmentItemFactory::class, getItemFactoryByPosition(0)::class)
            Assert.assertEquals(TextFragmentItemFactory::class, getItemFactoryByPosition(1)::class)
        }
    }

    @Test
    fun testMethodGetItemFactoryByData() {
        val fragmentScenario = PagingTestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        val textItemFactory = TextFragmentItemFactory()
        val imageItemFactory = ImageFragmentItemFactory()

        AssemblyPagingDataFragmentStateAdapter<Any>(fragment, listOf(textItemFactory, imageItemFactory)).apply {
            Assert.assertSame(
                imageItemFactory,
                getItemFactoryByData(Image(android.R.drawable.alert_dark_frame))
            )
            Assert.assertSame(textItemFactory, getItemFactoryByData(Text("hello")))
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                getItemFactoryByData(Date())
            }
        }
    }

    @Test
    fun testMethodGetItemFactoryByClass() {
        val fragmentScenario = PagingTestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        val textItemFactory = TextFragmentItemFactory()
        val imageItemFactory = ImageFragmentItemFactory()

        AssemblyPagingDataFragmentStateAdapter<Any>(fragment, listOf(textItemFactory, imageItemFactory)).apply {
            Assert.assertSame(
                imageItemFactory,
                getItemFactoryByClass(ImageFragmentItemFactory::class.java)
            )
            Assert.assertSame(
                textItemFactory,
                getItemFactoryByClass(TextFragmentItemFactory::class.java)
            )
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                getItemFactoryByClass(ViewFragmentItemFactory::class.java)
            }
        }
    }
}