package me.panpf.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("rawtypes")
public class DataManager {

    @NonNull
    private final Callback callback;
    @NonNull
    private final List<Object> dataList;

    public DataManager(@NonNull Callback callback) {
        this.callback = callback;
        this.dataList = new ArrayList<>();
    }

    public DataManager(@NonNull Callback callback, @Nullable List dataList) {
        this.callback = callback;
        if (dataList != null && dataList.size() > 0) {
            this.dataList = new ArrayList<Object>(dataList);
        } else {
            this.dataList = new ArrayList<>();
        }
    }

    public DataManager(@NonNull Callback callback, @Nullable Object[] dataArray) {
        this.callback = callback;
        if (dataArray != null && dataArray.length > 0) {
            this.dataList = new ArrayList<>(dataArray.length);
            Collections.addAll(this.dataList, dataArray);
        } else {
            this.dataList = new ArrayList<>();
        }
    }

    @Nullable
    public List<Object> getDataList() {
        return dataList.size() > 0 ? Collections.unmodifiableList(dataList) : null;
    }

    public void setDataList(@Nullable List dataList) {
        synchronized (this) {
            this.dataList.clear();
            if (dataList != null) {
                this.dataList.addAll(dataList);
            }
        }
        callback.onItemEnabledChanged();
    }

    public void addAll(@Nullable Collection collection) {
        if (collection == null || collection.size() == 0) {
            return;
        }
        synchronized (this) {
            dataList.addAll(collection);
        }
        callback.onItemEnabledChanged();
    }

    public void addAll(@Nullable Object... items) {
        if (items == null || items.length == 0) {
            return;
        }
        synchronized (this) {
            Collections.addAll(dataList, items);
        }
        callback.onItemEnabledChanged();
    }

    public void insert(@Nullable Object object, int index) {
        if (object == null) {
            return;
        }
        synchronized (this) {
            dataList.add(index, object);
        }
        callback.onItemEnabledChanged();
    }

    public void remove(@NonNull Object object) {
        //noinspection ConstantConditions
        if (object == null) {
            return;
        }
        synchronized (this) {
            dataList.remove(object);
        }
        callback.onItemEnabledChanged();
    }

    public void clear() {
        synchronized (this) {
            dataList.clear();
        }
        callback.onItemEnabledChanged();
    }

    public void sort(@NonNull Comparator comparator) {
        synchronized (this) {
            Collections.sort(dataList, comparator);
        }
        callback.onItemEnabledChanged();
    }

    public int getDataCount() {
        return dataList.size();
    }

    @Nullable
    public Object getData(int position) {
        return position >= 0 && position < dataList.size() ? dataList.get(position) : null;
    }

    public interface Callback {
        void onItemEnabledChanged();
    }
}
