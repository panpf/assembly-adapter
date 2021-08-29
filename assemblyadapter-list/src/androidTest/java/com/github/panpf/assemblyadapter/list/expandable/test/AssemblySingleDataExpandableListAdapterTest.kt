package com.github.panpf.assemblyadapter.list.expandable.test

import android.database.DataSetObserver
import android.widget.FrameLayout
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.list.expandable.AssemblySingleDataExpandableListAdapter
import com.github.panpf.assemblyadapter.list.expandable.test.internal.Strings
import com.github.panpf.assemblyadapter.list.expandable.test.internal.StringsChildItemFactory
import com.github.panpf.assemblyadapter.list.expandable.test.internal.StringsGroupItemFactory
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class AssemblySingleDataExpandableListAdapterTest {

    @Test
    fun testConstructor() {
        AssemblySingleDataExpandableListAdapter<Strings, String>(
            listOf(
                StringsGroupItemFactory(),
                StringsChildItemFactory()
            )
        ).apply {
            Assert.assertNull(data)
        }

        AssemblySingleDataExpandableListAdapter<Strings, String>(
            listOf(
                StringsGroupItemFactory(),
                StringsChildItemFactory()
            ),
            Strings("123456")
        ).apply {
            Assert.assertNotNull(data)
            Assert.assertEquals(Strings("123456"), data)
        }
    }

    @Test
    fun testPropertyData() {
        var dataFromObserver: Strings? = null
        AssemblySingleDataExpandableListAdapter<Strings, String>(
            listOf(
                StringsGroupItemFactory(),
                StringsChildItemFactory()
            )
        ).apply {
            registerDataSetObserver(object : DataSetObserver() {
                override fun onChanged() {
                    super.onChanged()
                    dataFromObserver = data
                }
            })

            Assert.assertNull(data)
            Assert.assertNull(dataFromObserver)

            data = Strings("Test data changed notify invoke")
            Assert.assertEquals(Strings("Test data changed notify invoke"), data)
            Assert.assertEquals(Strings("Test data changed notify invoke"), dataFromObserver)

            data = Strings("Test data changed notify invoke2")
            Assert.assertEquals(Strings("Test data changed notify invoke2"), data)
            Assert.assertEquals(Strings("Test data changed notify invoke2"), dataFromObserver)
        }
    }

    @Test
    fun testMethodGetGroupCount() {
        AssemblySingleDataExpandableListAdapter<Strings, String>(
            listOf(
                StringsGroupItemFactory(),
                StringsChildItemFactory()
            )
        ).apply {
            Assert.assertEquals(0, groupCount)

            data = Strings("Test count")
            Assert.assertEquals(1, groupCount)

            data = null
            Assert.assertEquals(0, groupCount)
        }
    }

    @Test
    fun testMethodGetGroup() {
        AssemblySingleDataExpandableListAdapter<Strings, String>(
            listOf(
                StringsGroupItemFactory(),
                StringsChildItemFactory()
            )
        ).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getGroup(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getGroup(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getGroup(1)
            }

            data = Strings("test")
            Assert.assertEquals(Strings("test"), getGroup(0))
        }
    }

    @Test
    fun testMethodGetGroupId() {
        AssemblySingleDataExpandableListAdapter<Strings, String>(
            listOf(
                StringsGroupItemFactory(),
                StringsChildItemFactory()
            )
        ).apply {
            Assert.assertEquals(-1L, getGroupId(-1))
            Assert.assertEquals(0L, getGroupId(0))
            Assert.assertEquals(1L, getGroupId(1))
            Assert.assertEquals(Int.MAX_VALUE.toLong(), getGroupId(Int.MAX_VALUE))
            Assert.assertEquals(Int.MIN_VALUE.toLong(), getGroupId(Int.MIN_VALUE))
        }
    }

    @Test
    fun testMethodGetGroupTypeCount() {
        AssemblySingleDataExpandableListAdapter<Strings, String>(
            listOf(
                StringsGroupItemFactory(),
            )
        ).apply {
            Assert.assertEquals(1, groupTypeCount)
        }

        AssemblySingleDataExpandableListAdapter<Strings, String>(
            listOf(
                StringsGroupItemFactory(),
                StringsChildItemFactory()
            )
        ).apply {
            Assert.assertEquals(2, groupTypeCount)
        }
    }

    @Test
    fun testMethodGetGroupType() {
        AssemblySingleDataExpandableListAdapter<Strings, String>(
            listOf(
                StringsGroupItemFactory(),
                StringsChildItemFactory()
            )
        ).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getGroupType(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getGroupType(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getGroupType(1)
            }

            data = Strings("test")
            Assert.assertEquals(0, getGroupType(0))
        }
    }

    @Test
    fun testMethodGetGroupView() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)
        AssemblySingleDataExpandableListAdapter<Strings, String>(
            listOf(
                StringsGroupItemFactory(),
                StringsChildItemFactory()
            )
        ).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getGroupView(-1, false, null, parent)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getGroupView(0, false, null, parent)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getGroupView(1, false, null, parent)
            }

            data = Strings("test")
            val itemView = getGroupView(0, false, null, parent)
            Assert.assertNotSame(itemView, getGroupView(0, false, null, parent))
            Assert.assertSame(itemView, getGroupView(0, false, itemView, parent))
        }
    }

    @Test
    fun testMethodGetChildCount() {
        AssemblySingleDataExpandableListAdapter<Strings, String>(
            listOf(
                StringsGroupItemFactory(),
                StringsChildItemFactory()
            ),
            Strings("Test count")
        ).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getChildrenCount(-1)
            }
            Assert.assertEquals(10, getChildrenCount(0))
            assertThrow(IndexOutOfBoundsException::class) {
                getChildrenCount(1)
            }

            data = Strings("Test2")
            Assert.assertEquals(5, getChildrenCount(0))
        }
    }

    @Test
    fun testMethodGetChild() {
        AssemblySingleDataExpandableListAdapter<Strings, String>(
            listOf(
                StringsGroupItemFactory(),
                StringsChildItemFactory()
            ),
            Strings("test")
        ).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getChild(-1, 0)
            }
            assertThrow(StringIndexOutOfBoundsException::class) {
                Assert.assertEquals("t", getChild(0, -1))
            }
            Assert.assertEquals("t", getChild(0, 0))
            Assert.assertEquals("e", getChild(0, 1))
            Assert.assertEquals("s", getChild(0, 2))
            Assert.assertEquals("t", getChild(0, 3))
            assertThrow(StringIndexOutOfBoundsException::class) {
                Assert.assertEquals("t", getChild(0, 4))
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getChild(-1, 0)
            }
        }
    }

    @Test
    fun testMethodGetChildId() {
        AssemblySingleDataExpandableListAdapter<Strings, String>(
            listOf(
                StringsGroupItemFactory(),
                StringsChildItemFactory()
            )
        ).apply {
            Assert.assertEquals(-1L, getChildId(-1, -1))
            Assert.assertEquals(0L, getChildId(0, 0))
            Assert.assertEquals(1L, getChildId(1, 1))
            Assert.assertEquals(Int.MAX_VALUE.toLong(), getChildId(Int.MAX_VALUE, Int.MAX_VALUE))
            Assert.assertEquals(Int.MIN_VALUE.toLong(), getChildId(Int.MIN_VALUE, Int.MIN_VALUE))
        }
    }

    @Test
    fun testMethodGetChildTypeCount() {
        AssemblySingleDataExpandableListAdapter<Strings, String>(
            listOf(
                StringsGroupItemFactory(),
            )
        ).apply {
            Assert.assertEquals(1, childTypeCount)
        }

        AssemblySingleDataExpandableListAdapter<Strings, String>(
            listOf(
                StringsGroupItemFactory(),
                StringsChildItemFactory()
            )
        ).apply {
            Assert.assertEquals(2, childTypeCount)
        }
    }

    @Test
    fun testMethodGetChildType() {
        AssemblySingleDataExpandableListAdapter<Strings, String>(
            listOf(
                StringsGroupItemFactory(),
                StringsChildItemFactory()
            ),
            Strings("test")
        ).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getChildType(-1, -1)
            }
            assertThrow(StringIndexOutOfBoundsException::class) {
                getChildType(0, -1)
            }
            Assert.assertEquals(1, getChildType(0, 0))
            Assert.assertEquals(1, getChildType(0, 1))
            Assert.assertEquals(1, getChildType(0, 2))
            Assert.assertEquals(1, getChildType(0, 3))
            assertThrow(StringIndexOutOfBoundsException::class) {
                getChildType(0, 4)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getChildType(1, 1)
            }
        }
    }

    @Test
    fun testMethodGetChildView() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)
        AssemblySingleDataExpandableListAdapter<Strings, String>(
            listOf(
                StringsGroupItemFactory(),
                StringsChildItemFactory()
            )
        ).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getChildView(-1, -1, false, null, parent)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getChildView(0, 0, false, null, parent)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getChildView(1, 1, false, null, parent)
            }

            data = Strings("test")
            assertThrow(StringIndexOutOfBoundsException::class) {
                getChildView(0, -1, false, null, parent)
            }
            assertThrow(StringIndexOutOfBoundsException::class) {
                getChildView(0, 4, false, null, parent)
            }
            val itemView = getChildView(0, 0, false, null, parent)
            Assert.assertNotSame(itemView, getChildView(0, 0, false, null, parent))
            Assert.assertSame(itemView, getChildView(0, 0, false, itemView, parent))
        }
    }

    @Test
    fun testMethodGetItemFactoryByPosition() {
        val groupItemFactory = StringsGroupItemFactory()
        val childItemFactory = StringsChildItemFactory()
        AssemblySingleDataExpandableListAdapter<Strings, String>(
            listOf(
                groupItemFactory,
                childItemFactory
            )
        ).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByPosition(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByPosition(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByPosition(1)
            }

            data = Strings("test")
            Assert.assertSame(groupItemFactory, getItemFactoryByPosition(0))
        }
    }
}