package me.panpf.adapter.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.fm_spinner.*
import me.panpf.adapter.AssemblyListAdapter
import me.panpf.adapter.sample.R
import me.panpf.adapter.sample.item.SpinnerItem
import java.util.*

class SpinnerFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fm_spinner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val stringList = ArrayList<String>(10)
        stringList.add("1")
        stringList.add("2")
        stringList.add("3")
        stringList.add("4")
        stringList.add("5")
        stringList.add("6")
        stringList.add("7")
        stringList.add("8")
        stringList.add("9")
        stringList.add("10")

        val adapter = AssemblyListAdapter(stringList)
        adapter.addItemFactory(SpinnerItem.Factory())
        spinnerFm_spinner.adapter = adapter
    }

    override fun onUserVisibleChanged(isVisibleToUser: Boolean) {
        val attachActivity = activity
        if (isVisibleToUser && attachActivity is AppCompatActivity) {
            attachActivity.supportActionBar?.subtitle = "Spinner"
        }
    }
}
