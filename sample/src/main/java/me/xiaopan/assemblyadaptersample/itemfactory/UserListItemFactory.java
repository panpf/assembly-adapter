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
import me.xiaopan.assemblyadaptersample.bean.User;

public class UserListItemFactory extends AssemblyItemFactory<UserListItemFactory.UserListItem> {

    private EventListener eventListener;

    public UserListItemFactory(Context context) {
        this.eventListener = new EventProcessor(context);
    }

    @Override
    public boolean isTarget(Object itemObject) {
        return itemObject instanceof User;
    }

    @Override
    public UserListItem createAssemblyItem(ViewGroup parent) {
        return new UserListItem(R.layout.list_item_user, parent);
    }

    public class UserListItem extends AssemblyItem<User> {
        private ImageView headImageView;
        private TextView nameTextView;
        private TextView sexTextView;
        private TextView ageTextView;
        private TextView jobTextView;

        public UserListItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews() {
            headImageView = (ImageView) findViewById(R.id.image_userListItem_head);
            nameTextView = (TextView) findViewById(R.id.text_userListItem_name);
            sexTextView = (TextView) findViewById(R.id.text_userListItem_sex);
            ageTextView = (TextView) findViewById(R.id.text_userListItem_age);
            jobTextView = (TextView) findViewById(R.id.text_userListItem_job);
        }

        @Override
        protected void onConfigViews(Context context) {
            headImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventListener.onClickHead(getPosition(), getData());
                }
            });
            nameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventListener.onClickName(getPosition(), getData());
                }
            });
            sexTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventListener.onClickSex(getPosition(), getData());
                }
            });
            ageTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventListener.onClickAge(getPosition(), getData());
                }
            });
            jobTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventListener.onClickJob(getPosition(), getData());
                }
            });
        }

        @Override
        protected void onSetData(int position, User user) {
            headImageView.setImageResource(user.headResId);
            nameTextView.setText(user.name);
            sexTextView.setText(user.sex);
            ageTextView.setText(user.age);
            jobTextView.setText(user.job);
        }
    }

    public interface EventListener{
        void onClickHead(int position, User user);
        void onClickName(int position, User user);
        void onClickSex(int position, User user);
        void onClickAge(int position, User user);
        void onClickJob(int position, User user);
    }

    private static class EventProcessor implements EventListener {
        private Context context;

        public EventProcessor(Context context) {
            this.context = context;
        }

        @Override
        public void onClickHead(int position, User user) {
            Toast.makeText(context, "别摸我头，讨厌啦！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClickName(int position, User user) {
            Toast.makeText(context, "我就叫"+user.name+"，咋地不服啊！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClickSex(int position, User user) {
            Toast.makeText(context, "我还就是"+user.sex+"个的了，有本事你捅我啊！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClickAge(int position, User user) {
            String message;
            if(user.sex.contains("男") || user.sex.contains("先生")){
                message = "哥今年"+user.age+"岁了，该找媳妇了！";
            }else{
                message = "姐今年"+user.age+"岁了，该找人嫁了！";
            }
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClickJob(int position, User user) {
            Toast.makeText(context, "我是名光荣的"+user.job, Toast.LENGTH_SHORT).show();
        }
    }
}
