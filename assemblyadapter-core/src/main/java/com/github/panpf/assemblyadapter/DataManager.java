package com.github.panpf.assemblyadapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DataManager<DATA> {

    @NonNull
    private final Callback callback;
    @NonNull
    private final List<DATA> dataList = new ArrayList<>();

    public DataManager(@NonNull Callback callback) {
        this.callback = callback;
    }

    public DataManager(@NonNull Callback callback, @Nullable List<DATA> dataList) {
        this.callback = callback;
        if (dataList != null && dataList.size() > 0) {
            this.dataList.addAll(dataList);
        }
    }

    public DataManager(@NonNull Callback callback, @Nullable DATA[] dataArray) {
        this.callback = callback;
        if (dataArray != null && dataArray.length > 0) {
            Collections.addAll(this.dataList, dataArray);
        }
    }

    @NonNull
    public List<DATA> getDataListSnapshot() {
        return new ArrayList<>(dataList);
    }

    public void setDataList(@Nullable List<DATA> datas) {
        synchronized (this) {
            this.dataList.clear();
            if (datas != null) {
                this.dataList.addAll(datas);
            }
        }
        callback.onDataListChanged();
    }

    public boolean addData(@Nullable DATA data) {
        boolean result;
        synchronized (this) {
            result = dataList.add(data);
        }
        if (result) {
            callback.onDataListChanged();
        }
        return result;
    }

    public void addData(int index, @Nullable DATA data) {
        synchronized (this) {
            dataList.add(index, data);
        }
        callback.onDataListChanged();
    }

    public boolean addAllData(@Nullable Collection<DATA> datas) {
        boolean result = false;
        if (datas != null && datas.size() != 0) {
            synchronized (this) {
                result = dataList.addAll(datas);
            }
        }
        if (result) {
            callback.onDataListChanged();
        }
        return result;
    }

    @SafeVarargs
    public final boolean addAllData(@Nullable DATA... datas) {
        boolean result = false;
        if (datas != null && datas.length != 0) {
            synchronized (this) {
                Collections.addAll(dataList, datas);
            }
            result = true;
        }
        if (result) {
            callback.onDataListChanged();
        }
        return result;
    }

    public boolean removeData(@Nullable DATA data) {
        boolean result;
        synchronized (this) {
            result = dataList.remove(data);
        }
        if (result) {
            callback.onDataListChanged();
        }
        return result;
    }

    @Nullable
    public DATA removeData(int index) {
        DATA data;
        synchronized (this) {
            data = dataList.remove(index);
        }
        callback.onDataListChanged();
        return data;
    }

    public boolean removeAllData(@Nullable Collection<DATA> datas) {
        boolean result = false;
        if (datas != null && datas.size() > 0) {
            synchronized (this) {
                result = dataList.removeAll(datas);
            }
        }
        if (result) {
            callback.onDataListChanged();
        }
        return result;
    }

    public void clearData() {
        synchronized (this) {
            dataList.clear();
        }
        callback.onDataListChanged();
    }

    public void sortData(@NonNull Comparator<DATA> comparator) {
        synchronized (this) {
            Collections.sort(dataList, comparator);
        }
        callback.onDataListChanged();
    }

    public int getDataCount() {
        return dataList.size();
    }

    @Nullable
    public DATA getData(int position) {
        return position >= 0 && position < dataList.size() ? dataList.get(position) : null;
    }

    public interface Callback {
        void onDataListChanged();
    }
}
