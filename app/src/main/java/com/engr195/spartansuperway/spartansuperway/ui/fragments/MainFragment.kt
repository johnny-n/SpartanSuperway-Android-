package com.engr195.spartansuperway.spartansuperway.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import com.engr195.spartansuperway.spartansuperway.R
import com.engr195.spartansuperway.spartansuperway.data.*
import com.engr195.spartansuperway.spartansuperway.utils.SimpleChildEventListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    var userId: String? = null
    val animDuration = 600L
    var etaAnimation: Runnable? = null
    var animationDuration = 500L
    var statusCode = 100
    var etaValue = 10
    // Pickup/dropoff locations pertain to station #
    var pickupLocation = 0
    var destLocation = 0

    companion object {
        val tag = MainFragment::class.java.simpleName

        fun newInstance(firebaseUid: String): Fragment {
            val fragment = MainFragment()
            val args = Bundle()
            args.putString(key_firebaseUid, firebaseUid)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = arguments.getString(key_firebaseUid)
        setupEtaConnection()
    }

    override fun onResume() {
        super.onResume()
        etaTime.isEnabled = true
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

    // This will set up the listeners on the database for automatic callback updates
    fun setupEtaConnection() {
        val currentTicketRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(userId)
                .child("currentTicket")

        // For updating ETA
        currentTicketRef.addChildEventListener(object : SimpleChildEventListener() {
            override fun onChildChanged(snapshot: DataSnapshot?, prevChild: String?) {

                val keyValue = snapshot?.value.toString()
                when (snapshot?.key) {
                    "from" -> pickupLocation = keyValue.toInt()
                    "to" -> destLocation = keyValue.toInt()
                    "status" -> statusCode = keyValue.toInt()
                    "eta" -> etaValue = keyValue.toInt()
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
            etaStatusPickupUser -> {
                updateGif(R.drawable.gif_pod_moving, R.color.white, 0)
                setupEtaAnimation()
                startEtaAnimation()

                animationDuration = 500L
                etaTime.background = resources.getDrawable(R.drawable.circle_green)
                "Your pod is on the way to pick you up."
            }
            etaStatusWaiting -> {
                updateGif(R.drawable.gif_pod_waiting, android.R.color.holo_blue_light, animDuration)
                setupEtaForDestination()
                animationDuration = 1000L
                etaTime.background = resources.getDrawable(R.drawable.circle_green)
                etaTime.text = "Your pod here.\nPlease enter the vehicle."
                return
            }
            etaStatusDestination -> {
                updateGif(R.drawable.gif_pod_moving, R.color.white, animDuration)
                animationDuration = 500L
                etaTime.background = resources.getDrawable(R.drawable.circle_green)
                "Your are on the way to to your destination."
            }
            etaStatusArrival -> {
                updateGif(R.drawable.gif_pod_waiting, android.R.color.holo_blue_light, animDuration)
                gifImage.setImageResource(R.drawable.gif_pod_waiting)
                gifBackground.setBackgroundResource(android.R.color.holo_blue_light)
                etaAnimation = null
                etaTime.removeCallbacks(etaAnimation)
                etaTime.background = resources.getDrawable(R.drawable.circle_yellow)
                etaTime.text = "Your pod has arrived at your destination.\n"
                completeEtaStatus()
                return
            }
            etaStatusNoTicket -> {
                updateGif(R.drawable.gif_sunshine, R.color.colorAccent, animDuration)
                etaTime.background = resources.getDrawable(R.drawable.circle_idle)
                etaTime.text = "No active ticket."
                return
            }
            else -> "Error in pod status\n"
        }

        val etaString = "Pickup: $pickup\n" +
                "Destination: $destination\n\n" +
                "$statusString\n\n" +
                "ETA: $eta seconds"

        etaTime.text = etaString
    }

    fun updateGif(gifResId: Int, gifBackgroundResId: Int, animDuration: Long) {
        // Calculate start/end radius of gifBackground
        val root: View = gifBackground
        val x = (root.right - root.left) / 2
        val y = (root.bottom - root.top) / 2

        val containerWidth = root.width / 2
        val containerHeight = root.height / 2
        val startingRadius = 0
        val finalRadius = Math.sqrt((containerWidth * containerHeight + containerHeight * containerHeight).toDouble()).toInt()

        gifImage.setImageResource(gifResId)
        gifBackground.setBackgroundResource(gifBackgroundResId)
        Log.d(tag, "root = $root, x = $x, y = $y, startingRadius = $startingRadius, finalRadius = $finalRadius")
        ViewAnimationUtils.createCircularReveal(root, x, y, startingRadius.toFloat(), finalRadius.toFloat())
                .setDuration(animDuration)
                .start()
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
            database.child("eta").setValue(10)
            database.child("status").setValue(etaStatusDestination)
            // TODO: Remove bug where onClickListener isn't removed with the line below
            etaTime.setOnClickListener(null)
        }
    }

    // TODO: Do something with this?
    fun completeEtaStatus() {
        etaTime.setOnClickListener {
            val database = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("users")
                    .child(userId)
                    .child("currentTicket")

            database.child("status").setValue(etaStatusNoTicket)
            // TODO: Remove bug where onClickListener isn't removed with the line below
            etaTime.setOnClickListener(null)
            etaTime.isEnabled = false
        }
    }

    override fun onPause() {
        super.onPause()
        if (etaAnimation != null) {
            etaTime.removeCallbacks(etaAnimation)
            etaAnimation = null
        }
    }

    // TODO: Remove this later
    // FOR TESTING/DEMO PURPOSES
    fun createTestTicket() {
        val fromLocation = 0
        val toLocation = 0
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
            database.child("status").setValue(etaStatusPickupUser)
            database.child("isNewTicket").setValue(true)
        }
    }

    fun <A, B, C> with(a: A, b: B, f: (A, B) -> C) = f(a, b)

    inline fun <T> with(receiver: T, block: T.() -> Unit) {
        receiver.block()
    }
}