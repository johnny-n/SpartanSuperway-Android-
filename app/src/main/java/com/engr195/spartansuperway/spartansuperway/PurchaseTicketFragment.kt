package com.engr195.spartansuperway.spartansuperway

import android.os.Bundle
import android.support.transition.AutoTransition
import android.support.transition.Scene
import android.support.transition.TransitionManager
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.engr195.spartansuperway.spartansuperway.utils.showToast
import kotlinx.android.synthetic.main.fragment_purchase_ticket.*
import kotlinx.android.synthetic.main.fragment_purchase_ticket.square

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

        purchaseTicketButton.setOnClickListener {
            // TODO: Invalidate all payment fields when purchaseTicketButton is clicked
            overlay.setBackgroundColor(R.color.darkNeutralBackground)
            purchaseTicketButton.visibility = View.GONE
            val ticketScene = Scene.getSceneForLayout(square, R.layout.fragment_ticket, context)
            square.animate()
                .translationY(-500f)
                .start()
            TransitionManager.go(ticketScene, AutoTransition())
            context.showToast("Ticket purchased!")

            val okButton = square.findViewById(R.id.okButton)
            okButton.setOnClickListener {
                // Go back to MainActivity
                activity.finish()
            }
        }
    }
}