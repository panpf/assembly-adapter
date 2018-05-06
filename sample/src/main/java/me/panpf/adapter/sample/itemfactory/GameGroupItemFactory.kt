package me.panpf.adapter.sample.itemfactory

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import me.panpf.adapter.AssemblyItem
import me.panpf.adapter.AssemblyItemFactory

import me.panpf.adapter.sample.R
import me.panpf.adapter.sample.bean.GameGroup
import me.panpf.adapter.sample.bindView

class GameGroupItemFactory : AssemblyItemFactory<GameGroup>() {

    override fun match(data: Any?): Boolean {
        return data is GameGroup
    }

    override fun createAssemblyItem(parent: ViewGroup): GameGroupItem {
        return GameGroupItem(R.layout.list_group_game, parent)
    }

    class GameGroupItem(itemLayoutId: Int, parent: ViewGroup) : AssemblyItem<GameGroup>(itemLayoutId, parent) {
        private val titleTextView: TextView by bindView(R.id.text_gameListGroup_name)

        override fun onConfigViews(context: Context) {

        }

        override fun onSetData(position: Int, gameGroup: GameGroup?) {
            if (isExpanded) {
                titleTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_collapse, 0)
            } else {
                titleTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_expand, 0)
            }
            titleTextView.text = gameGroup?.title
        }
    }
}
