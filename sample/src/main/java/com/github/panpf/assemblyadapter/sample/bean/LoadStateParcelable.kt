package com.github.panpf.assemblyadapter.sample.bean

import android.os.Parcelable
import androidx.paging.LoadState
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoadStateParcelable(
    val type: Int,
    val endOfPaginationReached: Boolean,
    val throwableClassName: String?
) : Parcelable {

    constructor(loadState: LoadState) : this(
        when (loadState) {
            is LoadState.NotLoading -> 0
            is LoadState.Loading -> 1
            is LoadState.Error -> -1
        },
        loadState.endOfPaginationReached,
        if (loadState is LoadState.Error) loadState.error.javaClass.name else null
    )

    fun toLoadState(): LoadState = when (type) {
        0 -> LoadState.NotLoading(endOfPaginationReached)
        1 -> LoadState.Loading
        -1 -> LoadState.Error(Throwable(throwableClassName))
        else -> throw IllegalStateException("Unknown type: $type")
    }
}