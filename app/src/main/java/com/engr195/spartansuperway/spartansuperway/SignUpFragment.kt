package com.engr195.spartansuperway.spartansuperway

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.engr195.spartansuperway.spartansuperway.util.FirebaseAccount
import com.engr195.spartansuperway.spartansuperway.util.showToast
import com.firebase.client.Firebase
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_sign_up, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val firebaseRef = Firebase("https://spartan-superway.firebaseio.com/")
        // TODO: Add error validation text (eg. retryTextField)

        // TODO: Check for internet connection
        createAccountButton.setOnClickListener { createFirebaseAccount() }
    }

    private fun createFirebaseAccount() {

        if (!isValidForm()) return

        val firebaseAccount = FirebaseAccount(
                firstNameField.text.toString(),
                lastNameField.text.toString(),
                emailField.text.toString(),
                passwordField.text.toString()
        )

        val firebaseRef = Firebase("https://spartan-superway.firebaseio.com/accounts")
//        firebaseRef.push().setValue(firebaseAccount)
        

        context.showToast("Account created!")
        fragmentManager.popBackStack()
    }

    private fun isValidForm(): Boolean {
        val firstNameValid = firstNameField.text.toString().length > 0
        val lastNameValid = lastNameField.text.toString().length > 0
        return if (firstNameValid &&  lastNameValid && passwordsMatch() && isValidForm()) {
            true
        } else {
            context.showToast("Please enter all fields correctly")
            false
        }
    }

    private fun passwordsMatch(): Boolean = passwordField.text == passwordConfirmField.text

    private fun isValidUsername(): Boolean {
        val email = emailField.text.toString()
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    private fun getVisibility(isValid: Boolean) = if (isValid) View.GONE else View.VISIBLE
}