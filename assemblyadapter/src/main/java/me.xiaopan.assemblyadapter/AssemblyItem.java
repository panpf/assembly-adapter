package me.xiaopan.assemblyadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class AssemblyItem<DATA> {
    private View itemView;
    private int position;
    private DATA data;
    private ContentSetter setter;

    public AssemblyItem(int itemLayoutId, ViewGroup parent) {
        this(LayoutInflater.from(parent.getContext()).inflate(itemLayoutId, parent, false));
    }

    @SuppressWarnings("WeakerAccess")
    public AssemblyItem(View itemView) {
        if (itemView == null) {
            throw new IllegalArgumentException("itemView may not be null");
        }
        this.itemView = itemView;
        onFindViews();
        onConfigViews(itemView.getContext());
    }

    /**
     * 设置数据，这个方法由Adapter调用
     *
     * @param position 位置
     * @param data     数据
     */
    public void setData(int position, DATA data) {
        this.position = position;
        this.data = data;
        onSetData(position, data);
    }

    /**
     * 根据id查找View
     */
    public View findViewById(int id) {
        return itemView.findViewById(id);
    }

    /**
     * 根据tag查找View
     */
    @SuppressWarnings("unused")
    public View findViewWithTag(Object tag) {
        return itemView.findViewWithTag(tag);
    }

    /**
     * 专门用来find view，只会执行一次
     */
    protected abstract void onFindViews();

    /**
     * 专门用来配置View，你可在在这里设置View的样式以及尺寸，只会执行一次
     */
    protected abstract void onConfigViews(Context context);

    /**
     * 设置数据
     *
     * @param position 位置
     * @param data     数据
     */
    protected abstract void onSetData(int position, DATA data);

    /**
     * 获取当前Item的View
     */
    @SuppressWarnings("WeakerAccess")
    public final View getItemView() {
        return this.itemView;
    }

    /**
     * 获取当前Item的数据
     */
    public DATA getData() {
        return data;
    }

    /**
     * 获取当前Item的位置
     */
    public int getPosition() {
        return position;
    }

    @SuppressWarnings("WeakerAccess")
    public ContentSetter getSetter() {
        if (setter == null) {
            setter = new ContentSetter(itemView);
        }
        return setter;
    }
}