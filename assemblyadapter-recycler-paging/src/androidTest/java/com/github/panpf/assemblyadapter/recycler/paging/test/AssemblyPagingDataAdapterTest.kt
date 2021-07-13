package com.github.panpf.assemblyadapter.recycler.paging.test

import androidx.test.runner.AndroidJUnit4
import com.github.panpf.assemblyadapter.ViewItemFactory
import com.github.panpf.assemblyadapter.diff.DiffKey
import com.github.panpf.assemblyadapter.recycler.paging.AssemblyPagingDataAdapter
import com.github.panpf.tools4j.test.ktx.assertNoThrow
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AssemblyPagingDataAdapterTest {

    data class HaveKeyData(val name: String) : DiffKey {
        override val diffKey: String = name
    }

    data class NoKeyData(val time: Long)

    private val haveKeyItemFactory =
        ViewItemFactory(HaveKeyData::class, android.R.layout.activity_list_item)
    private val noKeyItemFactory =
        ViewItemFactory(NoKeyData::class, android.R.layout.activity_list_item)

    @Test
    fun testDataClassDiffKey() {
        assertNoThrow {
            AssemblyPagingDataAdapter<Any>(listOf(haveKeyItemFactory))
        }
        assertNoThrow {
            AssemblyPagingDataAdapter<Any>(listOf(haveKeyItemFactory, haveKeyItemFactory))
        }

        assertThrow(IllegalArgumentException::class) {
            AssemblyPagingDataAdapter<Any>(listOf(haveKeyItemFactory, noKeyItemFactory))
        }
        assertThrow(IllegalArgumentException::class) {
            AssemblyPagingDataAdapter<Any>(listOf(noKeyItemFactory, haveKeyItemFactory))
        }
        assertThrow(IllegalArgumentException::class) {
            AssemblyPagingDataAdapter<Any>(listOf(noKeyItemFactory, noKeyItemFactory))
        }
        assertThrow(IllegalArgumentException::class) {
            AssemblyPagingDataAdapter<Any>(listOf(noKeyItemFactory))
        }
    }
}