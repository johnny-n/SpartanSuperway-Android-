package com.engr195.spartansuperway.spartansuperway.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.engr195.spartansuperway.spartansuperway.R
import com.engr195.spartansuperway.spartansuperway.data.etaStatusPickup
import com.engr195.spartansuperway.spartansuperway.ui.activities.LoginActivity
import com.engr195.spartansuperway.spartansuperway.ui.activities.MainActivity
import com.engr195.spartansuperway.spartansuperway.utils.showToast
import com.engr195.spartansuperway.spartansuperway.data.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SignInFragment : Fragment() {

    lateinit var parentActivity: LoginActivity
    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentActivity = activity as LoginActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_sign_in, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signInButton.isEnabled = false

        // Go to SignUpFragment to register an account.
        signUpAccountTextView.setOnClickListener { view ->
            val signUpFragment = SignUpFragment()
            parentActivity.replaceFragment(signUpFragment, true)
        }

        emailField.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val enabled = if (emailField.text.length > 0 && passwordField.text.length > 0) true else false
                signInButton.isEnabled = enabled
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        passwordField.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val enabled = if (emailField.text.length > 0 && passwordField.text.length > 0) true else false
                signInButton.isEnabled = enabled
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        signInButton.setOnClickListener { view ->
            val email = emailField.text.toString()
            val password = passwordField.text.toString()
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(parentActivity) { taskResult ->
                        if (taskResult.isSuccessful) {
                            Log.d(tag, "signInWithEmail:onComplete:${taskResult.isSuccessful}")
                            val intent = Intent(activity, MainActivity::class.java)
                            val userId = FirebaseAuth.getInstance().currentUser?.uid
                            userId?.let { intent.putExtra(MainActivity.key_firebaseUid, it) }
                            startActivity(intent)
                        } else {
                            Log.w(tag, "signInEmail:failed", taskResult.exception)
                            context.showToast("Incorrect email and/or password.")
                        }
                    }
        }


        autoSignInButton.setOnClickListener {
            val email = "test@gmail.com"
            val password = "Test1234"
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(parentActivity) { taskResult ->
                        if (taskResult.isSuccessful) {
                            Log.d(tag, "signInWithEmail:onComplete:${taskResult.isSuccessful}")
                            val intent = Intent(activity, MainActivity::class.java)
                            val userId = FirebaseAuth.getInstance().currentUser?.uid
                            createTestTicket(userId)
                            userId?.let { intent.putExtra(MainActivity.key_firebaseUid, it) }
                            startActivity(intent)
                        } else {
                            Log.w(tag, "signInEmail:failed", taskResult.exception)
                            context.showToast("Incorrect email and/or password.")
                        }
                    }
        }
    }
    fun createTestTicket(userId: String?) {
        val fromLocation = "Sunnyvale"
        val toLocation = "Union City"
        val eta = 0

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
}