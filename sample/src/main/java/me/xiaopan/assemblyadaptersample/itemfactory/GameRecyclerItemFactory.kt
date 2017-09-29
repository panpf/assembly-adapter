package me.xiaopan.assemblyadaptersample.itemfactory

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import me.xiaopan.assemblyadapter.AssemblyRecyclerItem
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory
import me.xiaopan.assemblyadaptersample.R
import me.xiaopan.assemblyadaptersample.bean.Game
import me.xiaopan.ssvt.bindView

class GameRecyclerItemFactory(context: Context) : AssemblyRecyclerItemFactory<GameRecyclerItemFactory.GameRecyclerItem>() {

    private val eventListener: EventListener

    init {
        this.eventListener = EventProcessor(context)
    }

    override fun isTarget(data: Any): Boolean {
        return data is Game
    }

    override fun createAssemblyItem(parent: ViewGroup): GameRecyclerItem {
        return GameRecyclerItem(R.layout.list_item_game, parent)
    }

    interface EventListener {
        fun onClickIcon(position: Int, user: Game)
        fun onClickName(position: Int, user: Game)
        fun onClickLike(position: Int, user: Game)
    }

    private class EventProcessor(private val context: Context) : EventListener {

        override fun onClickIcon(position: Int, game: Game) {
            Toast.makeText(context, "瞅这游戏这臭逼样！", Toast.LENGTH_SHORT).show()
        }

        override fun onClickName(position: Int, game: Game) {
            Toast.makeText(context, "原来你叫" + game.name + "啊！", Toast.LENGTH_SHORT).show()
        }

        override fun onClickLike(position: Int, game: Game) {
            Toast.makeText(context, "我也" + game.like + "这游戏！", Toast.LENGTH_SHORT).show()
        }
    }

    inner class GameRecyclerItem(itemLayoutId: Int, parent: ViewGroup) : AssemblyRecyclerItem<Game>(itemLayoutId, parent) {
        val iconImageView: ImageView by bindView(R.id.image_gameListItem_icon)
        val nameTextView: TextView by bindView(R.id.text_gameListItem_name)
        val likeTextView: TextView by bindView(R.id.text_gameListItem_like)

        override fun onConfigViews(context: Context) {
            iconImageView.setOnClickListener { eventListener.onClickIcon(layoutPosition, data) }
            nameTextView.setOnClickListener { eventListener.onClickName(layoutPosition, data) }
            likeTextView.setOnClickListener { eventListener.onClickLike(layoutPosition, data) }
        }

        override fun onSetData(position: Int, game: Game) {
            iconImageView.setImageResource(game.iconResId)
            nameTextView.text = game.name
            likeTextView.text = game.like
        }
    }
}
