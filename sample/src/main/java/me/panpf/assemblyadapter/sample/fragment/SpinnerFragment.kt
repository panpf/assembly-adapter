package me.panpf.assemblyadapter.sample.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import me.panpf.assemblyadapter.AssemblyAdapter
import me.panpf.assemblyadapter.sample.R
import me.panpf.assemblyadapter.sample.itemfactory.SpinnerItemFactory
import me.panpf.assemblyadapter.sample.bindView
import java.util.*

class SpinnerFragment : Fragment() {
    val spinner: Spinner by bindView(R.id.spinner_spinnerFragment)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_spinner, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
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

        val adapter = AssemblyAdapter(stringList)
        adapter.addItemFactory(SpinnerItemFactory())
        spinner.adapter = adapter
    }
}
