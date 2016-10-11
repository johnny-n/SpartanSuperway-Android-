package com.engr195.spartansuperway.spartansuperway

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater

class PaymentFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val paymentView = LayoutInflater.from(activity).inflate(R.layout.dialog_payment, null)

        return AlertDialog.Builder(context)
            .setView(paymentView)
            .setTitle("Payment Form")
            .setPositiveButton(android.R.string.ok, { dialog, which ->
                targetFragment.onActivityResult(targetRequestCode, Activity.RESULT_OK, null)
            }).create()
    }
}