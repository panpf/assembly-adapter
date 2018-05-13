更新日志如下：
>* 构造函数增加数组支持
>* ItemFactory增加getAdapter()方法
>* ItemFactory中新增isTarget(Object)方法替代isAssignableFrom(Object)
>* 加载更多尾巴支持显示THE END

使用1.10版本需要你对已有的代码做出修改：
>* AssembleItem的getBeanClass()方法已经删除，新增了isTarget()方法，可参考首页readme中的示例