package me.xiaopan.assemblyadaptersample.itemfactory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import me.xiaopan.assemblyadapter.AssemblyRecyclerItem;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import me.xiaopan.assemblyadaptersample.R;
import me.xiaopan.assemblyadaptersample.bean.Game;

public class GameRecyclerItemFactory extends AssemblyRecyclerItemFactory<GameRecyclerItemFactory.GameRecyclerItem> {

    private EventListener eventListener;

    public GameRecyclerItemFactory(Context context) {
        this.eventListener = new EventProcessor(context);
    }

    @Override
    public boolean isTarget(Object data) {
        return data instanceof Game;
    }

    @Override
    public GameRecyclerItem createAssemblyItem(ViewGroup parent) {
        return new GameRecyclerItem(R.layout.list_item_game, parent);
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

    public class GameRecyclerItem extends AssemblyRecyclerItem<Game> {
        private ImageView iconImageView;
        private TextView nameTextView;
        private TextView likeTextView;

        public GameRecyclerItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews() {
            iconImageView = findViewById(R.id.image_gameListItem_icon);
            nameTextView = findViewById(R.id.text_gameListItem_name);
            likeTextView = findViewById(R.id.text_gameListItem_like);
        }

        @Override
        protected void onConfigViews(Context context) {
            iconImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventListener.onClickIcon(getLayoutPosition(), getData());
                }
            });
            nameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventListener.onClickName(getLayoutPosition(), getData());
                }
            });
            likeTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventListener.onClickLike(getLayoutPosition(), getData());
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
