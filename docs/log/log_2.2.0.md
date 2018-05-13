Adapter：
>* ``新增``. AssemblyAdapter、AssemblyRecyclerAdapter、AssemblyExpandableAdapter、AssemblyPagerAdapter等Adapter增加setNotifyOnChange(boolean)方法，可控制数据改变时是否立即刷新列表，默认为true。当你需要连续多次修改数据的时候，你应该将notifyOnChange设为false，然后在最后主动调用notifyDataSetChanged()刷新列表，最后再将notifyOnChange设为true
>* ``新增``. AssemblyPagerAdapter新增addHeaderItem()和addFooterItem()方法返回FixedPagerItemInfo，现在你可以控制头或者尾巴了
>* ``新增``. AssemblyPagerAdapter新增add()、remove()、clear()、setDataList()等方法，现在你可以更方便的控制数据了

header、footer：
>* ``新增``. FixedItemInfo、FixedRecyclerItemInfo、FixedGroupItemInfo、FixedPagerItemInfo等增加setEnabled(boolean)方法，现在你可以通过这个方法直接控制隐藏或显示header、footer