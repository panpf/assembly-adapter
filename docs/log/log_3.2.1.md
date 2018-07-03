新功能：
* 新增了 [ArrayPagerAdapter] 和 [FragmentArrayStatePagerAdapter]
* 所有 PagerAdapter 的子类支持调用 notifyDataSetChanged() 方法时 getItemPosition(Object) 方法返回 POSITION_NONE，只需通过 setEnabledPositionNoneOnNotifyDataSetChanged(boolean) 方法开启即可


[ArrayPagerAdapter]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/pager/ArrayPagerAdapter.java
[FragmentArrayStatePagerAdapter]: https://github.com/panpf/assembly-adapter/blob/master/assembly-adapter/src/main/java/me/panpf/adapter/pager/FragmentArrayStatePagerAdapter.java