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
        val etaStatusWaiting = 200      // Pod is waiting for user to get inside
        val etaStatusDestination = 300  // Pod is otw to user's final destination
        val etaStatusArrival = 400      // Pod has arrived to the user's final destination
        val etaStatusNoTicket = 900
    }

    private val tag = "MainActivity"

    var userId: String? = null
    var etaAnimation: Runnable? = null
    var animationDuration = 500L
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
            }
        })
    }

    fun updateEta(pickup: String, destination: String, status: Int, eta: Int) {
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

    fun setupEtaForDestination() {
        etaTime.setOnClickListener {
            val database = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("users")
                    .child(userId)
                    .child("currentTicket")
            database.child("eta").setValue(10)
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
            database.child("status").setValue(MainActivity.etaStatusPickup)
            database.child("alive").setValue(true)
        }
    }

    fun <A, B, C> with(a: A, b: B, f: (A, B) -> C) = f(a, b)

    inline fun <T> with(receiver: T, block: T.() -> Unit) {
        receiver.block()
    }
}