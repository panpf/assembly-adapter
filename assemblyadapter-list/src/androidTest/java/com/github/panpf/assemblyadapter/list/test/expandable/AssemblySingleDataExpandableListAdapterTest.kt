package com.github.panpf.assemblyadapter.list.test.expandable

import android.database.DataSetObserver
import android.widget.FrameLayout
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.NotFoundMatchedItemFactoryException
import com.github.panpf.assemblyadapter.ViewItemFactory
import com.github.panpf.assemblyadapter.list.expandable.AssemblySingleDataExpandableListAdapter
import com.github.panpf.assemblyadapter.list.expandable.ExpandableGroup
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class AssemblySingleDataExpandableListAdapterTest {

    private data class Strings(val name: String = "") : ExpandableGroup {

        override fun getChildCount(): Int = name.length

        override fun getChild(childPosition: Int): Any {
            return name[childPosition].toString()
        }
    }

    private class StringsItemFactory :
        ViewItemFactory<Strings>(Strings::class, android.R.layout.activity_list_item)

    private class StringItemFactory :
        ViewItemFactory<String>(String::class, android.R.layout.activity_list_item)

    @Test
    fun testConstructor() {
        AssemblySingleDataExpandableListAdapter<Strings, String>(
            listOf(
                StringsItemFactory(),
                StringItemFactory()
            )
        ).apply {
            Assert.assertNull(data)
        }

        AssemblySingleDataExpandableListAdapter<Strings, String>(
            listOf(
                StringsItemFactory(),
                StringItemFactory()
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
                StringsItemFactory(),
                StringItemFactory()
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
    fun testMethodGetGroupAndChildCount() {
        AssemblySingleDataExpandableListAdapter<Strings, String>(
            listOf(
                StringsItemFactory(),
                StringItemFactory()
            )
        ).apply {
            Assert.assertEquals(0, groupCount)
            assertThrow(IndexOutOfBoundsException::class) {
                getChildrenCount(0)
            }

            data = Strings("hello")
            Assert.assertEquals(1, groupCount)
            Assert.assertEquals(5, getChildrenCount(0))

            data = null
            Assert.assertEquals(0, groupCount)
            assertThrow(IndexOutOfBoundsException::class) {
                getChildrenCount(0)
            }
        }
    }

    @Test
    fun testMethodGetGroupAndChild() {
        AssemblySingleDataExpandableListAdapter<Strings, String>(
            listOf(
                StringsItemFactory(),
                StringItemFactory()
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

            data = Strings("hello")
            Assert.assertEquals("hello", getGroup(0).name)
            assertThrow(StringIndexOutOfBoundsException::class) {
                getChild(0, -1)
            }
            Assert.assertEquals("h", getChild(0, 0))
            Assert.assertEquals("e", getChild(0, 1))
            Assert.assertEquals("l", getChild(0, 2))
            Assert.assertEquals("l", getChild(0, 3))
            Assert.assertEquals("o", getChild(0, 4))
            assertThrow(StringIndexOutOfBoundsException::class) {
                getChild(0, 5)
            }
        }
    }

    @Test
    fun testMethodGetGroupAndChildId() {
        AssemblySingleDataExpandableListAdapter<Strings, String>(StringsItemFactory()).apply {
            Assert.assertEquals(-1L, getGroupId(-1))
            Assert.assertEquals(-1L, getGroupId(0))
            Assert.assertEquals(-1L, getGroupId(1))
            Assert.assertEquals(-1L, getChildId(-1, -1))
            Assert.assertEquals(-1L, getChildId(0, 0))
            Assert.assertEquals(-1L, getChildId(1, 1))
        }

        AssemblySingleDataExpandableListAdapter<Strings, String>(
            StringsItemFactory(),
            hasStableIds = true
        ).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getGroupId(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getGroupId(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getGroupId(1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getChildId(-1, -1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getChildId(0, 0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getChildId(1, 1)
            }
        }

        AssemblySingleDataExpandableListAdapter<Strings, String>(
            StringsItemFactory(),
            initData = Strings("hello"),
            hasStableIds = true
        ).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getGroupId(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getChildId(-1, -1)
            }

            Assert.assertEquals("hello".hashCode().toLong(), getGroupId(0))
            assertThrow(StringIndexOutOfBoundsException::class) {
                getChildId(0, -1)
            }
            Assert.assertEquals(getChild(0, 0).hashCode().toLong(), getChildId(0, 0))
            Assert.assertEquals(getChild(0, 1).hashCode().toLong(), getChildId(0, 1))
            Assert.assertEquals(getChild(0, 2).hashCode().toLong(), getChildId(0, 2))
            Assert.assertEquals(getChild(0, 3).hashCode().toLong(), getChildId(0, 3))
            Assert.assertEquals(getChild(0, 4).hashCode().toLong(), getChildId(0, 4))
            assertThrow(StringIndexOutOfBoundsException::class) {
                getChildId(0, 5)
            }

            assertThrow(IndexOutOfBoundsException::class) {
                getGroupId(1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getChildId(1, 1)
            }
        }
    }

    @Test
    fun testMethodGetGroupAndChildTypeCount() {
        AssemblySingleDataExpandableListAdapter<Strings, String>(
            listOf(
                StringsItemFactory(),
            )
        ).apply {
            Assert.assertEquals(1, groupTypeCount)
            Assert.assertEquals(1, childTypeCount)
        }

        AssemblySingleDataExpandableListAdapter<Strings, String>(
            listOf(
                StringsItemFactory(),
                StringItemFactory()
            )
        ).apply {
            Assert.assertEquals(2, groupTypeCount)
            Assert.assertEquals(2, childTypeCount)
        }
    }

    @Test
    fun testMethodGetGroupAndChildType() {
        AssemblySingleDataExpandableListAdapter<Strings, String>(
            listOf(
                StringsItemFactory(),
                StringItemFactory(),
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
            assertThrow(IndexOutOfBoundsException::class) {
                getChildType(-1, -1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getChildType(0, 0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getChildType(1, 1)
            }

            data = Strings("hello")
            Assert.assertEquals(0, getGroupType(0))
            Assert.assertEquals(1, getChildType(0, 0))
        }
    }

    @Test
    fun testMethodGetGroupAndChildView() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)
        AssemblySingleDataExpandableListAdapter<Strings, String>(
            listOf(
                StringsItemFactory(),
                StringItemFactory()
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
            val groupView = getGroupView(0, false, null, parent)
            Assert.assertNotSame(groupView, getGroupView(0, false, null, parent))
            Assert.assertSame(groupView, getGroupView(0, false, groupView, parent))

            assertThrow(StringIndexOutOfBoundsException::class) {
                getChildView(0, -1, false, null, parent)
            }
            val childView = getChildView(0, 0, false, null, parent)
            Assert.assertNotSame(childView, getChildView(0, 0, false, null, parent))
            Assert.assertSame(childView, getChildView(0, 0, false, childView, parent))
            assertThrow(StringIndexOutOfBoundsException::class) {
                getChildView(0, 4, false, null, parent)
            }
        }
    }

    @Test
    fun testMethodGetItemFactoryByPosition() {
        val groupItemFactory = StringsItemFactory()
        val childItemFactory = StringItemFactory()
        AssemblySingleDataExpandableListAdapter<Strings, String>(
            listOf(groupItemFactory, childItemFactory)
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
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByChildPosition(-1, 0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByChildPosition(0, 0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByChildPosition(1, 0)
            }

            data = Strings("test")

            Assert.assertSame(groupItemFactory, getItemFactoryByPosition(0))
            assertThrow(StringIndexOutOfBoundsException::class) {
                getItemFactoryByChildPosition(0, -1)
            }
            Assert.assertSame(childItemFactory, getItemFactoryByChildPosition(0, 0))
            Assert.assertSame(childItemFactory, getItemFactoryByChildPosition(0, 1))
            Assert.assertSame(childItemFactory, getItemFactoryByChildPosition(0, 2))
            Assert.assertSame(childItemFactory, getItemFactoryByChildPosition(0, 3))
            assertThrow(StringIndexOutOfBoundsException::class) {
                getItemFactoryByChildPosition(0, 4)
            }
        }

        AssemblySingleDataExpandableListAdapter<Strings, String>(
            listOf(groupItemFactory)
        ).apply {
            data = Strings("test")

            Assert.assertSame(groupItemFactory, getItemFactoryByPosition(0))
            assertThrow(StringIndexOutOfBoundsException::class) {
                getItemFactoryByChildPosition(0, -1)
            }
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                Assert.assertSame(childItemFactory, getItemFactoryByChildPosition(0, 0))
            }
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                Assert.assertSame(childItemFactory, getItemFactoryByChildPosition(0, 1))
            }
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                Assert.assertSame(childItemFactory, getItemFactoryByChildPosition(0, 2))
            }
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                Assert.assertSame(childItemFactory, getItemFactoryByChildPosition(0, 3))
            }
            assertThrow(StringIndexOutOfBoundsException::class) {
                getItemFactoryByChildPosition(0, 4)
            }
        }

        AssemblySingleDataExpandableListAdapter<Strings, String>(
            listOf(childItemFactory)
        ).apply {
            data = Strings("test")

            assertThrow(NotFoundMatchedItemFactoryException::class) {
                Assert.assertSame(groupItemFactory, getItemFactoryByPosition(0))
            }
            assertThrow(StringIndexOutOfBoundsException::class) {
                getItemFactoryByChildPosition(0, -1)
            }
            Assert.assertSame(childItemFactory, getItemFactoryByChildPosition(0, 0))
            Assert.assertSame(childItemFactory, getItemFactoryByChildPosition(0, 1))
            Assert.assertSame(childItemFactory, getItemFactoryByChildPosition(0, 2))
            Assert.assertSame(childItemFactory, getItemFactoryByChildPosition(0, 3))
            assertThrow(StringIndexOutOfBoundsException::class) {
                getItemFactoryByChildPosition(0, 4)
            }
        }
    }
}