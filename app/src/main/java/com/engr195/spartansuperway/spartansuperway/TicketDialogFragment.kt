package com.engr195.spartansuperway.spartansuperway

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.engr195.spartansuperway.spartansuperway.utils.Ticket

class TicketDialogFragment: DialogFragment() {

    companion object {

        private val args_fromLocation = "args_fromLocation"
        private val args_toLocation = "args_toLocation"
        private val args_date = "args_date"
        private val args_time = "args_time"

        fun newInstance(ticket: Ticket): TicketDialogFragment {

            val fragment = TicketDialogFragment()
            val args = Bundle()
            with(args) {
                with(ticket) {
                    putString(fromLocation, args_fromLocation)
                    putString(toLocation, args_toLocation)
                    putString(date, args_date)
                    putString(time, args_time)
                }
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_ticket, container, false)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        with(arguments) {
            val ticketFrom = getString(args_toLocation)
            val ticketTo = getString(args_toLocation)
            val ticketDate = getString(args_date)
            val ticketTime = getString(args_time)
        }

        val ticketView = LayoutInflater.from(context).inflate(R.layout.fragment_ticket, null)


        // OK button will be set in AlertDialog.Builder, so we'll hide the default one inside the view
        val okButton = ticketView.findViewById(R.id.okButton) as Button
        okButton.visibility = View.GONE

        return AlertDialog.Builder(context)
                .setPositiveButton(android.R.string.ok) { dialog, which ->
                    dialog.dismiss()
                }
                .setView(ticketView)
                .create()
    }


}