package me.xiaopan.assemblyadaptersample.itemfactory;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import me.xiaopan.assemblyadapter.AssemblyGroupItem;
import me.xiaopan.assemblyadapter.AssemblyGroupItemFactory;
import me.xiaopan.assemblyadaptersample.R;
import me.xiaopan.assemblyadaptersample.bean.GameGroup;

public class GameGroupItemFactory extends AssemblyGroupItemFactory<GameGroupItemFactory.GameGroupItem>{

    @Override
    public boolean isTarget(Object data) {
        return data instanceof GameGroup;
    }

    @Override
    public GameGroupItem createAssemblyItem(ViewGroup parent) {
        return new GameGroupItem(R.layout.list_group_game, parent);
    }

    public static class GameGroupItem extends AssemblyGroupItem<GameGroup> {
        private TextView titleTextView;

        public GameGroupItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews() {
            titleTextView = (TextView) findViewById(R.id.text_gameListGroup_name);
        }

        @Override
        protected void onConfigViews(Context context) {

        }

        @Override
        protected void onSetData(int position, GameGroup gameGroup) {
            if(isExpanded()){
                titleTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_collapse, 0);
            }else{
                titleTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_expand, 0);
            }
            titleTextView.setText(gameGroup.title);
        }
    }
}
