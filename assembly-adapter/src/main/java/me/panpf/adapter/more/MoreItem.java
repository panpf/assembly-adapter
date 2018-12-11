package me.panpf.adapter.more;

import androidx.annotation.NonNull;
import android.view.View;

import me.panpf.adapter.Item;

public interface MoreItem<DATA> extends Item<DATA> {
    /**
     * 获取错误重试View，实现点击重试功能
     */
    @NonNull
    View getErrorRetryView();

    /**
     * 显示加载中状态
     */
    void showLoading();

    /**
     * 显示错误重试状态
     */
    void showErrorRetry();

    /**
     * 显示全部加载完毕已结束状态
     */
    void showEnd();
}
