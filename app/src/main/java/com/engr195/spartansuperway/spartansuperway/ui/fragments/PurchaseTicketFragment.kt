package com.engr195.spartansuperway.spartansuperway.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.support.transition.AutoTransition
import android.support.transition.Scene
import android.support.transition.TransitionManager
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.engr195.spartansuperway.spartansuperway.R
import com.engr195.spartansuperway.spartansuperway.data.etaStatusPickup
import com.engr195.spartansuperway.spartansuperway.utils.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.dialog_select_station.view.*
import kotlinx.android.synthetic.main.fragment_purchase_ticket.*

class PurchaseTicketFragment : Fragment() {

    var dialog: AlertDialog? = null
    // TODO: Remove this later and work on 'TODO: Guard..' below
    var fromStationSelected = false
    var toStationSelected = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_purchase_ticket, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: Guard purchase button to activate only when all fields are fulfilled correctly
        fromButton.setOnClickListener {
            fromStationSelected = true
            showDialogForLocation(fromStationTextView)
        }
        toButton.setOnClickListener {
            toStationSelected = true
            showDialogForLocation(toStationTextView)
        }

        purchaseTicketButton.setOnClickListener {
            // TODO: Invalidate all payment fields when purchaseTicketButton is clicked
            overlay.setBackgroundColor(R.color.darkNeutralBackground)
            purchaseTicketButton.visibility = View.GONE
            val ticketScene = Scene.getSceneForLayout(square, R.layout.fragment_ticket, context)

            // Move button's container up (in scene that TransitionManager has yet to '.go()' to)
            square.animate()
                    .translationY(-500f)
                    .start()
            TransitionManager.go(ticketScene, AutoTransition())
            context.showToast("Ticket purchased!")

            val fromLocation = square.findViewById(R.id.ticketFromLocation) as TextView
            val toLocation = square.findViewById(R.id.ticketToLocation) as TextView
            fromLocation.text = fromStationTextView.text
            toLocation.text = toStationTextView.text

            val okButton = square.findViewById(R.id.okButton)
            okButton.setOnClickListener {
                // Go back to MainActivity
                createTestTicket()
                activity.finish()
            }
        }
    }

    fun showDialogForLocation(locationTextView: TextView) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_select_station, null)

        // Set listener for each station button, and set the
        // result to the associated locationTextView TextView(from/to)
        setStationButtonListenerFor(view.stationOne, locationTextView)
        setStationButtonListenerFor(view.stationTwo, locationTextView)
        setStationButtonListenerFor(view.stationThree, locationTextView)
        setStationButtonListenerFor(view.stationFour, locationTextView)

        dialog = AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(true)
                .show()

        if (fromStationSelected && toStationSelected) {
            TransitionManager.beginDelayedTransition(square)
            square.alpha = 1.0f
            purchaseTicketButton.isEnabled = true
        }
    }

    fun setStationButtonListenerFor(button: Button, location: TextView) {
        button.setOnClickListener {
            location.text = button.text
            dialog?.dismiss()
        }
    }

    fun createTestTicket() {
        val fromStation = fromStationTextView.text
        val toStation = toStationTextView.text

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            val database = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("users")
                    .child(userId)
                    .child("currentTicket")

            database.child("from").setValue(fromStation.last().toString().toInt())
            database.child("to").setValue(toStation.last().toString().toInt())
            database.child("status").setValue(etaStatusPickup)
            database.child("isNewTicket").setValue(true)
            database.child("timerOn").setValue(false)
        }
    }
}