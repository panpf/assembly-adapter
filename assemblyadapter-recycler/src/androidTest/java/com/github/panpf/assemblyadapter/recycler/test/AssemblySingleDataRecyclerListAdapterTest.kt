package com.github.panpf.assemblyadapter.recycler.test

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.ViewItemFactory
import com.github.panpf.assemblyadapter.recycler.AssemblySingleDataRecyclerListAdapter
import com.github.panpf.assemblyadapter.recycler.InstanceDiffItemCallback
import com.github.panpf.assemblyadapter.recycler.SimpleAdapterDataObserver
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class AssemblySingleDataRecyclerListAdapterTest {

    private class TestItemFactory :
        ViewItemFactory<String>(String::class, android.R.layout.activity_list_item)

    @Test
    fun testConstructor() {
        AssemblySingleDataRecyclerListAdapter(TestItemFactory(), InstanceDiffItemCallback()).apply {
            Assert.assertNull(data)
        }

        AssemblySingleDataRecyclerListAdapter(
            TestItemFactory(),
            "123456",
            InstanceDiffItemCallback()
        ).apply {
            Assert.assertNotNull(data)
            Assert.assertEquals("123456", data)
        }
    }

    @Test
    fun testPropertyData() {
        var dataFromObserver: String? = null
        AssemblySingleDataRecyclerListAdapter(TestItemFactory(), InstanceDiffItemCallback()).apply {
            registerAdapterDataObserver(SimpleAdapterDataObserver {
                dataFromObserver = data
            })

            Assert.assertNull(data)
            Assert.assertNull(dataFromObserver)

            data = "Test data changed notify invoke"
            Thread.sleep(100)
            Assert.assertEquals("Test data changed notify invoke", data)
            Assert.assertEquals("Test data changed notify invoke", dataFromObserver)

            data = "Test data changed notify invoke2"
            Thread.sleep(100)
            Assert.assertEquals("Test data changed notify invoke2", data)
            Assert.assertEquals("Test data changed notify invoke2", dataFromObserver)
        }
    }

    @Test
    fun testMethodGetCount() {
        AssemblySingleDataRecyclerListAdapter(TestItemFactory(), InstanceDiffItemCallback()).apply {
            Assert.assertEquals(0, itemCount)

            data = "Test count"
            Thread.sleep(100)
            Assert.assertEquals(1, itemCount)

            data = null
            Thread.sleep(100)
            Assert.assertEquals(0, itemCount)
        }
    }

    @Test
    fun testMethodGetItemId() {
        AssemblySingleDataRecyclerListAdapter(TestItemFactory(), InstanceDiffItemCallback()).apply {
            Assert.assertEquals(-1L, getItemId(-1))
            Assert.assertEquals(0L, getItemId(0))
            Assert.assertEquals(1L, getItemId(1))
            Assert.assertEquals(Int.MAX_VALUE.toLong(), getItemId(Int.MAX_VALUE))
            Assert.assertEquals(Int.MIN_VALUE.toLong(), getItemId(Int.MIN_VALUE))
        }
    }

    @Test
    fun testMethodGetItemViewType() {
        AssemblySingleDataRecyclerListAdapter(TestItemFactory(), InstanceDiffItemCallback()).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItemViewType(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemViewType(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemViewType(1)
            }

            data = "test"
            Thread.sleep(100)
            Assert.assertEquals(0, getItemViewType(0))
        }
    }

    @Test
    fun testMethodCreateAndBindViewHolder() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = RecyclerView(context).apply {
            layoutManager = LinearLayoutManager(context)
        }
        AssemblySingleDataRecyclerListAdapter(TestItemFactory(), InstanceDiffItemCallback()).apply {
            val viewHolder = createViewHolder(parent, 0)

            assertThrow(IndexOutOfBoundsException::class) {
                bindViewHolder(viewHolder, -1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                bindViewHolder(viewHolder, 0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                bindViewHolder(viewHolder, 1)
            }

            data = "test"
            Thread.sleep(100)
            bindViewHolder(viewHolder, 0)
        }
    }

    @Test
    fun testMethodGetItemFactoryByPosition() {
        val itemFactory = TestItemFactory()
        AssemblySingleDataRecyclerListAdapter(itemFactory, InstanceDiffItemCallback()).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByPosition(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByPosition(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByPosition(1)
            }

            data = "test"
            Thread.sleep(100)
            Assert.assertSame(itemFactory, getItemFactoryByPosition(0))
        }
    }
}