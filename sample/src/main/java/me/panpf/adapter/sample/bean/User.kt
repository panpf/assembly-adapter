package me.panpf.adapter.sample.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User: Parcelable {
    var headResId: Int = 0
    var name: String? = null
    var sex: String? = null
    var age: String? = null
    var job: String? = null
    var monthly: String? = null
}
