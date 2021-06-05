package com.github.panpf.assemblyadapter.sample.old.ui.list

import android.view.ViewGroup
import android.widget.TextView
import me.panpf.adapter.AssemblyItem
import me.panpf.adapter.AssemblyItemFactory
import me.panpf.adapter.ktx.bindView
import com.github.panpf.assemblyadapter.sample.old.R
import com.github.panpf.assemblyadapter.sample.old.bean.UserGroup

class UserGroupItem(itemLayoutId: Int, parent: ViewGroup) : AssemblyItem<UserGroup>(itemLayoutId, parent) {
    private val titleTextView: TextView by bindView(R.id.userGroupItem_nameText)

    override fun onSetData(position: Int, userGroup: UserGroup?) {
        userGroup ?: return
        if (isExpanded) {
            titleTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_collapse, 0, 0, 0)
        } else {
            titleTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_expand, 0, 0, 0)
        }
        titleTextView.text = userGroup.title
    }

    class Factory : AssemblyItemFactory<UserGroup>() {

        override fun match(data: Any?): Boolean {
            return data is UserGroup
        }

        override fun createAssemblyItem(parent: ViewGroup): UserGroupItem {
            return UserGroupItem(R.layout.item_user_group, parent)
        }
    }
}
