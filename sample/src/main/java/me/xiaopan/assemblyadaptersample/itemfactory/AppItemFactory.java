package me.xiaopan.assemblyadaptersample.itemfactory;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import me.xiaopan.assemblyadapter.AssemblyRecyclerItem;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import me.xiaopan.assemblyadaptersample.R;
import me.xiaopan.assemblyadaptersample.bean.AppInfo;
import me.xiaopan.sketch.SketchImageView;

public class AppItemFactory extends AssemblyRecyclerItemFactory<AppItemFactory.AppItem> {
    @Override
    public boolean isTarget(Object o) {
        return o instanceof AppInfo;
    }

    @Override
    public AppItem createAssemblyItem(ViewGroup viewGroup) {
        return new AppItem(R.layout.list_item_app, viewGroup);
    }

    public class AppItem extends AssemblyRecyclerItem<AppInfo> {
        SketchImageView iconImageView;
        TextView nameTextView;

        public AppItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews() {
            iconImageView = findViewById(R.id.image_installedApp_icon);
            nameTextView = findViewById(R.id.text_installedApp_name);
        }

        @Override
        protected void onConfigViews(Context context) {
//            iconImageView.setOptionsByName(ImageOptions.ROUND_RECT);
//
//            ImageShaper imageShaper = iconImageView.getOptions().getImageShaper();
//            if (imageShaper instanceof RoundRectImageShaper) {
//                RoundRectImageShaper roundRectImageShaper = (RoundRectImageShaper) imageShaper;
//                iconImageView.setImageShape(SketchImageView.ImageShape.ROUNDED_RECT);
//                iconImageView.setImageShapeCornerRadius(roundRectImageShaper.getOuterRadii());
//            }
        }

        @Override
        protected void onSetData(int i, AppInfo appInfo) {
            if (appInfo.isTempInstalled()) {
                iconImageView.displayInstalledAppIcon(appInfo.getId(), appInfo.getVersionCode());
            } else {
                iconImageView.displayImage(appInfo.getApkFilePath());
            }
            nameTextView.setText(appInfo.getName());
        }
    }
}
