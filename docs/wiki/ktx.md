# 在 Kotlin 中使用 bindview

首先需要额外导入 assemblyadapeter-ktx，模块，请参考首页 README

然后就可以使用了，如下：
```kotlin
class AppItem(itemLayoutId: Int, parent: ViewGroup) : AssemblyItem<AppInfo>(itemLayoutId, parent) {
    private val iconImageView: SketchImageView by bindView(R.id.appItem_iconImage)
    private val nameTextView: TextView by bindView(R.id.appItem_nameText)
}
``` 
