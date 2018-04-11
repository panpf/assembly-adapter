package me.panpf.adapter.sample.itemfactory

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import me.panpf.adapter.AssemblyItem
import me.panpf.adapter.AssemblyItemFactory
import me.panpf.adapter.sample.R
import me.panpf.adapter.sample.bean.User
import me.panpf.adapter.sample.bindView

class UserChildItemFactory(context: Context) : AssemblyItemFactory<UserChildItemFactory.UserChildItem>() {

    private val eventListener: EventListener

    init {
        this.eventListener = EventProcessor(context)
    }

    override fun isTarget(data: Any?): Boolean {
        return data is User
    }

    override fun createAssemblyItem(parent: ViewGroup): UserChildItem {
        return UserChildItem(R.layout.list_item_user, parent)
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
            val message: String = if ((user.sex ?: "").contains("男") || (user.sex ?: "").contains("先生")) {
                "哥今年" + user.age + "岁了，该找媳妇了！"
            } else {
                "姐今年" + user.age + "岁了，该找人嫁了！"
            }
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        override fun onClickJob(position: Int, user: User) {
            Toast.makeText(context, "我是名光荣的" + user.job, Toast.LENGTH_SHORT).show()
        }
    }

    inner class UserChildItem(itemLayoutId: Int, parent: ViewGroup) : AssemblyItem<User>(itemLayoutId, parent) {
        private val headImageView: ImageView by bindView(R.id.image_userListItem_head)
        private val nameTextView: TextView by bindView(R.id.text_userListItem_name)
        private val sexTextView: TextView by bindView(R.id.text_userListItem_sex)
        private val ageTextView: TextView by bindView(R.id.text_userListItem_age)
        private val jobTextView: TextView by bindView(R.id.text_userListItem_job)

        override fun onConfigViews(context: Context) {
            headImageView.setOnClickListener { data?.let { it1 -> eventListener.onClickHead(position, it1) } }
            nameTextView.setOnClickListener { data?.let { it1 -> eventListener.onClickName(position, it1) } }
            sexTextView.setOnClickListener { data?.let { it1 -> eventListener.onClickSex(position, it1) } }
            ageTextView.setOnClickListener { data?.let { it1 -> eventListener.onClickAge(position, it1) } }
            jobTextView.setOnClickListener { data?.let { it1 -> eventListener.onClickJob(position, it1) } }
        }

        override fun onSetData(position: Int, user: User?) {
            if(user != null) headImageView.setImageResource(user.headResId) else headImageView.setImageDrawable(null)
            nameTextView.text = user?.name
            sexTextView.text = user?.sex
            ageTextView.text = user?.age
            jobTextView.text = user?.job
        }
    }
}
