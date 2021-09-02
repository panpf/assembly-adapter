package com.github.panpf.assemblyadapter.list.test.expandable

import android.database.DataSetObserver
import android.widget.FrameLayout
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.NotFoundMatchedItemFactoryException
import com.github.panpf.assemblyadapter.ViewItemFactory
import com.github.panpf.assemblyadapter.list.expandable.AssemblySingleDataExpandableListAdapter
import com.github.panpf.assemblyadapter.list.expandable.ExpandableGroup
import com.github.panpf.assemblyadapter.list.expandable.ViewExpandableGroupItemFactory
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class AssemblySingleDataExpandableListAdapterTest {

    private data class Text(val text: String)

    private data class TextGroup(val list: List<Text>) : ExpandableGroup {

        @Suppress("unused")
        val listJoinToString: String
            get() = list.joinToString(prefix = "[", postfix = "]") { it.text }

        constructor(vararg texts: String) : this(texts.map { Text(it) }.toList())

        override fun getChildCount(): Int = list.size

        override fun getChild(childPosition: Int): Any {
            // Shield the differences in exceptions thrown by different versions of the ArrayList get method
            return list.getOrNull(childPosition)
                ?: throw IndexOutOfBoundsException("Index: $childPosition, Size: ${list.size}")
        }
    }

    private class TextItemFactory : ViewItemFactory<Text>(Text::class, { context, _, _ ->
        TextView(context)
    })

    private class TextGroupItemFactory :
        ViewExpandableGroupItemFactory<TextGroup>(TextGroup::class, { context, _, _ ->
            FrameLayout(context)
        })

    @Test
    fun testConstructor() {
        AssemblySingleDataExpandableListAdapter<TextGroup, Text>(
            listOf(
                TextGroupItemFactory(),
                TextItemFactory()
            )
        ).apply {
            Assert.assertNull(data)
        }

        AssemblySingleDataExpandableListAdapter<TextGroup, Text>(
            listOf(
                TextGroupItemFactory(),
                TextItemFactory()
            ),
            TextGroup("hello")
        ).apply {
            Assert.assertEquals(TextGroup("hello"), data)
        }
    }

    @Test
    fun testPropertyData() {
        var dataFromObserver: TextGroup? = null
        AssemblySingleDataExpandableListAdapter<TextGroup, Text>(
            listOf(
                TextGroupItemFactory(),
                TextItemFactory()
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

            data = TextGroup("hello")
            Assert.assertEquals(TextGroup("hello"), data)
            Assert.assertEquals(TextGroup("hello"), dataFromObserver)

            data = TextGroup("world")
            Assert.assertEquals(TextGroup("world"), data)
            Assert.assertEquals(TextGroup("world"), dataFromObserver)
        }
    }

    @Test
    fun testMethodGetGroupAndChildCount() {
        AssemblySingleDataExpandableListAdapter<TextGroup, Text>(
            listOf(
                TextGroupItemFactory(),
                TextItemFactory()
            )
        ).apply {
            Assert.assertEquals(0, groupCount)
            assertThrow(IndexOutOfBoundsException::class) {
                getChildrenCount(0)
            }

            data = TextGroup("hello", "world")
            Assert.assertEquals(1, groupCount)
            Assert.assertEquals(2, getChildrenCount(0))

            data = null
            Assert.assertEquals(0, groupCount)
            assertThrow(IndexOutOfBoundsException::class) {
                getChildrenCount(0)
            }
        }
    }

    @Test
    fun testMethodGetGroupAndChild() {
        AssemblySingleDataExpandableListAdapter<TextGroup, Text>(
            listOf(
                TextGroupItemFactory(),
                TextItemFactory()
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

            data = TextGroup("hello", "world")
            Assert.assertEquals("[hello, world]", getGroup(0).listJoinToString)
            assertThrow(IndexOutOfBoundsException::class) {
                getChild(0, -1)
            }
            Assert.assertEquals(Text("hello"), getChild(0, 0))
            Assert.assertEquals(Text("world"), getChild(0, 1))
            assertThrow(IndexOutOfBoundsException::class) {
                getChild(0, 2)
            }
        }
    }

    @Test
    fun testMethodGetGroupAndChildId() {
        AssemblySingleDataExpandableListAdapter<TextGroup, Text>(TextGroupItemFactory()).apply {
            Assert.assertEquals(-1L, getGroupId(-1))
            Assert.assertEquals(-1L, getGroupId(0))
            Assert.assertEquals(-1L, getGroupId(1))
            Assert.assertEquals(-1L, getChildId(-1, -1))
            Assert.assertEquals(-1L, getChildId(0, 0))
            Assert.assertEquals(-1L, getChildId(1, 1))
        }

        AssemblySingleDataExpandableListAdapter<TextGroup, Text>(
            TextGroupItemFactory(),
        ).apply {
            setHasStableIds(true)
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

        AssemblySingleDataExpandableListAdapter<TextGroup, Text>(
            TextGroupItemFactory(),
            initData = TextGroup("hello", "world"),
        ).apply {
            setHasStableIds(true)
            assertThrow(IndexOutOfBoundsException::class) {
                getGroupId(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getChildId(-1, -1)
            }

            Assert.assertEquals(getGroup(0).hashCode().toLong(), getGroupId(0))
            assertThrow(IndexOutOfBoundsException::class) {
                getChildId(0, -1)
            }
            Assert.assertEquals(getChild(0, 0).hashCode().toLong(), getChildId(0, 0))
            Assert.assertEquals(getChild(0, 1).hashCode().toLong(), getChildId(0, 1))
            assertThrow(IndexOutOfBoundsException::class) {
                getChildId(0, 2)
            }

            assertThrow(IndexOutOfBoundsException::class) {
                getGroupId(1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getChildId(1, 0)
            }
        }
    }

    @Test
    fun testMethodGetGroupAndChildTypeCount() {
        AssemblySingleDataExpandableListAdapter<TextGroup, Text>(
            listOf(
                TextGroupItemFactory(),
            )
        ).apply {
            Assert.assertEquals(1, groupTypeCount)
            Assert.assertEquals(1, childTypeCount)
        }

        AssemblySingleDataExpandableListAdapter<TextGroup, Text>(
            listOf(
                TextGroupItemFactory(),
                TextItemFactory()
            )
        ).apply {
            Assert.assertEquals(2, groupTypeCount)
            Assert.assertEquals(2, childTypeCount)
        }
    }

    @Test
    fun testMethodGetGroupAndChildType() {
        AssemblySingleDataExpandableListAdapter<TextGroup, Text>(
            listOf(
                TextGroupItemFactory(),
                TextItemFactory(),
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

            data = TextGroup("hello")
            Assert.assertEquals(0, getGroupType(0))
            Assert.assertEquals(1, getChildType(0, 0))
        }
    }

    @Test
    fun testMethodGetGroupAndChildView() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)
        AssemblySingleDataExpandableListAdapter<TextGroup, Text>(
            listOf(
                TextGroupItemFactory(),
                TextItemFactory()
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

            data = TextGroup("test")
            val groupView = getGroupView(0, false, null, parent)
            Assert.assertNotSame(groupView, getGroupView(0, false, null, parent))
            Assert.assertSame(groupView, getGroupView(0, false, groupView, parent))

            assertThrow(IndexOutOfBoundsException::class) {
                getChildView(0, -1, false, null, parent)
            }
            val childView = getChildView(0, 0, false, null, parent)
            Assert.assertNotSame(childView, getChildView(0, 0, false, null, parent))
            Assert.assertSame(childView, getChildView(0, 0, false, childView, parent))
            assertThrow(IndexOutOfBoundsException::class) {
                getChildView(0, 4, false, null, parent)
            }
        }
    }

    @Test
    fun testMethodGetItemFactoryByPosition() {
        val groupItemFactory = TextGroupItemFactory()
        val childItemFactory = TextItemFactory()
        AssemblySingleDataExpandableListAdapter<TextGroup, Text>(
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

            data = TextGroup("hello", "world")

            Assert.assertSame(groupItemFactory, getItemFactoryByPosition(0))
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByChildPosition(0, -1)
            }
            Assert.assertSame(childItemFactory, getItemFactoryByChildPosition(0, 0))
            Assert.assertSame(childItemFactory, getItemFactoryByChildPosition(0, 1))
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByChildPosition(0, 2)
            }
        }

        AssemblySingleDataExpandableListAdapter<TextGroup, Text>(
            listOf(groupItemFactory)
        ).apply {
            data = TextGroup("hello", "world")

            Assert.assertSame(groupItemFactory, getItemFactoryByPosition(0))
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByChildPosition(0, -1)
            }
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                Assert.assertSame(childItemFactory, getItemFactoryByChildPosition(0, 0))
            }
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                Assert.assertSame(childItemFactory, getItemFactoryByChildPosition(0, 1))
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByChildPosition(0, 2)
            }
        }

        AssemblySingleDataExpandableListAdapter<TextGroup, Text>(
            listOf(childItemFactory)
        ).apply {
            data = TextGroup("hello", "world")

            assertThrow(NotFoundMatchedItemFactoryException::class) {
                Assert.assertSame(groupItemFactory, getItemFactoryByPosition(0))
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByChildPosition(0, -1)
            }
            Assert.assertSame(childItemFactory, getItemFactoryByChildPosition(0, 0))
            Assert.assertSame(childItemFactory, getItemFactoryByChildPosition(0, 1))
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByChildPosition(0, 2)
            }
        }
    }

    @Test
    fun testMethodHasObservers() {
        AssemblySingleDataExpandableListAdapter<TextGroup, Text>(
            listOf(TextGroupItemFactory(), TextItemFactory())
        ).apply {
            Assert.assertFalse(hasObservers())

            val dataObserver1 = object : DataSetObserver() {}
            val dataObserver2 = object : DataSetObserver() {}

            registerDataSetObserver(dataObserver1)
            Assert.assertTrue(hasObservers())

            registerDataSetObserver(dataObserver2)
            Assert.assertTrue(hasObservers())

            unregisterDataSetObserver(dataObserver1)
            Assert.assertTrue(hasObservers())

            unregisterDataSetObserver(dataObserver2)
            Assert.assertFalse(hasObservers())
        }
    }

    @Test
    fun testMethodIsChildSelectable() {
        AssemblySingleDataExpandableListAdapter<TextGroup, Text>(
            listOf(TextGroupItemFactory(), TextItemFactory()),
            TextGroup("hello", "world", "good"),
        ).apply {
            Assert.assertNull(childSelectable)

            childSelectable = { adapter, groupPosition, childPosition ->
                adapter.getChild(groupPosition, childPosition)
                groupPosition == 0 && childPosition == 2
            }
            Assert.assertNotNull(childSelectable)

            assertThrow(IndexOutOfBoundsException::class) {
                childSelectable(0, -1)
            }
            Assert.assertFalse(childSelectable(0, 0))
            Assert.assertFalse(childSelectable(0, 1))
            Assert.assertTrue(childSelectable(0, 2))
            assertThrow(IndexOutOfBoundsException::class) {
                childSelectable(0, 3)
            }
        }
    }
}