package com.engr195.spartansuperway.spartansuperway

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_item_options.*
import java.util.*

class EtaActivity : AppCompatActivity() {

    companion object {
        val optionsMap = HashMap<Int, String>()
    }

    init {
        optionsMap.put(1, "Option1")
        optionsMap.put(2, "Option2")
        optionsMap.put(3, "Option3")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eta)
    }

    private inner class OptionsAdapter : RecyclerView.Adapter<OptionsViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): OptionsViewHolder {
            val inflater = LayoutInflater.from(applicationContext)
            val view = inflater.inflate(R.layout.list_item_options, parent, false)
            return OptionsViewHolder(view)
        }

        override fun onBindViewHolder(holder: OptionsViewHolder?, position: Int) {
            val text = optionsMap[1]
            holder?.setOptionText(text!!)
        }

        override fun getItemCount(): Int = optionsMap.size

    }
    private inner class OptionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setOptionText(text: String) {
            optionText.setText(text)
        }
    }
}