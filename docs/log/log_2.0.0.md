Adapter：
>* 新增setDataList(List)方法，方便直接替换数据
>* 新增addAll(List), insert(int, Object), remove(Object), sort(Comparator)方法，去掉append(List)方法
>* loadMoreEnd()方法改为setLoadMoreEnd(true)，方便在不禁用的情况下恢复加载更多

Item：
>* AssemblyItem不再需要传入ItemFactor，因此去掉了getItemFactory()方法，现在你需要访问AssemblyItemFactory中的字段的话，只需把Item设为非静态的即可
>* 去掉了AssemblyItem的第二个泛型，因为不再需要ItemFactory了
>* 重构了AssemblyItem的构造函数，现在有AssemblyItem(int, ViewGroup)和AssemblyItem(View)两种，使用更加方便
>* AssemblyItem中的成员变量都改成private的了，你只能通过其get方法来获取
>* AssemblyItem新增了findViewById(int)和findViewWithTag(Object)方法
>* AssemblyItem.onFindViews(View)方法的View参数去掉了