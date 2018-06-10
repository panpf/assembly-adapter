package me.panpf.adapter.sample.item

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import me.panpf.adapter.AssemblyItem
import me.panpf.adapter.AssemblyItemFactory
import me.panpf.adapter.ktx.bindView
import me.panpf.adapter.sample.R
import me.panpf.adapter.sample.bean.User

class UserItem(itemLayoutId: Int, parent: ViewGroup) : AssemblyItem<User>(itemLayoutId, parent) {
    private val headImageView: ImageView by bindView(R.id.userItem_headImage)
    private val nameTextView: TextView by bindView(R.id.userItem_nameText)
    private val sexTextView: TextView by bindView(R.id.userItem_sexText)
    private val ageTextView: TextView by bindView(R.id.userItem_ageText)
    private val jobTextView: TextView by bindView(R.id.userItem_jobText)

    override fun onSetData(position: Int, user: User?) {
        user ?: return
        headImageView.setImageResource(user.headResId)
        nameTextView.text = user.name
        sexTextView.text = user.sex
        ageTextView.text = user.age
        jobTextView.text = user.job
    }

    class Factory : AssemblyItemFactory<User>() {
        init {
            setOnViewClickListener(R.id.userItem_headImage) { context, view, position, positionInPart, data ->
                Toast.makeText(context, "别摸我头，讨厌啦！", Toast.LENGTH_SHORT).show()
            }
            setOnViewClickListener(R.id.userItem_nameText) { context, view, position, positionInPart, data ->
                Toast.makeText(context, "我就叫" + data?.name + "，咋地不服啊！", Toast.LENGTH_SHORT).show()
            }
            setOnViewClickListener(R.id.userItem_sexText) { context, view, position, positionInPart, data ->
                Toast.makeText(context, "我还就是" + data?.sex + "个的了，有本事你捅我啊！", Toast.LENGTH_SHORT).show()
            }
            setOnViewClickListener(R.id.userItem_ageText) { context, view, position, positionInPart, data ->
                val message: String = if ((data?.sex
                                ?: "").contains("男") || (data?.sex ?: "").contains("先生")) {
                    "哥今年" + data?.age + "岁了，该找媳妇了！"
                } else {
                    "姐今年" + data?.age + "岁了，该找人嫁了！"
                }
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
            setOnViewClickListener(R.id.userItem_jobText) { context, view, position, positionInPart, data ->
                Toast.makeText(context, "我是名光荣的" + data?.job, Toast.LENGTH_SHORT).show()
            }
        }

        override fun match(data: Any?): Boolean {
            return data is User
        }

        override fun createAssemblyItem(parent: ViewGroup): AssemblyItem<User> {
            return UserItem(R.layout.item_user, parent)
        }
    }
}
