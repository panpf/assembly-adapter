package com.github.panpf.assemblyadapter.pager.test

import android.database.DataSetObserver
import androidx.fragment.app.Fragment
import com.github.panpf.assemblyadapter.pager.AssemblySingleDataFragmentStatePagerAdapter
import com.github.panpf.assemblyadapter.pager.ViewFragmentItemFactory
import com.github.panpf.tools4a.test.ktx.launchFragmentInContainerWithOn
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class AssemblySingleDataFragmentStatePagerAdapterTest {

    private class TestItemFactory :
        ViewFragmentItemFactory<String>(String::class, android.R.layout.activity_list_item)

    class TestFragment : Fragment()

    @Test
    fun testConstructor() {
        TestFragment::class.launchFragmentInContainerWithOn { fragment ->
            val fragmentManager = fragment.childFragmentManager

            AssemblySingleDataFragmentStatePagerAdapter(fragmentManager, TestItemFactory()).apply {
                Assert.assertNull(data)
            }

            AssemblySingleDataFragmentStatePagerAdapter(
                fragmentManager,
                TestItemFactory(),
                "123456"
            ).apply {
                Assert.assertNotNull(data)
                Assert.assertEquals("123456", data)
            }
        }
    }

    @Test
    fun testPropertyData() {
        TestFragment::class.launchFragmentInContainerWithOn { fragment ->
            val fragmentManager = fragment.childFragmentManager

            var dataFromObserver: String? = null
            AssemblySingleDataFragmentStatePagerAdapter(fragmentManager, TestItemFactory()).apply {
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
    }

    @Test
    fun testMethodGetCount() {
        TestFragment::class.launchFragmentInContainerWithOn { fragment ->
            val fragmentManager = fragment.childFragmentManager

            AssemblySingleDataFragmentStatePagerAdapter(fragmentManager, TestItemFactory()).apply {
                Assert.assertEquals(0, count)

                data = "Test count"
                Assert.assertEquals(1, count)

                data = null
                Assert.assertEquals(0, count)
            }
        }
    }

    @Test
    fun testMethodGetItem() {
        TestFragment::class.launchFragmentInContainerWithOn { fragment ->
            val fragmentManager = fragment.childFragmentManager

            AssemblySingleDataFragmentStatePagerAdapter(fragmentManager, TestItemFactory()).apply {
                assertThrow(IndexOutOfBoundsException::class) {
                    getItem(-1)
                }
                assertThrow(IndexOutOfBoundsException::class) {
                    getItem(0)
                }
                assertThrow(IndexOutOfBoundsException::class) {
                    getItem(1)
                }

                data = "test"
                getItem(0)
            }
        }
    }

    @Test
    fun testMethodGetItemFactoryByPosition() {
        TestFragment::class.launchFragmentInContainerWithOn { fragment ->
            val fragmentManager = fragment.childFragmentManager

            val itemFactory = TestItemFactory()
            AssemblySingleDataFragmentStatePagerAdapter(fragmentManager, itemFactory).apply {
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
}