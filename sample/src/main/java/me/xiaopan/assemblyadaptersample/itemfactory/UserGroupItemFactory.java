package me.xiaopan.assemblyadaptersample.itemfactory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.xiaopan.assemblyadapter.AssemblyGroupItem;
import me.xiaopan.assemblyadapter.AssemblyGroupItemFactory;
import me.xiaopan.assemblyadaptersample.R;
import me.xiaopan.assemblyadaptersample.bean.UserGroup;

public class UserGroupItemFactory extends AssemblyGroupItemFactory<UserGroupItemFactory.UserGroupItem>{

    @Override
    public boolean isTarget(Object itemObject) {
        return itemObject instanceof UserGroup;
    }

    @Override
    public UserGroupItem createAssemblyItem(ViewGroup parent) {
        return new UserGroupItem(inflateView(R.layout.list_group_user, parent));
    }

    public class UserGroupItem extends AssemblyGroupItem<UserGroup> {
        private TextView titleTextView;

        protected UserGroupItem(View convertView) {
            super(convertView);
        }

        @Override
        protected void onFindViews(View convertView) {
            titleTextView = (TextView) convertView.findViewById(R.id.text_userListGroup_name);
        }

        @Override
        protected void onConfigViews(Context context) {

        }

        @Override
        protected void onSetData(int groupPosition, boolean isExpanded, UserGroup userGroup) {
            if(isExpanded){
                titleTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_collapse, 0, 0, 0);
            }else{
                titleTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_expand, 0, 0, 0);
            }
            titleTextView.setText(userGroup.title);
        }
    }
}
