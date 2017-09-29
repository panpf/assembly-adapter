package me.xiaopan.assemblyadaptersample.itemfactory

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView

import me.xiaopan.assemblyadapter.AssemblyGroupItem
import me.xiaopan.assemblyadapter.AssemblyGroupItemFactory
import me.xiaopan.assemblyadaptersample.R
import me.xiaopan.assemblyadaptersample.bean.UserGroup
import me.xiaopan.ssvt.bindView

class UserGroupItemFactory : AssemblyGroupItemFactory<UserGroupItemFactory.UserGroupItem>() {

    override fun isTarget(data: Any): Boolean {
        return data is UserGroup
    }

    override fun createAssemblyItem(parent: ViewGroup): UserGroupItem {
        return UserGroupItem(R.layout.list_group_user, parent)
    }

    inner class UserGroupItem(itemLayoutId: Int, parent: ViewGroup) : AssemblyGroupItem<UserGroup>(itemLayoutId, parent) {
        val titleTextView: TextView by bindView(R.id.text_userListGroup_name)

        override fun onConfigViews(context: Context) {

        }

        override fun onSetData(position: Int, userGroup: UserGroup) {
            if (isExpanded) {
                titleTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_collapse, 0, 0, 0)
            } else {
                titleTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_expand, 0, 0, 0)
            }
            titleTextView.text = userGroup.title
        }
    }
}
