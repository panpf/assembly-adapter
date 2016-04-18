package me.xiaopan.assemblyadaptersample.itemfactory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import me.xiaopan.assemblyadapter.AssemblyChildItem;
import me.xiaopan.assemblyadapter.AssemblyChildItemFactory;
import me.xiaopan.assemblyadaptersample.R;
import me.xiaopan.assemblyadaptersample.bean.Game;

public class GameChildItemFactory extends AssemblyChildItemFactory<GameChildItemFactory.GameChildItem> {

    private EventListener eventListener;

    public GameChildItemFactory(Context context) {
        this.eventListener = new EventProcessor(context);
    }

    @Override
    public boolean isTarget(Object itemObject) {
        return itemObject instanceof Game;
    }

    @Override
    public GameChildItem createAssemblyItem(ViewGroup parent) {
        return new GameChildItem(inflateView(R.layout.list_item_game, parent), this);
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

    public static class GameChildItem extends AssemblyChildItem<Game, GameChildItemFactory> {
        private ImageView iconImageView;
        private TextView nameTextView;
        private TextView likeTextView;

        protected GameChildItem(View convertView, GameChildItemFactory factory) {
            super(convertView, factory);
        }

        @Override
        protected void onFindViews(View convertView) {
            iconImageView = (ImageView) convertView.findViewById(R.id.image_gameListItem_icon);
            nameTextView = (TextView) convertView.findViewById(R.id.text_gameListItem_name);
            likeTextView = (TextView) convertView.findViewById(R.id.text_gameListItem_like);
        }

        @Override
        protected void onConfigViews(Context context) {
            iconImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getItemFactory().eventListener.onClickIcon(getChildPosition(), getData());
                }
            });
            nameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getItemFactory().eventListener.onClickName(getChildPosition(), getData());
                }
            });
            likeTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getItemFactory().eventListener.onClickLike(getChildPosition(), getData());
                }
            });
        }

        @Override
        protected void onSetData(int groupPosition, int childPosition, boolean isLastChild, Game game) {
            iconImageView.setImageResource(game.iconResId);
            nameTextView.setText(game.name);
            likeTextView.setText(game.like);
        }
    }
}
