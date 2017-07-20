package com.engr195.spartansuperway.spartansuperway.ui.activities

import android.app.AlertDialog
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.support.transition.AutoTransition
import android.support.transition.Scene
import android.support.transition.TransitionManager
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.engr195.spartansuperway.spartansuperway.R
import com.engr195.spartansuperway.spartansuperway.data.etaStatusPickupUser
import com.engr195.spartansuperway.spartansuperway.utils.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.dialog_select_station.view.*

class PaymentActivity: AppCompatActivity() {

    val tag = PaymentActivity::class.java.simpleName
    var dialog: AlertDialog? = null
    // TODO: Remove this later and work on 'TODO: Guard..' below
    var fromStationSelected = false
    var toStationSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

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
            val ticketScene = Scene.getSceneForLayout(square, R.layout.fragment_ticket, this)

            // Move button's container up (in scene that TransitionManager has yet to '.go()' to)
            square.animate()
                    .translationY(-250f)
                    .start()
            TransitionManager.go(ticketScene, AutoTransition())
            this.showToast("Ticket purchased!")

            val fromLocation = square.findViewById(R.id.ticketFromLocation) as TextView
            val toLocation = square.findViewById(R.id.ticketToLocation) as TextView
            fromLocation.text = fromStationTextView.text
            toLocation.text = toStationTextView.text

            val okButton = square.findViewById(R.id.okButton)
            okButton.setOnClickListener {
                // Go back to MainActivity
                createTestTicketAndFinish()
            }
        }
    }

    fun showDialogForLocation(locationTextView: TextView) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_select_station, null)

        // Set listener for each station button, and set the
        // result to the associated locationTextView TextView(from/to)
        setStationButtonListenerFor(view.stationOne, locationTextView)
        setStationButtonListenerFor(view.stationTwo, locationTextView)
        setStationButtonListenerFor(view.stationThree, locationTextView)
        setStationButtonListenerFor(view.stationFour, locationTextView)

        dialog = AlertDialog.Builder(this)
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
            location.setTypeface(Typeface.DEFAULT_BOLD, Typeface.ITALIC)
            dialog?.dismiss()
        }
    }

    fun createTestTicketAndFinish() {
        val fromStation = fromStationTextView.text
        val toStation = toStationTextView.text

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {

            Log.d(tag, "push to firebase")
            val database = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("users")
                    .child(userId)
                    .child("currentTicket")

            database.child("from").setValue(fromStation.last().toString().toInt())
            database.child("to").setValue(toStation.last().toString().toInt())
            database.child("status").setValue(etaStatusPickupUser)
            database.child("isNewTicket").setValue(true)
            database.child("timerOn").setValue(false)
        }

        // TODO: Account for network delay to push data to Firebase.
        // TODO: We do not want to finish the activity before this data is pushed to firebase
        Handler().postDelayed(1000) {
            finish()
        }
    }
    /**
     * @param addToBackStack set to true if you want the "back" button to reverse this action
     *                       (adding the transaction to the back stack)
     */
    fun replaceFragment(fragment: Fragment, addToBackStack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.replace(R.id.fragment_container, fragment).commit()
    }

    fun Handler.postDelayed(duration: Long, runnable: () -> Unit) {
        this.postDelayed(runnable, duration)
    }

}