package com.github.panpf.assemblyadapter.pager.test

import android.database.DataSetObserver
import android.view.View
import android.widget.FrameLayout
import androidx.test.platform.app.InstrumentationRegistry
import androidx.viewpager.widget.PagerAdapter
import com.github.panpf.assemblyadapter.pager.AssemblySingleDataPagerAdapter
import com.github.panpf.assemblyadapter.pager.ViewPagerItemFactory
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class AssemblySingleDataPagerAdapterTest {

    private class TestItemFactory :
        ViewPagerItemFactory<String>(String::class, android.R.layout.activity_list_item)

    @Test
    fun testConstructor() {
        AssemblySingleDataPagerAdapter(TestItemFactory()).apply {
            Assert.assertNull(data)
        }

        AssemblySingleDataPagerAdapter(
            TestItemFactory(),
            "123456"
        ).apply {
            Assert.assertNotNull(data)
            Assert.assertEquals("123456", data)
        }
    }

    @Test
    fun testPropertyData() {
        var dataFromObserver: String? = null
        AssemblySingleDataPagerAdapter(TestItemFactory()).apply {
            registerDataSetObserver(object : DataSetObserver() {
                override fun onChanged() {
                    super.onChanged()
                    dataFromObserver = data
                }
            })

            Assert.assertNull(data)
            Assert.assertNull(dataFromObserver)

            data = "Test data changed notify invoke"
            Assert.assertEquals("Test data changed notify invoke", data)
            Assert.assertEquals("Test data changed notify invoke", dataFromObserver)

            data = "Test data changed notify invoke2"
            Assert.assertEquals("Test data changed notify invoke2", data)
            Assert.assertEquals("Test data changed notify invoke2", dataFromObserver)
        }
    }

    @Test
    fun testMethodGetCount() {
        AssemblySingleDataPagerAdapter(TestItemFactory()).apply {
            Assert.assertEquals(0, count)

            data = "Test count"
            Assert.assertEquals(1, count)

            data = null
            Assert.assertEquals(0, count)
        }
    }

    @Test
    fun testMethodInstantiateItem() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)
        AssemblySingleDataPagerAdapter(TestItemFactory()).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                instantiateItem(parent, -1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                instantiateItem(parent, 0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                instantiateItem(parent, 1)
            }

            data = "test"
            Assert.assertTrue(instantiateItem(parent, 0) is View)
        }
    }

    @Test
    fun testMethodDestroyItem() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)
        AssemblySingleDataPagerAdapter(TestItemFactory()).apply {
            data = "test"
            val item = instantiateItem(parent, 0)
            Assert.assertTrue(item is View)

            assertThrow(IndexOutOfBoundsException::class) {
                destroyItem(parent, -1, item)
            }
            destroyItem(parent, 0, item)
            assertThrow(IndexOutOfBoundsException::class) {
                destroyItem(parent, 1, item)
            }
            assertThrow(ClassCastException::class) {
                destroyItem(parent, 0, "fake item")
            }
        }
    }

    @Test
    fun testMethodIsViewFromObject() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)
        AssemblySingleDataPagerAdapter(TestItemFactory()).apply {
            data = "test"
            val item = instantiateItem(parent, 0)
            Assert.assertTrue(item is View)
            Assert.assertTrue(isViewFromObject(item as View, item))
            Assert.assertFalse(isViewFromObject(item, "fake item"))
        }
    }

    @Test
    fun testMethodGetItemPosition() {
//        val context = InstrumentationRegistry.getInstrumentation().context
//        val parent = FrameLayout(context)
//        AssemblySingleDataPagerAdapter(TestItemFactory()).apply {
//            data = "test"
//            val item = instantiateItem(parent, 0)
//            Assert.assertEquals(PagerAdapter.POSITION_NONE, getItemPosition(item))
//            notifyDataSetChanged()
//        }

        // todo 补充测试
    }

    @Test
    fun testMethodGetItemFactoryByPosition() {
        val itemFactory = TestItemFactory()
        AssemblySingleDataPagerAdapter(itemFactory).apply {
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
            Assert.assertSame(itemFactory, getItemFactoryByPosition(0))
        }
    }
}