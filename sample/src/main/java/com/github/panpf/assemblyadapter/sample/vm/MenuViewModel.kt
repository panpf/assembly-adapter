package com.github.panpf.assemblyadapter.sample.vm

import android.app.Application
import android.view.MenuItem
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.github.panpf.liveevent.LiveEvent

class MenuViewModel(application: Application) : AndroidViewModel(application) {

    val menuInfoListData = MutableLiveData<List<MenuInfo>>()
    val menuClickEvent = LiveEvent<MenuInfo>()

    class MenuInfo(
        val id: Int,
        val title: String,
        val group: Int = 0,
        val order: Int = 0,
        val showAsAction: Int = MenuItem.SHOW_AS_ACTION_NEVER
    )
}