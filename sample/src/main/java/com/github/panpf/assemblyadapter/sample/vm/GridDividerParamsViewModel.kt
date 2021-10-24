package com.github.panpf.assemblyadapter.sample.vm

import android.app.Application
import android.preference.PreferenceManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.panpf.assemblyadapter.sample.base.LifecycleAndroidViewModel
import com.github.panpf.assemblyadapter.sample.bean.GridDividerParams
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GridDividerParamsViewModel(application: Application, private val name: String) :
    LifecycleAndroidViewModel(application) {

    class Factory(private val application: Application, private val name: String) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return GridDividerParamsViewModel(application, name) as T
        }
    }

    val dividerParamsData = MutableLiveData<GridDividerParams>()
    private val json = Json {
        ignoreUnknownKeys = true
    }

    init {
        val preference = PreferenceManager.getDefaultSharedPreferences(application)

        val dividerParams = preference.getString(name, null)
            ?.let { paramsJson ->
                json.decodeFromString<GridDividerParams>(paramsJson)
            }
            ?: GridDividerParams()
        dividerParamsData.postValue(dividerParams)

        dividerParamsData.observe(this) { newDividerParams ->
            if (newDividerParams != null) {
                preference.edit().putString(name, json.encodeToString(newDividerParams)).apply()
            } else {
                preference.edit().remove(name).apply()
            }
        }
    }
}