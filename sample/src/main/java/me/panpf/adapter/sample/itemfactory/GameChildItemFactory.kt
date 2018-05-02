package me.panpf.adapter.sample.itemfactory

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import me.panpf.adapter.AssemblyItem
import me.panpf.adapter.AssemblyItemFactory
import me.panpf.adapter.sample.R
import me.panpf.adapter.sample.bean.Game
import me.panpf.adapter.sample.bindView

class GameChildItemFactory(context: Context) : AssemblyItemFactory<GameChildItemFactory.GameChildItem>() {

    private val listener: GameChildItemListener

    init {
        this.listener = GameChildItemListenerImpl(context)
    }

    override fun isTarget(data: Any?): Boolean {
        return data is Game
    }

    override fun createAssemblyItem(parent: ViewGroup): GameChildItem {
        return GameChildItem(R.layout.list_item_game, parent)
    }

    interface GameChildItemListener {
        fun onClickIcon(position: Int, user: Game)

        fun onClickName(position: Int, user: Game)

        fun onClickLike(position: Int, user: Game)
    }

    private class GameChildItemListenerImpl(private val context: Context) : GameChildItemListener {

        override fun onClickIcon(position: Int, user: Game) {
            Toast.makeText(context, "瞅这游戏这臭逼样！", Toast.LENGTH_SHORT).show()
        }

        override fun onClickName(position: Int, user: Game) {
            Toast.makeText(context, "原来你叫" + user.name + "啊！", Toast.LENGTH_SHORT).show()
        }

        override fun onClickLike(position: Int, user: Game) {
            Toast.makeText(context, "我也" + user.like + "这游戏！", Toast.LENGTH_SHORT).show()
        }
    }

    inner class GameChildItem(itemLayoutId: Int, parent: ViewGroup) : AssemblyItem<Game>(itemLayoutId, parent) {
        private val iconImageView: ImageView by bindView(R.id.image_gameListItem_icon)
        private val nameTextView: TextView by bindView(R.id.text_gameListItem_name)
        private val likeTextView: TextView by bindView(R.id.text_gameListItem_like)

        override fun onConfigViews(context: Context) {
            iconImageView.setOnClickListener { data?.let { it1 -> listener.onClickIcon(position, it1) } }
            nameTextView.setOnClickListener { data?.let { it1 -> listener.onClickName(position, it1) } }
            likeTextView.setOnClickListener { data?.let { it1 -> listener.onClickLike(position, it1) } }
        }

        override fun onSetData(position: Int, game: Game?) {
            game ?: return
            iconImageView.setImageResource(game.iconResId)
            nameTextView.text = game.name
            likeTextView.text = game.like
        }
    }
}
