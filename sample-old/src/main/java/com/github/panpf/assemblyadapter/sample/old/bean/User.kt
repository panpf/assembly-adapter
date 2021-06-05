package com.github.panpf.assemblyadapter.sample.old.bean

import me.panpf.adapter.paged.Diffable

class User : Diffable<User> {

    var headResId: Int = 0
    var name: String? = null
    var sex: String? = null
    var age: String? = null
    var job: String? = null
    var monthly: String? = null

    override fun areItemsTheSame(other: User): Boolean = this.name == other.name

    override fun areContentsTheSame(other: User): Boolean {
        if (this === other) return true

        if (headResId != other.headResId) return false
        if (name != other.name) return false
        if (sex != other.sex) return false
        if (age != other.age) return false
        if (job != other.job) return false
        if (monthly != other.monthly) return false

        return true
    }
}
