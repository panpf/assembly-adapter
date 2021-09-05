package com.github.panpf.assemblyadapter.list.test.expandable

import android.widget.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.Item
import com.github.panpf.assemblyadapter.ViewItemFactory
import com.github.panpf.assemblyadapter.list.expandable.*
import com.github.panpf.assemblyadapter.list.test.R
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ConcatExpandableListAdapterTest {

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

    private data class Image(val resId: Int)

    private data class ImageGroup(val list: List<Image>) : ExpandableGroup {

        @Suppress("unused")
        val listJoinToString: String
            get() = list.joinToString(prefix = "[", postfix = "]") { it.resId.toString() }

        constructor(vararg resIds: Int) : this(resIds.map { Image(it) }.toList())

        override fun getChildCount(): Int = list.size

        override fun getChild(childPosition: Int): Any {
            // Shield the differences in exceptions thrown by different versions of the ArrayList get method
            return list.getOrNull(childPosition)
                ?: throw IndexOutOfBoundsException("Index: $childPosition, Size: ${list.size}")
        }
    }

    private class ImageItemFactory : ViewItemFactory<Image>(Image::class, { context, _, _ ->
        ImageView(context)
    })

    private class ImageGroupItemFactory :
        ViewExpandableGroupItemFactory<ImageGroup>(ImageGroup::class, { context, _, _ ->
            LinearLayout(context)
        })

    @Test
    fun testConfig() {
        ConcatExpandableListAdapter.Config.DEFAULT.apply {
            Assert.assertEquals(true, this.isolateViewTypes)
            Assert.assertEquals(
                ConcatExpandableListAdapter.Config.StableIdMode.NO_STABLE_IDS,
                this.stableIdMode
            )
        }

        ConcatExpandableListAdapter.Config.Builder().build().apply {
            Assert.assertEquals(true, this.isolateViewTypes)
            Assert.assertEquals(
                ConcatExpandableListAdapter.Config.StableIdMode.NO_STABLE_IDS,
                this.stableIdMode
            )
        }

        ConcatExpandableListAdapter.Config.Builder().setIsolateViewTypes(false).build().apply {
            Assert.assertEquals(false, this.isolateViewTypes)
            Assert.assertEquals(
                ConcatExpandableListAdapter.Config.StableIdMode.NO_STABLE_IDS,
                this.stableIdMode
            )
        }

        ConcatExpandableListAdapter.Config.Builder()
            .setStableIdMode(ConcatExpandableListAdapter.Config.StableIdMode.ISOLATED_STABLE_IDS)
            .build()
            .apply {
                Assert.assertEquals(true, this.isolateViewTypes)
                Assert.assertEquals(
                    ConcatExpandableListAdapter.Config.StableIdMode.ISOLATED_STABLE_IDS,
                    this.stableIdMode
                )
            }

        ConcatExpandableListAdapter.Config.Builder()
            .setStableIdMode(ConcatExpandableListAdapter.Config.StableIdMode.SHARED_STABLE_IDS)
            .build()
            .apply {
                Assert.assertEquals(true, this.isolateViewTypes)
                Assert.assertEquals(
                    ConcatExpandableListAdapter.Config.StableIdMode.SHARED_STABLE_IDS,
                    this.stableIdMode
                )
            }
    }

    @Test
    fun testConstructor() {
        ConcatExpandableListAdapter(
            AssemblySingleDataExpandableListAdapter<TextGroup, Text>(
                TextGroupItemFactory()
            )
        ).apply {
            Assert.assertEquals(1, adapters.size)
        }
        ConcatExpandableListAdapter(
            AssemblySingleDataExpandableListAdapter<TextGroup, Text>(TextGroupItemFactory()),
            AssemblySingleDataExpandableListAdapter<TextGroup, Text>(TextGroupItemFactory())
        ).apply {
            Assert.assertEquals(2, adapters.size)
        }

        ConcatExpandableListAdapter(
            ConcatExpandableListAdapter.Config.DEFAULT,
            AssemblySingleDataExpandableListAdapter<TextGroup, Text>(TextGroupItemFactory())
        ).apply {
            Assert.assertEquals(1, adapters.size)
        }
        ConcatExpandableListAdapter(
            ConcatExpandableListAdapter.Config.DEFAULT,
            AssemblySingleDataExpandableListAdapter<TextGroup, Text>(TextGroupItemFactory()),
            AssemblySingleDataExpandableListAdapter<TextGroup, Text>(TextGroupItemFactory())
        ).apply {
            Assert.assertEquals(2, adapters.size)
        }

        ConcatExpandableListAdapter(
            listOf(
                AssemblySingleDataExpandableListAdapter<TextGroup, Text>(TextGroupItemFactory())
            )
        ).apply {
            Assert.assertEquals(1, adapters.size)
        }
        ConcatExpandableListAdapter(
            listOf(
                AssemblySingleDataExpandableListAdapter(TextGroupItemFactory()),
                AssemblySingleDataExpandableListAdapter<TextGroup, Text>(TextGroupItemFactory())
            )
        ).apply {
            Assert.assertEquals(2, adapters.size)
        }

        ConcatExpandableListAdapter(
            ConcatExpandableListAdapter.Config.DEFAULT,
            listOf(AssemblySingleDataExpandableListAdapter<TextGroup, Text>(TextGroupItemFactory()))
        ).apply {
            Assert.assertEquals(1, adapters.size)
        }
        ConcatExpandableListAdapter(
            ConcatExpandableListAdapter.Config.DEFAULT,
            listOf(
                AssemblySingleDataExpandableListAdapter(TextGroupItemFactory()),
                AssemblySingleDataExpandableListAdapter<TextGroup, Text>(TextGroupItemFactory())
            )
        ).apply {
            Assert.assertEquals(2, adapters.size)
        }
    }

    @Test
    fun testMethodAddAndRemoveAdapter() {
        ConcatExpandableListAdapter().apply {
            Assert.assertEquals(0, adapters.size)
            Assert.assertEquals(0, groupCount)
            Assert.assertEquals("", adapters.joinToString { it.groupCount.toString() })

            val adapter1 = AssemblySingleDataExpandableListAdapter<TextGroup, Text>(
                TextGroupItemFactory(),
                TextGroup("a")
            )
            val adapter2 =
                AssemblyExpandableListAdapter<TextGroup, Text>(
                    listOf(TextGroupItemFactory(), TextItemFactory()),
                    listOf(TextGroup("b"), TextGroup("c"))
                )
            val adapter3 =
                AssemblyExpandableListAdapter<TextGroup, Text>(
                    listOf(TextGroupItemFactory(), TextItemFactory()),
                    listOf(TextGroup("d"), TextGroup("e"), TextGroup("f"))
                )

            addAdapter(adapter1)
            Assert.assertEquals(1, adapters.size)
            Assert.assertEquals(1, groupCount)
            Assert.assertEquals("1", adapters.joinToString { it.groupCount.toString() })

            addAdapter(adapter3)
            Assert.assertEquals(2, adapters.size)
            Assert.assertEquals(4, groupCount)
            Assert.assertEquals("1, 3", adapters.joinToString { it.groupCount.toString() })

            addAdapter(1, adapter2)
            Assert.assertEquals(3, adapters.size)
            Assert.assertEquals(6, groupCount)
            Assert.assertEquals("1, 2, 3", adapters.joinToString { it.groupCount.toString() })

            removeAdapter(adapter1)
            Assert.assertEquals(2, adapters.size)
            Assert.assertEquals(5, groupCount)
            Assert.assertEquals("2, 3", adapters.joinToString { it.groupCount.toString() })

            removeAdapter(adapter3)
            Assert.assertEquals(1, adapters.size)
            Assert.assertEquals(2, groupCount)
            Assert.assertEquals("2", adapters.joinToString { it.groupCount.toString() })
        }
    }

    @Test
    fun testMethodGetGroupAndChildTypeCount() {
        // IsolateViewTypes true
        ConcatExpandableListAdapter().apply {
            Assert.assertEquals(0, groupTypeCount)
            Assert.assertEquals(0, childTypeCount)

            addAdapter(AssemblySingleDataExpandableListAdapter<Text, Any>(TextItemFactory()))
            Assert.assertEquals(1, groupTypeCount)
            Assert.assertEquals(1, childTypeCount)

            addAdapter(AssemblySingleDataExpandableListAdapter<Text, Any>(TextItemFactory()))
            Assert.assertEquals(2, groupTypeCount)
            Assert.assertEquals(2, childTypeCount)

            addAdapter(
                AssemblyExpandableListAdapter<Any, Any>(
                    listOf(
                        TextItemFactory(),
                        TextItemFactory()
                    )
                )
            )
            Assert.assertEquals(4, groupTypeCount)
            Assert.assertEquals(4, childTypeCount)
        }

        // IsolateViewTypes false
        ConcatExpandableListAdapter(
            ConcatExpandableListAdapter.Config.Builder()
                .setIsolateViewTypes(false)
                .build()
        ).apply {
            Assert.assertEquals(0, groupTypeCount)
            Assert.assertEquals(0, childTypeCount)

            addAdapter(AssemblySingleDataExpandableListAdapter<Text, Any>(TextItemFactory()))
            Assert.assertEquals(1, groupTypeCount)
            Assert.assertEquals(1, childTypeCount)

            addAdapter(AssemblySingleDataExpandableListAdapter<Text, Any>(TextItemFactory()))
            Assert.assertEquals(2, groupTypeCount)
            Assert.assertEquals(2, childTypeCount)

            addAdapter(
                AssemblyExpandableListAdapter<Any, Any>(
                    listOf(
                        TextItemFactory(),
                        TextItemFactory()
                    )
                )
            )
            Assert.assertEquals(4, groupTypeCount)
            Assert.assertEquals(4, childTypeCount)
        }
    }

    @Test
    fun testMethodGetGroupAndChildType() {
        // IsolateViewTypes true
        ConcatExpandableListAdapter(
            AssemblySingleDataExpandableListAdapter<TextGroup, Text>(
                listOf(TextGroupItemFactory(), TextItemFactory()),
                TextGroup("a")
            ),
            AssemblyExpandableListAdapter<Any, Any>(
                listOf(
                    TextGroupItemFactory(),
                    TextItemFactory(),
                    ImageGroupItemFactory(),
                    ImageItemFactory()
                ),
                listOf(
                    ImageGroup(android.R.drawable.alert_dark_frame),
                    TextGroup("c"),
                    ImageGroup(android.R.drawable.arrow_up_float)
                )
            ),
            AssemblySingleDataExpandableListAdapter<ImageGroup, Image>(
                listOf(ImageGroupItemFactory(), ImageItemFactory()),
                ImageGroup(android.R.drawable.btn_plus)
            ),
        ).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getGroupType(-1)
            }
            Assert.assertEquals(0, getGroupType(0))
            Assert.assertEquals(1, getGroupType(1))
            Assert.assertEquals(2, getGroupType(2))
            Assert.assertEquals(1, getGroupType(3))
            Assert.assertEquals(3, getGroupType(4))
            assertThrow(IllegalArgumentException::class) {
                getGroupType(5)
            }

            assertThrow(IndexOutOfBoundsException::class) {
                getChildType(-1, 0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getChildType(0, -1)
            }
            Assert.assertEquals(4, getChildType(0, 0))
            assertThrow(IndexOutOfBoundsException::class) {
                getChildType(0, 1)
            }
            Assert.assertEquals(5, getChildType(1, 0))
            Assert.assertEquals(6, getChildType(2, 0))
            Assert.assertEquals(5, getChildType(3, 0))
            Assert.assertEquals(7, getChildType(4, 0))
            assertThrow(IllegalArgumentException::class) {
                getChildType(5, 0)
            }
        }

        // IsolateViewTypes false
        ConcatExpandableListAdapter(
            ConcatExpandableListAdapter.Config.Builder()
                .setIsolateViewTypes(false)
                .build(),
            AssemblySingleDataExpandableListAdapter<TextGroup, Text>(
                listOf(TextGroupItemFactory(), TextItemFactory()),
                TextGroup("a")
            ),
            AssemblyExpandableListAdapter<Any, Any>(
                listOf(
                    TextGroupItemFactory(),
                    TextItemFactory(),
                    ImageGroupItemFactory(),
                    ImageItemFactory()
                ),
                listOf(
                    ImageGroup(android.R.drawable.alert_dark_frame),
                    TextGroup("c"),
                    ImageGroup(android.R.drawable.arrow_up_float)
                )
            ),
            AssemblySingleDataExpandableListAdapter<ImageGroup, Image>(
                listOf(ImageGroupItemFactory(), ImageItemFactory()),
                ImageGroup(android.R.drawable.btn_plus)
            ),
        ).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getGroupType(-1)
            }
            Assert.assertEquals(0, getGroupType(0))
            Assert.assertEquals(2, getGroupType(1))
            Assert.assertEquals(0, getGroupType(2))
            Assert.assertEquals(2, getGroupType(3))
            Assert.assertEquals(0, getGroupType(4))
            assertThrow(IllegalArgumentException::class) {
                getGroupType(5)
            }

            assertThrow(IndexOutOfBoundsException::class) {
                getChildType(-1, 0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getChildType(0, -1)
            }
            Assert.assertEquals(1, getChildType(0, 0))
            assertThrow(IndexOutOfBoundsException::class) {
                getChildType(0, 1)
            }
            Assert.assertEquals(3, getChildType(1, 0))
            Assert.assertEquals(1, getChildType(2, 0))
            Assert.assertEquals(3, getChildType(3, 0))
            Assert.assertEquals(1, getChildType(4, 0))
            assertThrow(IllegalArgumentException::class) {
                getChildType(5, 0)
            }
        }
    }

    @Test
    fun testMethodGetGroupAndChildId() {
        val currentTimeMillis = System.currentTimeMillis()
        val child0 = currentTimeMillis.toString()
        val child1 = (currentTimeMillis + 1).toString()
        val child2 = (currentTimeMillis + 2).toString()
        val data0 = TextGroup(child0)
        val data1 = TextGroup(child1)
        val data2 = TextGroup(child2)
        val data0ItemId = data0.hashCode().toLong()
        val data1ItemId = data1.hashCode().toLong()
        val data2ItemId = data2.hashCode().toLong()
        val child0ItemId = child0.hashCode().toLong()
        val child1ItemId = child1.hashCode().toLong()
        val child2ItemId = child2.hashCode().toLong()

        // NO_STABLE_IDS
        ConcatExpandableListAdapter(
            AssemblySingleDataExpandableListAdapter<Any, Any>(
                itemFactoryList = listOf(TextGroupItemFactory(), TextItemFactory()),
                initData = data0
            ),
            AssemblyExpandableListAdapter<Any, Any>(
                itemFactoryList = listOf(TextGroupItemFactory(), TextItemFactory()),
                initDataList = listOf(data2, data1, data2)
            ),
            AssemblySingleDataExpandableListAdapter<Any, Any>(
                itemFactoryList = listOf(TextGroupItemFactory(), TextItemFactory()),
                initData = data1
            ),
        ).apply {
            Assert.assertEquals(-1L, getGroupId(-1))
            Assert.assertEquals(-1L, getGroupId(0))
            Assert.assertEquals(-1L, getGroupId(1))
            Assert.assertEquals(-1L, getGroupId(2))
            Assert.assertEquals(-1L, getGroupId(3))
            Assert.assertEquals(-1L, getGroupId(4))
            assertThrow(IllegalArgumentException::class) {
                getGroupId(5)
            }

            Assert.assertEquals(-1L, getChildId(-1, 0))
            Assert.assertEquals(-1L, getChildId(0, 0))
            Assert.assertEquals(-1L, getChildId(1, 0))
            Assert.assertEquals(-1L, getChildId(2, 0))
            Assert.assertEquals(-1L, getChildId(3, 0))
            Assert.assertEquals(-1L, getChildId(4, 0))
            assertThrow(IllegalArgumentException::class) {
                getChildId(5, 0)
            }
        }

        // ISOLATED_STABLE_IDS
        ConcatExpandableListAdapter(
            ConcatExpandableListAdapter.Config.Builder()
                .setStableIdMode(ConcatExpandableListAdapter.Config.StableIdMode.ISOLATED_STABLE_IDS)
                .build(),
            AssemblySingleDataExpandableListAdapter<Any, Any>(
                itemFactoryList = listOf(TextGroupItemFactory(), TextItemFactory()),
                initData = data0,
            ).apply { setHasStableIds(true) },
            AssemblyExpandableListAdapter<Any, Any>(
                itemFactoryList = listOf(TextGroupItemFactory(), TextItemFactory()),
                initDataList = listOf(data2, data1, data2),
            ).apply { setHasStableIds(true) },
            AssemblySingleDataExpandableListAdapter<Any, Any>(
                itemFactoryList = listOf(TextGroupItemFactory(), TextItemFactory()),
                initData = data1,
            ).apply { setHasStableIds(true) }
        ).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getGroupId(-1)
            }
            Assert.assertEquals(0L, getGroupId(0))
            Assert.assertEquals(1L, getGroupId(1))
            Assert.assertEquals(2L, getGroupId(2))
            Assert.assertEquals(1L, getGroupId(3))
            Assert.assertEquals(3L, getGroupId(4))
            assertThrow(IllegalArgumentException::class) {
                getGroupId(5)
            }

            assertThrow(IndexOutOfBoundsException::class) {
                getChildId(-1, 0)
            }
            Assert.assertEquals(4L, getChildId(0, 0))
            Assert.assertEquals(5L, getChildId(1, 0))
            Assert.assertEquals(6L, getChildId(2, 0))
            Assert.assertEquals(5L, getChildId(3, 0))
            Assert.assertEquals(7L, getChildId(4, 0))
            assertThrow(IllegalArgumentException::class) {
                getChildId(5, 0)
            }
        }

        // SHARED_STABLE_IDS
        ConcatExpandableListAdapter(
            ConcatExpandableListAdapter.Config.Builder()
                .setStableIdMode(ConcatExpandableListAdapter.Config.StableIdMode.SHARED_STABLE_IDS)
                .build(),
            AssemblySingleDataExpandableListAdapter<Any, Any>(
                itemFactoryList = listOf(TextGroupItemFactory(), TextItemFactory()),
                initData = data0,
            ).apply { setHasStableIds(true) },
            AssemblyExpandableListAdapter<Any, Any>(
                itemFactoryList = listOf(TextGroupItemFactory(), TextItemFactory()),
                initDataList = listOf(data2, data1, data2),
            ).apply { setHasStableIds(true) },
            AssemblySingleDataExpandableListAdapter<Any, Any>(
                itemFactoryList = listOf(TextGroupItemFactory(), TextItemFactory()),
                initData = data1,
            ).apply { setHasStableIds(true) },
        ).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getGroupId(-1)
            }
            Assert.assertEquals(data0ItemId, getGroupId(0))
            Assert.assertEquals(data2ItemId, getGroupId(1))
            Assert.assertEquals(data1ItemId, getGroupId(2))
            Assert.assertEquals(data2ItemId, getGroupId(3))
            Assert.assertEquals(data1ItemId, getGroupId(4))
            assertThrow(IllegalArgumentException::class) {
                getGroupId(5)
            }

            assertThrow(IndexOutOfBoundsException::class) {
                getChildId(-1, 0)
            }
            Assert.assertEquals(child0ItemId, getChildId(0, 0))
            Assert.assertEquals(child2ItemId, getChildId(1, 0))
            Assert.assertEquals(child1ItemId, getChildId(2, 0))
            Assert.assertEquals(child2ItemId, getChildId(3, 0))
            Assert.assertEquals(child1ItemId, getChildId(4, 0))
            assertThrow(IllegalArgumentException::class) {
                getChildId(5, 0)
            }
        }
    }

    @Test
    fun testMethodGetGroupAndChildView() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)
        ConcatExpandableListAdapter(
            AssemblySingleDataExpandableListAdapter<TextGroup, Text>(
                itemFactoryList = listOf(TextGroupItemFactory(), TextItemFactory()),
                initData = TextGroup("a"),
            ),
            AssemblyExpandableListAdapter<Any, Any>(
                itemFactoryList = listOf(
                    TextGroupItemFactory(),
                    TextItemFactory(),
                    ImageGroupItemFactory(),
                    ImageItemFactory()
                ),
                initDataList = listOf(
                    ImageGroup(android.R.drawable.bottom_bar),
                    TextGroup("b"),
                    ImageGroup(android.R.drawable.btn_plus)
                ),
            ),
            AssemblySingleDataExpandableListAdapter<ImageGroup, Image>(
                itemFactoryList = listOf(ImageGroupItemFactory(), ImageItemFactory()),
                initData = ImageGroup(android.R.drawable.alert_dark_frame),
            ),
        ).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getGroupView(-1, false, null, parent)
            }
            Assert.assertTrue(getGroupView(0, false, null, parent) is FrameLayout)
            Assert.assertTrue(getGroupView(1, false, null, parent) is LinearLayout)
            Assert.assertTrue(getGroupView(2, false, null, parent) is FrameLayout)
            Assert.assertTrue(getGroupView(3, false, null, parent) is LinearLayout)
            Assert.assertTrue(getGroupView(4, false, null, parent) is LinearLayout)
            assertThrow(IllegalArgumentException::class) {
                getGroupView(5, false, null, parent)
            }

            val groupView = getGroupView(0, false, null, parent)
            Assert.assertNotSame(groupView, getGroupView(0, false, null, parent))
            Assert.assertSame(groupView, getGroupView(0, false, groupView, parent))


            assertThrow(IndexOutOfBoundsException::class) {
                getChildView(-1, 0, false, null, parent)
            }
            Assert.assertTrue(getChildView(0, 0, false, null, parent) is TextView)
            Assert.assertTrue(getChildView(1, 0, false, null, parent) is ImageView)
            Assert.assertTrue(getChildView(2, 0, false, null, parent) is TextView)
            Assert.assertTrue(getChildView(3, 0, false, null, parent) is ImageView)
            Assert.assertTrue(getChildView(4, 0, false, null, parent) is ImageView)
            assertThrow(IllegalArgumentException::class) {
                getChildView(5, 0, false, null, parent)
            }

            val childView = getChildView(0, 0, false, null, parent)
            Assert.assertNotSame(childView, getChildView(0, 0, false, null, parent))
            Assert.assertSame(childView, getChildView(0, 0, false, childView, parent))
        }
    }

    @Test
    fun testMethodGetGroupAndChildCount() {
        val headerAdapter = AssemblySingleDataExpandableListAdapter<TextGroup, Any>(
            listOf(TextGroupItemFactory(), TextItemFactory())
        )
        val bodyAdapter = AssemblyExpandableListAdapter<Any, Any>(
            listOf(
                TextGroupItemFactory(),
                TextItemFactory(),
                ImageGroupItemFactory(),
                ImageItemFactory()
            )
        )
        val footerHeader = AssemblySingleDataExpandableListAdapter<ImageGroup, Any>(
            listOf(ImageGroupItemFactory(), ImageItemFactory())
        )
        ConcatExpandableListAdapter(headerAdapter, bodyAdapter, footerHeader).apply {
            Assert.assertEquals(0, groupCount)
            assertThrow(IllegalArgumentException::class) {
                getChildrenCount(0)
            }

            headerAdapter.data = TextGroup("hello")
            Assert.assertEquals(1, groupCount)
            Assert.assertEquals(1, getChildrenCount(0))

            bodyAdapter.submitList(
                listOf(
                    ImageGroup(android.R.drawable.bottom_bar),
                    TextGroup("hello", "world"),
                    ImageGroup(android.R.drawable.btn_plus)
                )
            )
            Assert.assertEquals(4, groupCount)
            Assert.assertEquals(1, getChildrenCount(1))
            Assert.assertEquals(2, getChildrenCount(2))
            Assert.assertEquals(1, getChildrenCount(3))

            footerHeader.data = ImageGroup(android.R.drawable.btn_default)
            Assert.assertEquals(5, groupCount)
            Assert.assertEquals(1, getChildrenCount(4))

            bodyAdapter.submitList(listOf(TextGroup("world")))
            Assert.assertEquals(3, groupCount)

            bodyAdapter.submitList(null)
            Assert.assertEquals(2, groupCount)

            footerHeader.data = null
            Assert.assertEquals(1, groupCount)

            headerAdapter.data = null
            Assert.assertEquals(0, groupCount)
        }
    }

    @Test
    fun testMethodGetGroupAndChild() {
        ConcatExpandableListAdapter(
            AssemblySingleDataExpandableListAdapter<TextGroup, Any>(
                itemFactoryList = listOf(TextGroupItemFactory(), TextItemFactory()),
                initData = TextGroup("hello"),
            ),
            AssemblyExpandableListAdapter<Any, Any>(
                itemFactoryList = listOf(
                    TextGroupItemFactory(),
                    TextItemFactory(),
                    ImageGroupItemFactory(),
                    ImageItemFactory()
                ),
                initDataList = listOf(
                    ImageGroup(android.R.drawable.bottom_bar),
                    TextGroup("world"),
                    ImageGroup(android.R.drawable.btn_plus)
                ),
            ),
            AssemblySingleDataExpandableListAdapter<ImageGroup, Any>(
                itemFactoryList = listOf(ImageGroupItemFactory(), ImageItemFactory()),
                initData = ImageGroup(android.R.drawable.alert_dark_frame),
            ),
        ).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getGroup(-1)
            }
            Assert.assertEquals(TextGroup("hello"), getGroup(0))
            Assert.assertEquals(ImageGroup(android.R.drawable.bottom_bar), getGroup(1))
            Assert.assertEquals(TextGroup("world"), getGroup(2))
            Assert.assertEquals(ImageGroup(android.R.drawable.btn_plus), getGroup(3))
            Assert.assertEquals(ImageGroup(android.R.drawable.alert_dark_frame), getGroup(4))
            assertThrow(IllegalArgumentException::class) {
                getGroup(5)
            }

            assertThrow(IndexOutOfBoundsException::class) {
                getChild(-1, 0)
            }
            Assert.assertEquals(Text("hello"), getChild(0, 0))
            Assert.assertEquals(Image(android.R.drawable.bottom_bar), getChild(1, 0))
            Assert.assertEquals(Text("world"), getChild(2, 0))
            Assert.assertEquals(Image(android.R.drawable.btn_plus), getChild(3, 0))
            Assert.assertEquals(Image(android.R.drawable.alert_dark_frame), getChild(4, 0))
            assertThrow(IllegalArgumentException::class) {
                getChild(5, 0)
            }
        }
    }

    @Test
    fun testMethodHasStableIds() {
        ConcatExpandableListAdapter(
            AssemblySingleDataExpandableListAdapter<Text, Any>(
                TextItemFactory()
            )
        ).apply {
            Assert.assertFalse(hasStableIds())
        }

        ConcatExpandableListAdapter(
            ConcatExpandableListAdapter.Config.Builder()
                .setStableIdMode(ConcatExpandableListAdapter.Config.StableIdMode.SHARED_STABLE_IDS)
                .build(),
            AssemblySingleDataExpandableListAdapter<Text, Any>(
                itemFactory = TextItemFactory(),
            ).apply { setHasStableIds(true) }
        ).apply {
            Assert.assertTrue(hasStableIds())
        }

        ConcatExpandableListAdapter(
            ConcatExpandableListAdapter.Config.Builder()
                .setStableIdMode(ConcatExpandableListAdapter.Config.StableIdMode.ISOLATED_STABLE_IDS)
                .build(),
            AssemblySingleDataExpandableListAdapter<Text, Any>(
                itemFactory = TextItemFactory(),
            ).apply { setHasStableIds(true) }
        ).apply {
            Assert.assertTrue(hasStableIds())
        }
    }

    @Test
    fun testMethodFindLocalAdapterAndPosition() {
        val headerAdapter = AssemblySingleDataExpandableListAdapter<Text, Any>(
            itemFactory = TextItemFactory(),
            initData = Text("hello"),
        )
        val bodyAdapter = AssemblyExpandableListAdapter<Any, Any>(
            itemFactoryList = listOf(TextItemFactory(), ImageItemFactory()),
            initDataList = listOf(
                Image(android.R.drawable.bottom_bar),
                Text("world"),
                Image(android.R.drawable.btn_plus)
            ),
        )
        val footerAdapter = AssemblySingleDataExpandableListAdapter<Image, Any>(
            itemFactory = ImageItemFactory(),
            initData = Image(android.R.drawable.alert_dark_frame),
        )
        ConcatExpandableListAdapter(
            headerAdapter,
            bodyAdapter,
            footerAdapter,
        ).apply {
            findLocalAdapterAndPosition(-1).apply {
                Assert.assertSame(headerAdapter, first)
                Assert.assertEquals(-1, second)
            }
            findLocalAdapterAndPosition(0).apply {
                Assert.assertSame(headerAdapter, first)
                Assert.assertEquals(0, second)
            }
            findLocalAdapterAndPosition(1).apply {
                Assert.assertSame(bodyAdapter, first)
                Assert.assertEquals(0, second)
            }
            findLocalAdapterAndPosition(2).apply {
                Assert.assertSame(bodyAdapter, first)
                Assert.assertEquals(1, second)
            }
            findLocalAdapterAndPosition(3).apply {
                Assert.assertSame(bodyAdapter, first)
                Assert.assertEquals(2, second)
            }
            findLocalAdapterAndPosition(4).apply {
                Assert.assertSame(footerAdapter, first)
                Assert.assertEquals(0, second)
            }
            assertThrow(IllegalArgumentException::class) {
                findLocalAdapterAndPosition(5)
            }
        }
    }

    @Test
    fun testNestedAdapterPosition() {
        val count1Adapter =
            AssemblySingleDataExpandableListAdapter<Any, Any>(
                listOf(TextGroupItemFactory(), TextItemFactory()),
                TextGroup()
            )
        val count3Adapter = AssemblyExpandableListAdapter<Any, Any>(
            listOf(
                TextGroupItemFactory(),
                TextItemFactory(),
            ),
            listOf(TextGroup(), Text("hello"), TextGroup())
        )
        val count5Adapter = AssemblyExpandableListAdapter<Any, Any>(
            listOf(
                TextGroupItemFactory(),
                TextItemFactory(),
            ),
            listOf(TextGroup(), Text("hello"), TextGroup(), Text("hello"), TextGroup())
        )
        val count7Adapter = AssemblyExpandableListAdapter<Any, Any>(
            listOf(
                TextGroupItemFactory(),
                TextItemFactory(),
            ),
            listOf(
                TextGroup(),
                Text("hello"),
                TextGroup(),
                Text("hello"),
                TextGroup(),
                Text("hello"),
                TextGroup()
            )
        )
        val concatCount9Adapter =
            ConcatExpandableListAdapter(count1Adapter, count3Adapter, count5Adapter)
        val concatCount11Adapter =
            ConcatExpandableListAdapter(count1Adapter, count3Adapter, count7Adapter)
        val concatCount13Adapter =
            ConcatExpandableListAdapter(count1Adapter, count5Adapter, count7Adapter)
        val concatNestingCount16Adapter = ConcatExpandableListAdapter(
            count1Adapter, ConcatExpandableListAdapter(count3Adapter, count5Adapter), count7Adapter
        )

        Assert.assertEquals("count1Adapter.groupCount", 1, count1Adapter.groupCount)
        Assert.assertEquals("count3Adapter.groupCount", 3, count3Adapter.groupCount)
        Assert.assertEquals("count5Adapter.groupCount", 5, count5Adapter.groupCount)
        Assert.assertEquals("count7Adapter.groupCount", 7, count7Adapter.groupCount)
        Assert.assertEquals("count7Adapter.groupCount", 9, concatCount9Adapter.groupCount)
        Assert.assertEquals("count7Adapter.groupCount", 11, concatCount11Adapter.groupCount)
        Assert.assertEquals("count12Adapter.groupCount", 13, concatCount13Adapter.groupCount)
        Assert.assertEquals("count15Adapter.groupCount", 16, concatNestingCount16Adapter.groupCount)

        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = ListView(context)
        val verifyAdapterPosition: (BaseExpandableListAdapter, Int, Int, Int) -> Unit =
            { adapter, groupPosition, expectedBindingAdapterPosition, expectedAbsoluteAdapterPosition ->
                val groupItemView = adapter.getGroupView(groupPosition, false, null, parent)
                val groupItem = groupItemView.getTag(R.id.aa_tag_item) as Item<*>
                Assert.assertEquals(
                    "count${adapter.groupCount}Adapter. groupPosition(${groupPosition}). item.bindingAdapterPosition",
                    expectedBindingAdapterPosition, groupItem.bindingAdapterPosition
                )
                Assert.assertEquals(
                    "count${adapter.groupCount}Adapter. groupPosition(${groupPosition}). item.absoluteAdapterPosition",
                    expectedAbsoluteAdapterPosition, groupItem.absoluteAdapterPosition
                )

                if (adapter.getChildrenCount(groupPosition) > 0) {
                    val childItemView =
                        adapter.getChildView(groupPosition, 0, false, null, parent)
                    val childItem =
                        childItemView.getTag(R.id.aa_tag_item) as ExpandableChildItem<*, *>
                    Assert.assertEquals(
                        "count${adapter.groupCount}Adapter. groupPosition(${groupPosition}), childPosition(0). item.groupBindingAdapterPosition",
                        expectedBindingAdapterPosition, childItem.groupBindingAdapterPosition
                    )
                    Assert.assertEquals(
                        "count${adapter.groupCount}Adapter. groupPosition(${groupPosition}), childPosition(0). item.groupAbsoluteAdapterPosition",
                        expectedAbsoluteAdapterPosition, childItem.groupAbsoluteAdapterPosition
                    )
                    Assert.assertEquals(
                        "count${adapter.groupCount}Adapter. groupPosition(${groupPosition}), childPosition(0). item.bindingAdapterPosition",
                        0, childItem.bindingAdapterPosition
                    )
                    Assert.assertEquals(
                        "count${adapter.groupCount}Adapter. groupPosition(${groupPosition}), childPosition(0). item.absoluteAdapterPosition",
                        0, childItem.absoluteAdapterPosition
                    )
                }
            }

        /* adapter, position, bindingAdapterPosition, absoluteAdapterPosition */
        verifyAdapterPosition(count1Adapter, 0, 0, 0)

        verifyAdapterPosition(count3Adapter, 0, 0, 0)
        verifyAdapterPosition(count3Adapter, 1, 1, 1)
        verifyAdapterPosition(count3Adapter, 2, 2, 2)

        verifyAdapterPosition(count5Adapter, 0, 0, 0)
        verifyAdapterPosition(count5Adapter, 1, 1, 1)
        verifyAdapterPosition(count5Adapter, 2, 2, 2)
        verifyAdapterPosition(count5Adapter, 3, 3, 3)
        verifyAdapterPosition(count5Adapter, 4, 4, 4)

        verifyAdapterPosition(count7Adapter, 0, 0, 0)
        verifyAdapterPosition(count7Adapter, 1, 1, 1)
        verifyAdapterPosition(count7Adapter, 2, 2, 2)
        verifyAdapterPosition(count7Adapter, 3, 3, 3)
        verifyAdapterPosition(count7Adapter, 4, 4, 4)
        verifyAdapterPosition(count7Adapter, 5, 5, 5)
        verifyAdapterPosition(count7Adapter, 6, 6, 6)

        verifyAdapterPosition(concatCount9Adapter, 0, 0, 0)
        verifyAdapterPosition(concatCount9Adapter, 1, 0, 1)
        verifyAdapterPosition(concatCount9Adapter, 2, 1, 2)
        verifyAdapterPosition(concatCount9Adapter, 3, 2, 3)
        verifyAdapterPosition(concatCount9Adapter, 4, 0, 4)
        verifyAdapterPosition(concatCount9Adapter, 5, 1, 5)
        verifyAdapterPosition(concatCount9Adapter, 6, 2, 6)
        verifyAdapterPosition(concatCount9Adapter, 7, 3, 7)
        verifyAdapterPosition(concatCount9Adapter, 8, 4, 8)

        verifyAdapterPosition(concatCount11Adapter, 0, 0, 0)
        verifyAdapterPosition(concatCount11Adapter, 1, 0, 1)
        verifyAdapterPosition(concatCount11Adapter, 2, 1, 2)
        verifyAdapterPosition(concatCount11Adapter, 3, 2, 3)
        verifyAdapterPosition(concatCount11Adapter, 4, 0, 4)
        verifyAdapterPosition(concatCount11Adapter, 5, 1, 5)
        verifyAdapterPosition(concatCount11Adapter, 6, 2, 6)
        verifyAdapterPosition(concatCount11Adapter, 7, 3, 7)
        verifyAdapterPosition(concatCount11Adapter, 8, 4, 8)
        verifyAdapterPosition(concatCount11Adapter, 9, 5, 9)
        verifyAdapterPosition(concatCount11Adapter, 10, 6, 10)

        verifyAdapterPosition(concatCount13Adapter, 0, 0, 0)
        verifyAdapterPosition(concatCount13Adapter, 1, 0, 1)
        verifyAdapterPosition(concatCount13Adapter, 2, 1, 2)
        verifyAdapterPosition(concatCount13Adapter, 3, 2, 3)
        verifyAdapterPosition(concatCount13Adapter, 4, 3, 4)
        verifyAdapterPosition(concatCount13Adapter, 5, 4, 5)
        verifyAdapterPosition(concatCount13Adapter, 6, 0, 6)
        verifyAdapterPosition(concatCount13Adapter, 7, 1, 7)
        verifyAdapterPosition(concatCount13Adapter, 8, 2, 8)
        verifyAdapterPosition(concatCount13Adapter, 9, 3, 9)
        verifyAdapterPosition(concatCount13Adapter, 10, 4, 10)
        verifyAdapterPosition(concatCount13Adapter, 11, 5, 11)
        verifyAdapterPosition(concatCount13Adapter, 12, 6, 12)

        verifyAdapterPosition(concatNestingCount16Adapter, 0, 0, 0)
        verifyAdapterPosition(concatNestingCount16Adapter, 1, 0, 1)
        verifyAdapterPosition(concatNestingCount16Adapter, 2, 1, 2)
        verifyAdapterPosition(concatNestingCount16Adapter, 3, 2, 3)
        verifyAdapterPosition(concatNestingCount16Adapter, 4, 0, 4)
        verifyAdapterPosition(concatNestingCount16Adapter, 5, 1, 5)
        verifyAdapterPosition(concatNestingCount16Adapter, 6, 2, 6)
        verifyAdapterPosition(concatNestingCount16Adapter, 7, 3, 7)
        verifyAdapterPosition(concatNestingCount16Adapter, 8, 4, 8)
        verifyAdapterPosition(concatNestingCount16Adapter, 9, 0, 9)
        verifyAdapterPosition(concatNestingCount16Adapter, 10, 1, 10)
        verifyAdapterPosition(concatNestingCount16Adapter, 11, 2, 11)
        verifyAdapterPosition(concatNestingCount16Adapter, 12, 3, 12)
        verifyAdapterPosition(concatNestingCount16Adapter, 13, 4, 13)
        verifyAdapterPosition(concatNestingCount16Adapter, 14, 5, 14)
        verifyAdapterPosition(concatNestingCount16Adapter, 15, 6, 15)
    }
}