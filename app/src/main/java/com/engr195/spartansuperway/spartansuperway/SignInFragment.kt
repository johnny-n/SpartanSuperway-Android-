package com.engr195.spartansuperway.spartansuperway

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.engr195.spartansuperway.spartansuperway.utils.showToast
import com.google.firebase.auth.FirebaseAuth
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
                            startActivity(intent)
                        } else {
                            Log.w(tag, "signInEmail:failed", taskResult.exception)
                            context.showToast("Incorrect email and/or password.")
                        }
                    }
        }
    }
}