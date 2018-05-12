package me.panpf.adapter.sample.itemfactory

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import me.panpf.adapter.AssemblyItem
import me.panpf.adapter.AssemblyItemFactory
import me.panpf.adapter.ktx.bindView
import me.panpf.adapter.sample.R
import me.panpf.adapter.sample.bean.UserGroup

class UserGroupItemFactory : AssemblyItemFactory<UserGroup>() {

    override fun match(data: Any?): Boolean {
        return data is UserGroup
    }

    override fun createAssemblyItem(parent: ViewGroup): UserGroupItem {
        return UserGroupItem(R.layout.list_group_user, parent)
    }

    inner class UserGroupItem(itemLayoutId: Int, parent: ViewGroup) : AssemblyItem<UserGroup>(itemLayoutId, parent) {
        private val titleTextView: TextView by bindView(R.id.text_userListGroup_name)

        override fun onConfigViews(context: Context) {

        }

        override fun onSetData(position: Int, userGroup: UserGroup?) {
            userGroup ?: return
            if (isExpanded) {
                titleTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_collapse, 0, 0, 0)
            } else {
                titleTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_expand, 0, 0, 0)
            }
            titleTextView.text = userGroup.title
        }
    }
}
