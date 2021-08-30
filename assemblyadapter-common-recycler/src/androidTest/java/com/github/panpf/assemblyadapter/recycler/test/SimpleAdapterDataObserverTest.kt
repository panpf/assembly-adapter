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
package com.github.panpf.assemblyadapter.recycler.test

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.recycler.InstanceDiffItemCallback
import com.github.panpf.assemblyadapter.recycler.SimpleAdapterDataObserver
import org.junit.Assert
import org.junit.Test

class SimpleAdapterDataObserverTest {

    @Test
    fun test() {
        val changeTypeList = ArrayList<SimpleAdapterDataObserver.Type>()
        val simpleAdapterDataObserver = SimpleAdapterDataObserver {
            changeTypeList.add(it)
        }

        val adapter = TestAdapter().apply {
            registerAdapterDataObserver(simpleAdapterDataObserver)
        }

        Assert.assertEquals("", changeTypeList.joinToString { it.toString() })

        adapter.submitList(listOf(1, 3, 7, 8, 0, 9, 5))
        Assert.assertEquals(
            "Changed",
            changeTypeList.joinToString { it::class.java.simpleName }
        )
        changeTypeList.clear()


        val listAdapter = TestListAdapter().apply {
            registerAdapterDataObserver(simpleAdapterDataObserver)
        }

        listAdapter.submitList(listOf(1, 2, 3))
        Thread.sleep(100)
        Assert.assertEquals(
            "RangeInserted(positionStart=0, itemCount=3)",
            changeTypeList.joinToString { it.toString() }
        )
        changeTypeList.clear()

        listAdapter.submitList(listOf(1, 2, 3, 4, 6, 7, 8))
        Thread.sleep(100)
        Assert.assertEquals(
            "RangeInserted(positionStart=3, itemCount=4)",
            changeTypeList.joinToString { it.toString() }
        )
        changeTypeList.clear()

        listAdapter.submitList(listOf(1, 2, 0, 9, 5, 7, 8))
        Thread.sleep(100)
        Assert.assertEquals(
            "RangeRemoved(positionStart=2, itemCount=3), RangeInserted(positionStart=2, itemCount=3)",
            changeTypeList.joinToString { it.toString() }
        )
        changeTypeList.clear()

        listAdapter.submitList(listOf(1, 2, 7, 8, 0, 9, 5))
        Thread.sleep(100)
        Assert.assertEquals(
            "RangeMoved(fromPosition=6, toPosition=2, itemCount=1), RangeMoved(fromPosition=6, toPosition=2, itemCount=1)",
            changeTypeList.joinToString { it.toString() }
        )
        changeTypeList.clear()

        listAdapter.submitList(listOf(1, 3, 7, 8, 0, 9, 5))
        Thread.sleep(100)
        Assert.assertEquals(
            "RangeRemoved(positionStart=1, itemCount=1), RangeInserted(positionStart=1, itemCount=1)",
            changeTypeList.joinToString { it.toString() }
        )
        changeTypeList.clear()
    }

    private class TestListAdapter :
        ListAdapter<Any, RecyclerView.ViewHolder>(InstanceDiffItemCallback()) {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RecyclerView.ViewHolder {
            return object : RecyclerView.ViewHolder(TextView(parent.context)) {}
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        }
    }

    private class TestAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var list: List<Any>? = null

        fun submitList(list: List<Any>?) {
            this.list = list
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return list?.size ?: 0
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RecyclerView.ViewHolder {
            return object : RecyclerView.ViewHolder(TextView(parent.context)) {}
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        }
    }
}