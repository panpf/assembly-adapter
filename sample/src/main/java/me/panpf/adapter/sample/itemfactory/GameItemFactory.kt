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

class GameItemFactory(context: Context) : AssemblyItemFactory<GameItemFactory.GameItem>() {

    private val eventListener: EventListener

    init {
        this.eventListener = EventProcessor(context)
    }

    override fun isTarget(data: Any): Boolean {
        return data is Game
    }

    override fun createAssemblyItem(parent: ViewGroup): GameItem {
        return GameItem(R.layout.list_item_game, parent)
    }

    interface EventListener {
        fun onClickIcon(position: Int, user: Game)

        fun onClickName(position: Int, user: Game)

        fun onClickLike(position: Int, user: Game)
    }

    private class EventProcessor(private val context: Context) : EventListener {

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

    inner class GameItem(itemLayoutId: Int, parent: ViewGroup) : AssemblyItem<Game>(itemLayoutId, parent) {
        private val iconImageView: ImageView by bindView(R.id.image_gameListItem_icon)
        private val nameTextView: TextView by bindView(R.id.text_gameListItem_name)
        private val likeTextView: TextView by bindView(R.id.text_gameListItem_like)

        override fun onConfigViews(context: Context) {
            iconImageView.setOnClickListener { data.let { it1 -> eventListener.onClickIcon(position, it1) } }

            nameTextView.setOnClickListener { data.let { it1 -> eventListener.onClickName(position, it1) } }

            likeTextView.setOnClickListener { data.let { it1 -> eventListener.onClickLike(position, it1) } }
        }

        override fun onSetData(position: Int, game: Game) {
            iconImageView.setImageResource(game.iconResId)
            nameTextView.text = game.name
            likeTextView.text = game.like
        }
    }
}
