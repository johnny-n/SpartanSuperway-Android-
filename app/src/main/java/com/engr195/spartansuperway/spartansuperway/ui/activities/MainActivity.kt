package com.engr195.spartansuperway.spartansuperway.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.engr195.spartansuperway.spartansuperway.R
import com.engr195.spartansuperway.spartansuperway.data.etaStatusArrival
import com.engr195.spartansuperway.spartansuperway.data.etaStatusDestination
import com.engr195.spartansuperway.spartansuperway.data.etaStatusPickup
import com.engr195.spartansuperway.spartansuperway.data.etaStatusWaiting
import com.engr195.spartansuperway.spartansuperway.utils.SimpleChildEventListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        val tag = MainActivity::class.java.simpleName
        val key_firebaseUid = "key_firebase_uid"
    }

    private val tag = "MainActivity"

    var userId: String? = null
    var etaAnimation: Runnable? = null
    var animationDuration = 500L
    var statusCode = 100
    var etaValue = 1337
    // Pickup/dropoff locations pertain to station #
    var pickupLocation = 0
    var destLocation = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        purchaseTicketButton.setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            startActivity(intent)
        }

        viewTicketsButton.setOnClickListener {
            val intent = Intent(this, ViewTicketsActivity::class.java)
            startActivity(intent)
        }


        intent.extras.get(key_firebaseUid)?.let { userId = it as String }
        Log.d(tag, userId ?: "userId == null")


        userId?.let { setupEtaConnection() }
//        createTestTicket()

        setupEtaAnimation()
    }

    fun startEtaAnimation() {
        if (etaAnimation != null) {
            etaTime.animate()
                    .setDuration(animationDuration)
                    .alpha(1.0f)
                    .withEndAction(etaAnimation)
        } else {
            etaTime.alpha = 1.0f
        }
    }

    fun setupEtaAnimation() {
        if (etaAnimation == null) {
            etaAnimation = Runnable {
                etaTime.animate()
                        .setDuration(animationDuration)
                        .alpha(0.6f)
                        .withEndAction {
                            startEtaAnimation()
                        }
            }
        }
    }

    // This will set up the listeners on the database for automatic callback updates
    fun setupEtaConnection() {
        val currentTicketRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(userId)
                .child("currentTicket")

        // For updating ETA
        currentTicketRef.addChildEventListener(object: SimpleChildEventListener() {
            override fun onChildChanged(snapshot: DataSnapshot?, prevChild: String?) {

                val keyValue = snapshot?.value.toString()
                when (snapshot?.key) {
                    "from"  -> pickupLocation = keyValue.toInt()
                    "to"    -> destLocation = keyValue.toInt()
                    "status"-> statusCode = keyValue.toInt()
                    "eta"   -> etaValue = keyValue.toInt()
                }

                Log.d("PickupLocation", pickupLocation.toString())
                Log.d("DestLocation", destLocation.toString())
                Log.d("StatusCode", statusCode.toString())
                Log.d("ETA Value", "eta = $etaValue")

                updateEtaUi(pickupLocation, destLocation, statusCode, etaValue)
            }
        })
    }

    // This gets called from the Firebase listeners whenever there are updates to the database
    fun updateEtaUi(pickup: Int, destination: Int, status: Int, eta: Int) {
        val statusString = when (status) {
            // TODO: Finish updated status code drawable names
            etaStatusPickup      -> {
                setupEtaAnimation()
                startEtaAnimation()

                animationDuration = 500L
                etaTime.background = resources.getDrawable(R.drawable.circle_pod_pickup)
                "Your pod is on the way to pick you up."
            }
            etaStatusWaiting     -> {
                setupEtaForDestination()
                animationDuration = 1000L
                etaTime.background = resources.getDrawable(R.drawable.circle_pod_waiting)
                etaTime.text = "Your pod here.\nPlease enter the vehicle."
                return
            }
            etaStatusDestination -> {
                animationDuration = 500L
                etaTime.background = resources.getDrawable(R.drawable.circle_pod_to_destination)
                "Your are on the way to to your destination."
            }
            etaStatusArrival     -> {
                etaAnimation = null
                etaTime.removeCallbacks(etaAnimation)
                etaTime.background = resources.getDrawable(R.drawable.circle_pod_dropoff)
                etaTime.text = "Your pod has arrived at your destination.\n"
                return
            }
            else -> "Error in pod status."
        }

        val etaString = "Pickup: $pickup\n" +
                        "Destination: $destination\n\n" +
                        "$statusString\n\n" +
                        "ETA: $eta seconds"

        etaTime.text = etaString
    }

    fun setupEtaForPickup() {
        val database = FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(userId)
                .child("currentTicket")


    }

    fun setupEtaForDestination() {
        etaTime.setOnClickListener {
            val database = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("users")
                    .child(userId)
                    .child("currentTicket")

            Log.d(tag, "writing value to status = ${etaStatusDestination}")
            database.child("status").setValue(etaStatusDestination)
            // TODO: Remove bug where onClickListener isn't removed with the line below
            etaTime.setOnClickListener(null)
        }
    }

    override fun onPause() {
        super.onPause()
        if (etaAnimation != null) {
            etaTime.removeCallbacks(etaAnimation)
            etaAnimation = null
        }
    }

    override fun onBackPressed() {
        // Do nothing. Users should sign out via the menu options.
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.sign_out_settings -> finish()
        }

        return super.onOptionsItemSelected(item)
    }

    // TODO: Remove this later
    // FOR TESTING/DEMO PURPOSES
    fun createTestTicket() {
        val fromLocation = "Sunnyvale"
        val toLocation = "Union City"
        val eta = 12345

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            val database = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("users")
                    .child(userId)
                    .child("currentTicket")

            database.child("from").setValue(fromLocation)
            database.child("to").setValue(toLocation)
            database.child("eta").setValue(eta)
            database.child("status").setValue(etaStatusPickup)
            database.child("alive").setValue(true)
        }
    }

    fun <A, B, C> with(a: A, b: B, f: (A, B) -> C) = f(a, b)

    inline fun <T> with(receiver: T, block: T.() -> Unit) {
        receiver.block()
    }
}