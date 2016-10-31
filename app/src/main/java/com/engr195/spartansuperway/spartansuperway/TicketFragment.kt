package com.engr195.spartansuperway.spartansuperway

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class TicketFragment: Fragment() {


    companion object {
        private val args_fromLocation = "args_fromLocation"
        private val args_toLocation = "args_toLocation"
        private val args_date = "args_date"
        private val args_time = "args_time"

        fun newInstance(fromLocation: String,
                        toLocation: String,
                        date: String,
                        time: String,
                        qrCode: Any?): TicketFragment {

            val fragment = TicketFragment()
            val args = Bundle()
            with(args) {
                putString(fromLocation, args_fromLocation)
                putString(toLocation, args_toLocation)
                putString(date, args_date)
                putString(time, args_time)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_ticket, container, false)
}