package com.engr195.spartansuperway.spartansuperway

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_purchase_ticket.*

class PurchaseTicketFragment: Fragment() {

    // TODO: Enter hard-coded strings in fragment_purchase_tickets into strings.xml

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_purchase_ticket, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up spinner
        val spinnerAdapter = ArrayAdapter.createFromResource(context, R.array.spinner_locations, android.R.layout.simple_spinner_item)
        fromSpinner.adapter = spinnerAdapter
        toSpinner.adapter = spinnerAdapter
    }
}