package me.xiaopan.assemblyadaptersample.itemfactory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import me.xiaopan.assemblyadapter.AssemblyItem;
import me.xiaopan.assemblyadapter.AssemblyItemFactory;
import me.xiaopan.assemblyadaptersample.R;
import me.xiaopan.assemblyadaptersample.bean.Game;

public class GameListItemFactory extends AssemblyItemFactory<GameListItemFactory.GameListItem> {

    private EventListener eventListener;

    public GameListItemFactory(Context context) {
        this.eventListener = new EventProcessor(context);
    }

    @Override
    public boolean isTarget(Object itemObject) {
        return itemObject instanceof Game;
    }

    @Override
    public GameListItem createAssemblyItem(ViewGroup parent) {
        return new GameListItem(R.layout.list_item_game, parent);
    }

    public interface EventListener{
        void onClickIcon(int position, Game user);
        void onClickName(int position, Game user);
        void onClickLike(int position, Game user);
    }

    private static class EventProcessor implements EventListener {
        private Context context;

        public EventProcessor(Context context) {
            this.context = context;
        }

        @Override
        public void onClickIcon(int position, Game game) {
            Toast.makeText(context, "瞅这游戏这臭逼样！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClickName(int position, Game game) {
            Toast.makeText(context, "原来你叫"+game.name+"啊！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClickLike(int position, Game game) {
            Toast.makeText(context, "我也"+game.like+"这游戏！", Toast.LENGTH_SHORT).show();
        }
    }

    public class GameListItem extends AssemblyItem<Game> {
        private ImageView iconImageView;
        private TextView nameTextView;
        private TextView likeTextView;

        public GameListItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews(View itemView) {
            iconImageView = (ImageView) findViewById(R.id.image_gameListItem_icon);
            nameTextView = (TextView) findViewById(R.id.text_gameListItem_name);
            likeTextView = (TextView) findViewById(R.id.text_gameListItem_like);
        }

        @Override
        protected void onConfigViews(Context context) {
            iconImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventListener.onClickIcon(getPosition(), getData());
                }
            });
            nameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventListener.onClickName(getPosition(), getData());
                }
            });
            likeTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventListener.onClickLike(getPosition(), getData());
                }
            });
        }

        @Override
        protected void onSetData(int position, Game game) {
            iconImageView.setImageResource(game.iconResId);
            nameTextView.setText(game.name);
            likeTextView.setText(game.like);
        }
    }
}
