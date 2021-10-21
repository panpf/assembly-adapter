package com.github.panpf.assemblyadapter.sample.vm

import android.app.Application
import android.preference.PreferenceManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.panpf.assemblyadapter.sample.base.LifecycleAndroidViewModel
import com.github.panpf.assemblyadapter.sample.bean.LinearDividerParams
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LinearDividerParamsViewModel(application: Application, private val name: String) :
    LifecycleAndroidViewModel(application) {

    class Factory(private val application: Application, private val name: String) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LinearDividerParamsViewModel(application, name) as T
        }
    }

    val dividerParamsData = MutableLiveData<LinearDividerParams>()

    init {
        val preference = PreferenceManager.getDefaultSharedPreferences(application)

        val dividerParams = preference.getString(name, null)
            ?.let { paramsJson ->
                Json.decodeFromString<LinearDividerParams>(paramsJson)
            }
            ?: LinearDividerParams()
        dividerParamsData.postValue(dividerParams)

        dividerParamsData.observe(this) { newDividerParams ->
            if (newDividerParams != null) {
                preference.edit().putString(name, Json.encodeToString(newDividerParams)).apply()
            } else {
                preference.edit().remove(name).apply()
            }
        }
    }
}