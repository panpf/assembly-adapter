package com.github.panpf.assemblyadapter.pager2.paging.test

import android.R
import androidx.fragment.app.FragmentActivity
import androidx.test.runner.AndroidJUnit4
import com.github.panpf.assemblyadapter.diff.DiffKey
import com.github.panpf.assemblyadapter.pager.ViewFragmentItemFactory
import com.github.panpf.assemblyadapter.pager2.paging.AssemblyPagingDataFragmentStateAdapter
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