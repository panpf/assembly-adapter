package me.xiaopan.assemblyadaptersample.itemfactory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import me.xiaopan.assemblyadaptersample.R;
import me.xiaopan.assemblyadaptersample.bean.User;
import me.xiaopan.assemblyexpandableadapter.AssemblyChildItem;
import me.xiaopan.assemblyexpandableadapter.AssemblyChildItemFactory;

public class UserChildItemFactory extends AssemblyChildItemFactory<UserChildItemFactory.UserChildItem> {

    private EventListener eventListener;

    public UserChildItemFactory(Context context) {
        this.eventListener = new EventProcessor(context);
    }

    @Override
    public Class<?> getBeanClass() {
        return User.class;
    }

    @Override
    public UserChildItem createAssemblyItem(ViewGroup parent) {
        return new UserChildItem(parent, this);
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

    public static class UserChildItem extends AssemblyChildItem<User, UserChildItemFactory> {
        private ImageView headImageView;
        private TextView nameTextView;
        private TextView sexTextView;
        private TextView ageTextView;
        private TextView jobTextView;

        protected UserChildItem(ViewGroup parent, UserChildItemFactory factory) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_user, parent, false), factory);
        }

        @Override
        protected void onFindViews(View convertView) {
            headImageView = (ImageView) convertView.findViewById(R.id.image_userListItem_head);
            nameTextView = (TextView) convertView.findViewById(R.id.text_userListItem_name);
            sexTextView = (TextView) convertView.findViewById(R.id.text_userListItem_sex);
            ageTextView = (TextView) convertView.findViewById(R.id.text_userListItem_age);
            jobTextView = (TextView) convertView.findViewById(R.id.text_userListItem_job);
        }

        @Override
        protected void onConfigViews(Context context) {
            headImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getItemFactory().eventListener.onClickHead(getChildPosition(), getData());
                }
            });
            nameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getItemFactory().eventListener.onClickName(getChildPosition(), getData());
                }
            });
            sexTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getItemFactory().eventListener.onClickSex(getChildPosition(), getData());
                }
            });
            ageTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getItemFactory().eventListener.onClickAge(getChildPosition(), getData());
                }
            });
            jobTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getItemFactory().eventListener.onClickJob(getChildPosition(), getData());
                }
            });
        }

        @Override
        protected void onSetData(int groupPosition, int childPosition, boolean isLastChild, User user) {
            headImageView.setImageResource(user.headResId);
            nameTextView.setText(user.name);
            sexTextView.setText(user.sex);
            ageTextView.setText(user.age);
            jobTextView.setText(user.job);
        }
    }
}
