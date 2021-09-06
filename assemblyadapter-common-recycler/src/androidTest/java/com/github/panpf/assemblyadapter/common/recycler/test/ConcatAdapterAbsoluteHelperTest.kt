package com.github.panpf.assemblyadapter.common.recycler.test

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.recycler.ConcatAdapterAbsoluteHelper
import com.github.panpf.assemblyadapter.recycler.InstanceDiffItemCallback
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class ConcatAdapterAbsoluteHelperTest {

    @Test
    fun test() {
        val headerAdapter = TestListAdapter().apply {
            submitList(listOf(1, 2, 3))
        }
        val body1Adapter = TestListAdapter().apply {
            submitList(listOf(4, 5, 6))
        }
        val body2Adapter = TestListAdapter().apply {
            submitList(listOf(7, 8, 9))
        }
        val footerAdapter = TestListAdapter().apply {
            submitList(listOf(10, 11, 12))
        }
        val concatBodyAdapter = ConcatAdapter(body1Adapter, body2Adapter)
        val concatAdapter = ConcatAdapter(headerAdapter, concatBodyAdapter, footerAdapter)

        val absoluteHelper = ConcatAdapterAbsoluteHelper()

        headerAdapter.apply {
            Assert.assertEquals(
                0, absoluteHelper.findAbsoluteAdapterPosition(this, this, 0)
            )
            Assert.assertEquals(
                1, absoluteHelper.findAbsoluteAdapterPosition(this, this, 1)
            )
            Assert.assertEquals(
                2, absoluteHelper.findAbsoluteAdapterPosition(this, this, 2)
            )
            assertThrow(IndexOutOfBoundsException::class) {
                absoluteHelper.findAbsoluteAdapterPosition(this, this, -1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                absoluteHelper.findAbsoluteAdapterPosition(this, this, 3)
            }
            Assert.assertEquals(
                0, absoluteHelper.findAbsoluteAdapterPosition(concatAdapter, this, 0)
            )
            Assert.assertEquals(
                1, absoluteHelper.findAbsoluteAdapterPosition(concatAdapter, this, 1)
            )
            Assert.assertEquals(
                2, absoluteHelper.findAbsoluteAdapterPosition(concatAdapter, this, 2)
            )
            assertThrow(IndexOutOfBoundsException::class) {
                absoluteHelper.findAbsoluteAdapterPosition(concatAdapter, this, -1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                absoluteHelper.findAbsoluteAdapterPosition(concatAdapter, this, 3)
            }
        }

        body1Adapter.apply {
            Assert.assertEquals(
                0, absoluteHelper.findAbsoluteAdapterPosition(this, this, 0)
            )
            Assert.assertEquals(
                1, absoluteHelper.findAbsoluteAdapterPosition(this, this, 1)
            )
            Assert.assertEquals(
                2, absoluteHelper.findAbsoluteAdapterPosition(this, this, 2)
            )
            assertThrow(IndexOutOfBoundsException::class) {
                absoluteHelper.findAbsoluteAdapterPosition(this, this, -1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                absoluteHelper.findAbsoluteAdapterPosition(this, this, 3)
            }
            Assert.assertEquals(
                0, absoluteHelper.findAbsoluteAdapterPosition(concatBodyAdapter, this, 0)
            )
            Assert.assertEquals(
                1, absoluteHelper.findAbsoluteAdapterPosition(concatBodyAdapter, this, 1)
            )
            Assert.assertEquals(
                2, absoluteHelper.findAbsoluteAdapterPosition(concatBodyAdapter, this, 2)
            )
            assertThrow(IndexOutOfBoundsException::class) {
                absoluteHelper.findAbsoluteAdapterPosition(concatBodyAdapter, this, -1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                absoluteHelper.findAbsoluteAdapterPosition(concatBodyAdapter, this, 3)
            }
            Assert.assertEquals(
                3, absoluteHelper.findAbsoluteAdapterPosition(concatAdapter, this, 0)
            )
            Assert.assertEquals(
                4, absoluteHelper.findAbsoluteAdapterPosition(concatAdapter, this, 1)
            )
            Assert.assertEquals(
                5, absoluteHelper.findAbsoluteAdapterPosition(concatAdapter, this, 2)
            )
            assertThrow(IndexOutOfBoundsException::class) {
                absoluteHelper.findAbsoluteAdapterPosition(concatAdapter, this, -1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                absoluteHelper.findAbsoluteAdapterPosition(concatAdapter, this, 3)
            }
        }

        body2Adapter.apply {
            Assert.assertEquals(
                0, absoluteHelper.findAbsoluteAdapterPosition(this, this, 0)
            )
            Assert.assertEquals(
                1, absoluteHelper.findAbsoluteAdapterPosition(this, this, 1)
            )
            Assert.assertEquals(
                2, absoluteHelper.findAbsoluteAdapterPosition(this, this, 2)
            )
            assertThrow(IndexOutOfBoundsException::class) {
                absoluteHelper.findAbsoluteAdapterPosition(this, this, -1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                absoluteHelper.findAbsoluteAdapterPosition(this, this, 3)
            }
            Assert.assertEquals(
                3, absoluteHelper.findAbsoluteAdapterPosition(concatBodyAdapter, this, 0)
            )
            Assert.assertEquals(
                4, absoluteHelper.findAbsoluteAdapterPosition(concatBodyAdapter, this, 1)
            )
            Assert.assertEquals(
                5, absoluteHelper.findAbsoluteAdapterPosition(concatBodyAdapter, this, 2)
            )
            assertThrow(IndexOutOfBoundsException::class) {
                absoluteHelper.findAbsoluteAdapterPosition(concatBodyAdapter, this, -1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                absoluteHelper.findAbsoluteAdapterPosition(concatBodyAdapter, this, 3)
            }
            Assert.assertEquals(
                6, absoluteHelper.findAbsoluteAdapterPosition(concatAdapter, this, 0)
            )
            Assert.assertEquals(
                7, absoluteHelper.findAbsoluteAdapterPosition(concatAdapter, this, 1)
            )
            Assert.assertEquals(
                8, absoluteHelper.findAbsoluteAdapterPosition(concatAdapter, this, 2)
            )
            assertThrow(IndexOutOfBoundsException::class) {
                absoluteHelper.findAbsoluteAdapterPosition(concatAdapter, this, -1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                absoluteHelper.findAbsoluteAdapterPosition(concatAdapter, this, 3)
            }
        }

        footerAdapter.apply {
            Assert.assertEquals(
                0, absoluteHelper.findAbsoluteAdapterPosition(this, this, 0)
            )
            Assert.assertEquals(
                1, absoluteHelper.findAbsoluteAdapterPosition(this, this, 1)
            )
            Assert.assertEquals(
                2, absoluteHelper.findAbsoluteAdapterPosition(this, this, 2)
            )
            assertThrow(IndexOutOfBoundsException::class) {
                absoluteHelper.findAbsoluteAdapterPosition(this, this, -1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                absoluteHelper.findAbsoluteAdapterPosition(this, this, 3)
            }
            Assert.assertEquals(
                9, absoluteHelper.findAbsoluteAdapterPosition(concatAdapter, this, 0)
            )
            Assert.assertEquals(
                10, absoluteHelper.findAbsoluteAdapterPosition(concatAdapter, this, 1)
            )
            Assert.assertEquals(
                11, absoluteHelper.findAbsoluteAdapterPosition(concatAdapter, this, 2)
            )
            assertThrow(IndexOutOfBoundsException::class) {
                absoluteHelper.findAbsoluteAdapterPosition(concatAdapter, this, -1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                absoluteHelper.findAbsoluteAdapterPosition(concatAdapter, this, 3)
            }
        }

        assertThrow(IllegalArgumentException::class) {
            absoluteHelper.findAbsoluteAdapterPosition(headerAdapter, body1Adapter, 0)
        }
    }

    private class TestListAdapter :
        ListAdapter<Any, RecyclerView.ViewHolder>(InstanceDiffItemCallback()) {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RecyclerView.ViewHolder {
            return object: RecyclerView.ViewHolder(TextView(parent.context)){}
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        }
    }
}