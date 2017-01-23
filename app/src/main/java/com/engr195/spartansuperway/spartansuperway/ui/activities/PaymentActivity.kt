package com.engr195.spartansuperway.spartansuperway.ui.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.engr195.spartansuperway.spartansuperway.R
import com.engr195.spartansuperway.spartansuperway.ui.fragments.PurchaseTicketFragment

class PaymentActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        supportFragmentManager.beginTransaction()
            .add(R.id.container, PurchaseTicketFragment())
            .commit()
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

}