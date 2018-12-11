package me.panpf.adapter.sample.ui

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    override fun onResume() {
        super.onResume()
        if (userVisibleHint) onUserVisibleChanged(true)
    }

    override fun onPause() {
        super.onPause()
        if (userVisibleHint) onUserVisibleChanged(false)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        onUserVisibleChanged(isResumed)
    }

    open fun onUserVisibleChanged(isVisibleToUser: Boolean) {

    }
}
