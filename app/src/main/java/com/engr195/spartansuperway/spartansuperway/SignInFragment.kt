package com.engr195.spartansuperway.spartansuperway

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.engr195.spartansuperway.spartansuperway.utils.showToast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SignInFragment : Fragment() {

    lateinit var parentActivity: MainActivity
    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentActivity = activity as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_sign_in, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Go to SignUpFragment to register an account.
        signUpAccountTextView.setOnClickListener { view ->
            val signUpFragment = SignUpFragment()
            parentActivity.replaceFragment(signUpFragment, true)
        }

        signInButton.setOnClickListener { view ->
            val email = emailField.text.toString()
            val password = passwordField.text.toString()
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(parentActivity) { taskResult ->
                        if (taskResult.isSuccessful) {
                            Log.d(tag, "signInWithEmail:onComplete:${taskResult.isSuccessful}")
                            val intent = Intent(activity, EtaActivity::class.java)
                            startActivity(intent)
                        } else {
                            Log.w(tag, "signInEmail:failed", taskResult.exception)
                            context.showToast("Incorrect email and/or password.")
                        }
                    }
        }
    }
}