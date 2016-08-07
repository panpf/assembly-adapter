Adapter：
>* public AssemblyPagerAdapter(FragmentManager fm, Object[] dataArray) 去掉FragmentManager
>* 修复AssemblyFragmentStatePagerAdapter、AssemblyFragmentPagerAdapter、AssemblyPagerAdapter这三个类的addItemFactory()方法检查出异常时没有return的BUG

ItemFactory：
>* AssemblyFragmentItemFactory、AssemblyPagerItemFactory增加setAdapter(PagerAdapter)和getAdapter()方法
>* 所有ItemFactory的setAdapter方法的访问权限改为包级

FixedItemInfo：
>* 所有FixedItemInfo的setData(Object)方法增加刷新数据功能