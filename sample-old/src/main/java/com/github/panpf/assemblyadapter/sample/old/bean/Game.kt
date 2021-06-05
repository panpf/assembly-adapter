package com.github.panpf.assemblyadapter.sample.old.bean

import me.panpf.adapter.paged.Diffable

class Game: Diffable<Game> {

    var iconResId: Int = 0
    var name: String? = null
    var like: String? = null

    override fun areItemsTheSame(other: Game): Boolean = this.name == other.name

    override fun areContentsTheSame(other: Game): Boolean {
        if (this === other) return true

        if (iconResId != other.iconResId) return false
        if (name != other.name) return false
        if (like != other.like) return false

        return true
    }
}
