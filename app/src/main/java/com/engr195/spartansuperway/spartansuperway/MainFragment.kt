package com.engr195.spartansuperway.spartansuperway

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.dialog_payment.*

// TODO: Remove this fragment?
class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: Animate any changes when new views pop up.

        orderPodButton.setOnClickListener {
            // TODO: Set default visibility of payment form to 'View.GONE'
            paymentForm.visibility = View.VISIBLE
        }

        submitFormButton.setOnClickListener {
            // TODO: Set default visibility of QR image to 'View.GONE'
            paymentForm.visibility = View.GONE
            qrImage.visibility = View.VISIBLE
        }

        qrImage.setOnClickListener {
            val intent = Intent(activity, EtaActivity::class.java)
            startActivity(intent)
        }
    }
}