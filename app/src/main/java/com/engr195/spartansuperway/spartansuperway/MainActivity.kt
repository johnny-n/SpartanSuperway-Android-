package com.engr195.spartansuperway.spartansuperway

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        val optionsMap = SparseArray<String>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // RecyclerView stuff
//        optionsMap.put(1, "Purchase Ticket")
//        optionsMap.put(2, "View Tickets")
//        optionsMap.put(3, "Map")
//        optionsMap.put(4, "Option4")
//        optionsMap.put(5, "Option5")
//        optionsMap.put(6, "Option6")
//        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        recyclerView.adapter = OptionsAdapter()



//        etaTime.setOnClickListener {
//            val payment = PaymentFragment()
//            payment.show(supportFragmentManager, "EtaActivity.class")
//        }
    }

    inline fun <T> with(receiver: T, block: T.() -> Unit) {
        receiver.block()
    }

    private inner class OptionsAdapter : RecyclerView.Adapter<OptionsViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): OptionsViewHolder {
            val inflater = LayoutInflater.from(applicationContext)
            val view = inflater.inflate(R.layout.list_item_options, parent, false)
            return OptionsViewHolder(view)
        }

        override fun onBindViewHolder(holder: OptionsViewHolder?, position: Int) {
            val text = optionsMap[position]
            holder?.setOptionText(text)
        }

        override fun getItemCount(): Int = optionsMap.size()

    }

    private inner class OptionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val optionText: TextView

        init { optionText = itemView.findViewById(R.id.optionText) as TextView }

        fun setOptionText(text: String?) {
            text?.let { optionText.setText(it) }
        }
    }
}