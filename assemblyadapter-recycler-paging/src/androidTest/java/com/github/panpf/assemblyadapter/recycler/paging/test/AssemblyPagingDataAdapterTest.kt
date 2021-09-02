package com.github.panpf.assemblyadapter.recycler.paging.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.panpf.assemblyadapter.ViewItemFactory
import com.github.panpf.assemblyadapter.recycler.*
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

//    class PagingTestFragment : Fragment() {
//
//        val pagingDataAdapter = AssemblyPagingDataAdapter<Any>(
//            listOf(TextItemFactory(), ImageItemFactory())
//        )
//
//        private var dataList: List<Any>? = null
//
//        private val pagingFlow = Pager(
//            PagingConfig(pageSize = 10, enablePlaceholders = false),
//            initialKey = 0,
//            pagingSourceFactory = { TestPagingSource(dataList) }
//        ).flow
//
//        class TestPagingSource(private val dataList: List<Any>?) :
//            PagingSource<Int, Any>() {
//            override fun getRefreshKey(state: PagingState<Int, Any>): Int = 0
//
//            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Any> {
//                return LoadResult.Page(
//                    dataList ?: emptyList(),
//                    prevKey = null,
//                    nextKey = null
//                )
//            }
//        }
//
//        override fun onCreateView(
//            inflater: LayoutInflater,
//            container: ViewGroup?,
//            savedInstanceState: Bundle?
//        ): View = RecyclerView(requireContext())
//
//        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//            super.onViewCreated(view, savedInstanceState)
//            (view as RecyclerView).apply {
//                layoutManager = LinearLayoutManager(requireContext())
//                adapter = pagingDataAdapter
//
//                lifecycleScope.launch {
//                    pagingFlow.collect {
//                        pagingDataAdapter.submitData(it)
//                    }
//                }
//            }
//        }
//
//        fun submitList(list: List<Any>?) {
//            dataList = list
//            pagingDataAdapter.refresh()
//        }
//    }

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


//    @Test
//    fun testPropertyCurrentListAndSubmitList() {
//        val fragmentScenario = PagingTestFragment::class.launchFragmentInContainer()
//        val fragment = fragmentScenario.getFragmentSync()
//        val pagingDataAdapter: AssemblyPagingDataAdapter<Any> = fragment.pagingDataAdapter
//        pagingDataAdapter.apply {
//            Assert.assertEquals("", currentList.joinToString())
//
//            fragment.submitList(listOf(Text("hello")))
//            Thread.sleep(100)
//            Assert.assertEquals("Text(text=hello)", currentList.joinToString())
//
//            fragment.submitList(listOf(Text("hello"), Text("world")))
//            Assert.assertEquals("Text(text=hello), Text(text=world)", currentList.joinToString())
//
//            fragment.submitList(null)
//            Thread.sleep(100)
//            Assert.assertEquals("", currentList.joinToString())
//        }
//    }
//
//    @Test
//    fun testMethodGetItemId() {
//        AssemblyPagingDataAdapter<Text>(listOf(TextItemFactory())).apply {
//            Assert.assertEquals(-1L, getItemId(-1))
//            Assert.assertEquals(-1L, getItemId(0))
//            Assert.assertEquals(-1L, getItemId(1))
//        }
//
//        AssemblyPagingDataAdapter<Text>(listOf(TextItemFactory())).apply {
//            assertThrow(UnsupportedOperationException::class) {
//                setHasStableIds(true)
//            }
//            Assert.assertEquals(-1L, getItemId(-1))
//            Assert.assertEquals(-1L, getItemId(0))
//            Assert.assertEquals(-1L, getItemId(1))
//        }
//
//        AssemblyPagingDataAdapter<Text>(listOf(TextItemFactory())).apply {
//            // listOf("hello", "world")
//            assertThrow(UnsupportedOperationException::class) {
//                setHasStableIds(true)
//            }
//            Assert.assertEquals(-1L, getItemId(-1))
//            Assert.assertEquals(-1L, getItemId(0))
//            Assert.assertEquals(-1L, getItemId(1))
//            Assert.assertEquals(-1L, getItemId(2))
//        }
//    }
}