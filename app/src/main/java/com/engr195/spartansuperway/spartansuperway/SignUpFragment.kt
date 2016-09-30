package com.engr195.spartansuperway.spartansuperway

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.engr195.spartansuperway.spartansuperway.util.FirebaseAccount
import com.engr195.spartansuperway.spartansuperway.util.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment: Fragment() {

    val className = this@SignUpFragment.javaClass.name

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_sign_up, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: Check for internet connection
        createAccountButton.setOnClickListener { createFirebaseAccount() }
    }

    // TODO: Properly handle field validations
    // TODO: Add field validation text (eg. "password & passwordConfirm do not match")
    private fun createFirebaseAccount() {

//        if (!isValidForm()) return

        val firebaseAccount = FirebaseAccount(
                firstNameField.text.toString(),
                lastNameField.text.toString(),
                emailField.text.toString(),
                passwordField.text.toString()
        )

        val auth = FirebaseAuth.getInstance()
        auth.addAuthStateListener {
            val userProfile = UserProfileChangeRequest.Builder()
                    .setDisplayName("${firebaseAccount.firstName} ${firebaseAccount.lastName}")
                    .build()
            auth.currentUser?.updateProfile(userProfile)
        }

        auth.createUserWithEmailAndPassword(firebaseAccount.email, firebaseAccount.password)
            .addOnCompleteListener(activity, { task ->
                Log.d(className, "createUserWithEmail:onComplete:" + task.isSuccessful());
            })

        fragmentManager.popBackStack()
        context.showToast("Successfully created account!")
    }

    private fun isValidForm(): Boolean {
        val firstNameValid = firstNameField.text.toString().length > 0
        val lastNameValid = lastNameField.text.toString().length > 0
        return if (firstNameValid &&  lastNameValid && passwordsMatch()) {
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