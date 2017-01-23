package com.engr195.spartansuperway.spartansuperway.ui.fragments

import android.os.Bundle
import android.support.transition.AutoTransition
import android.support.transition.Scene
import android.support.transition.TransitionManager
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.engr195.spartansuperway.spartansuperway.R
import com.engr195.spartansuperway.spartansuperway.ui.activities.MainActivity
import com.engr195.spartansuperway.spartansuperway.utils.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_purchase_ticket.*

class PurchaseTicketFragment : Fragment() {

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

            // Move button's container up (in scene that has yet to be .go() to)
            square.animate()
                    .translationY(-500f)
                    .start()
            TransitionManager.go(ticketScene, AutoTransition())
            context.showToast("Ticket purchased!")

            val fromLocation = square.findViewById(R.id.ticketFromLocation) as TextView
            val toLocation = square.findViewById(R.id.ticketToLocation) as TextView
            fromLocation.text = fromSpinner.selectedItem.toString()
            toLocation.text = toSpinner.selectedItem.toString()

            val okButton = square.findViewById(R.id.okButton)
            okButton.setOnClickListener {
                // Go back to MainActivity
                createTestTicket()
                activity.finish()
            }
        }
    }

    fun createTestTicket() {
        val fromLocation = fromSpinner.selectedItem.toString()
        val toLocation = toSpinner.selectedItem.toString()
        val eta = 10

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            val database = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("users")
                    .child(userId)
                    .child("currentTicket")

            database.child("from").setValue(fromLocation)
            database.child("to").setValue(toLocation)
//            database.child("eta").setValue(eta)
            database.child("status").setValue(MainActivity.etaStatusPickup) // etaStatusPickup = 100
            database.child("alive").setValue(true)
        }
    }
}