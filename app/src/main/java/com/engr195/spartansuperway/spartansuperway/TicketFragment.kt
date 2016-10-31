package com.engr195.spartansuperway.spartansuperway

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater

class TicketDialogFragment: DialogFragment() {

    companion object {

        private val args_fromLocation = "args_fromLocation"
        private val args_toLocation = "args_toLocation"
        private val args_date = "args_date"
        private val args_time = "args_time"

        fun newInstance(fromLocation: String,
                        toLocation: String,
                        date: String,
                        time: String,
                        qrCode: Any?): TicketDialogFragment {

            val fragment = TicketDialogFragment()
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val ticketView = LayoutInflater.from(context).inflate(R.layout.fragment_ticket, null)
        return AlertDialog.Builder(context)
            .setView(R.layout.fragment_ticket)
            .create()
    }
}