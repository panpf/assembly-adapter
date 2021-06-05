package com.github.panpf.assemblyadapter.sample.old.ui.list

import android.view.ViewGroup
import android.widget.TextView
import me.panpf.adapter.AssemblyItem
import me.panpf.adapter.AssemblyItemFactory
import me.panpf.adapter.ktx.bindView
import com.github.panpf.assemblyadapter.sample.old.R
import com.github.panpf.assemblyadapter.sample.old.bean.GameGroup

class GameGroupItem(itemLayoutId: Int, parent: ViewGroup) : AssemblyItem<GameGroup>(itemLayoutId, parent) {
    private val titleTextView: TextView by bindView(R.id.gameGroupItem_nameText)

    override fun onSetData(position: Int, gameGroup: GameGroup?) {
        if (isExpanded) {
            titleTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_collapse, 0)
        } else {
            titleTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_expand, 0)
        }
        titleTextView.text = gameGroup?.title
    }

    class Factory : AssemblyItemFactory<GameGroup>() {

        override fun match(data: Any?): Boolean {
            return data is GameGroup
        }

        override fun createAssemblyItem(parent: ViewGroup): GameGroupItem {
            return GameGroupItem(R.layout.item_game_group, parent)
        }
    }
}
