package com.engr195.spartansuperway.spartansuperway

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        val key_firebaseUid = "key_firebase_uid"
        val etaStatusPickup = 100       // Pod is otw to pickup user at their location
        val etaStatusDestination = 200  // Pod is otw to user's final destination
        val etaStatusArrival = 300      // Pod has arrived to the user's final destination
        val etaStatusNoTicket = 900
    }

    private val tag = "MainActivity"

    var userId: String? = null
    var pickupLocation = "pickupLocation"
    var destLocation = "destLocation"
    var statusCode = 100
    var etaValue = 1337

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
        createTestTicket()

        // TEST
        repeatEtaAnimation()
    }

    // TEST
    fun repeatEtaAnimation() {
        etaTime.animate()
                .setDuration(500)
                .alpha(0.60f)
                .withEndAction {
                    etaTime.animate()
                            .setDuration(500)
                            .alpha(1.0f)
                            .withEndAction {
                                repeatEtaAnimation()
                            }
                }
    }

    // This will set up the listeners on the database for automatic callback updates
    fun setupEtaConnection() {
        val database = FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(userId)
                .child("currentTicket")

//        database.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val ticketAlive = snapshot.child("currentTicket").child("alive").toString().toBoolean()
//                if (ticketAlive) {
//                    val pickupLocation = snapshot.child("from").value.toString()
//                    val destLocation = snapshot.child("to").value.toString()
//                    val statusCode = snapshot.child("status").value.toString().toInt()
//                    val etaValue = snapshot.value.toString().toInt()
//                    Log.d("ETA Value", "eta = $etaValue")
//                    updateEta(pickupLocation, destLocation, statusCode, etaValue)
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.e("MainActivity", "singleValueEvent error: ${error.toString()}")
//            }
//        })

        // For updating ETA
        database.addChildEventListener(object : ChildEventListener {
            override fun onChildMoved(snapshot: DataSnapshot?, prevChild: String?) {
            }

            override fun onChildChanged(snapshot: DataSnapshot, prevChild: String?) {
                // TODO: Save these variables globally

                val keyValue = snapshot.value.toString()
                when (snapshot.key) {
                    "from"  -> pickupLocation = keyValue
                    "to"    -> destLocation = keyValue
                    "status"-> statusCode = keyValue.toInt()
                    "eta"   -> etaValue = keyValue.toInt()
                }

                Log.d("PickupLocation", pickupLocation)
                Log.d("DestLocation", destLocation)
                Log.d("StatusCode", statusCode.toString())
                Log.d("ETA Value", "eta = $etaValue")

                // TODO: Change status color depending on value of status code for ETA
                updateEta(pickupLocation, destLocation, statusCode, etaValue)
            }

            override fun onChildAdded(snapshot: DataSnapshot?, prevChild: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot?) {
            }

            override fun onCancelled(error: DatabaseError?) {
                throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    fun updateEta(pickup: String, destination: String, status: Int, eta: Int) {
        val statusString = when (status) {
            // TODO: Finish updated status code drawable names
            etaStatusPickup      -> {
                etaTime.background = resources.getDrawable(R.drawable.circle_pod_pickup)
                "Your pod is on the way to pick you up." }
            etaStatusDestination -> {
                etaTime.background = resources.getDrawable(R.drawable.circle_pod_otw)
                "You on the way to to your destination."
            }
            etaStatusArrival     -> {
                etaTime.background = resources.getDrawable(R.drawable.circle_pod_dropoff)
                "Your pod has arrived at your destination." }
            else -> "Error in pod status."
        }

        val etaString = "Pickup: $pickup\n" +
                "Destination: $destination\n\n" +
                "$statusString\n\n" +
                "ETA: $eta minutes"

        etaTime.text = etaString
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

    fun <A, B, C> with(a: A, b: B, f: (A, B) -> C) = f(a, b)

    inline fun <T> with(receiver: T, block: T.() -> Unit) {
        receiver.block()
    }

    // For testing purposes
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
            database.child("status").setValue(MainActivity.etaStatusPickup)
            database.child("alive").setValue(true)
        }
    }
}