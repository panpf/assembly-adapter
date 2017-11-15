package me.xiaopan.assemblyadaptersample.itemfactory

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import me.xiaopan.assemblyadapter.AssemblyRecyclerItem
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory
import me.xiaopan.assemblyadaptersample.R
import me.xiaopan.assemblyadaptersample.bean.User
import me.xiaopan.assemblyadaptersample.bindView

class UserRecyclerItemFactory(context: Context) : AssemblyRecyclerItemFactory<UserRecyclerItemFactory.UserRecyclerItem>() {

    private val eventListener: EventListener

    init {
        this.eventListener = EventProcessor(context)
    }

    override fun isTarget(data: Any): Boolean {
        return data is User
    }

    override fun createAssemblyItem(parent: ViewGroup): UserRecyclerItem {
        return UserRecyclerItem(R.layout.list_item_user, parent)
    }

    interface EventListener {
        fun onClickHead(position: Int, user: User)
        fun onClickName(position: Int, user: User)
        fun onClickSex(position: Int, user: User)
        fun onClickAge(position: Int, user: User)
        fun onClickJob(position: Int, user: User)
    }

    private class EventProcessor(private val context: Context) : EventListener {

        override fun onClickHead(position: Int, user: User) {
            Toast.makeText(context, "别摸我头，讨厌啦！", Toast.LENGTH_SHORT).show()
        }

        override fun onClickName(position: Int, user: User) {
            Toast.makeText(context, "我就叫" + user.name + "，咋地不服啊！", Toast.LENGTH_SHORT).show()
        }

        override fun onClickSex(position: Int, user: User) {
            Toast.makeText(context, "我还就是" + user.sex + "个的了，有本事你捅我啊！", Toast.LENGTH_SHORT).show()
        }

        override fun onClickAge(position: Int, user: User) {
            val message: String
            if ((user.sex ?: "").contains("男") || (user.sex ?: "").contains("先生")) {
                message = "哥今年" + user.age + "岁了，该找媳妇了！"
            } else {
                message = "姐今年" + user.age + "岁了，该找人嫁了！"
            }
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        override fun onClickJob(position: Int, user: User) {
            Toast.makeText(context, "我是名光荣的" + user.job, Toast.LENGTH_SHORT).show()
        }
    }

    inner class UserRecyclerItem(itemLayoutId: Int, parent: ViewGroup) : AssemblyRecyclerItem<User>(itemLayoutId, parent) {
        val headImageView: ImageView by bindView(R.id.image_userListItem_head)
        val nameTextView: TextView by bindView(R.id.text_userListItem_name)
        val sexTextView: TextView by bindView(R.id.text_userListItem_sex)
        val ageTextView: TextView by bindView(R.id.text_userListItem_age)
        val jobTextView: TextView by bindView(R.id.text_userListItem_job)

        override fun onConfigViews(context: Context) {
            headImageView.setOnClickListener { eventListener.onClickHead(layoutPosition, data) }
            nameTextView.setOnClickListener { eventListener.onClickName(layoutPosition, data) }
            sexTextView.setOnClickListener { eventListener.onClickSex(layoutPosition, data) }
            ageTextView.setOnClickListener { eventListener.onClickAge(layoutPosition, data) }
            jobTextView.setOnClickListener { eventListener.onClickJob(layoutPosition, data) }
        }

        override fun onSetData(position: Int, user: User) {
            headImageView.setImageResource(user.headResId)
            nameTextView.text = user.name
            sexTextView.text = user.sex
            ageTextView.text = user.age
            jobTextView.text = user.job
        }
    }
}
