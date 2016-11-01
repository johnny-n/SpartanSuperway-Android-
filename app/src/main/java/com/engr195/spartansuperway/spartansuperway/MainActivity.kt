package com.engr195.spartansuperway.spartansuperway

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        val optionsMap = SparseArray<String>()
    }

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
    }

    fun <A, B, C> with(a: A, b: B, f: (A, B) -> C) = f(a, b)

    inline fun <T> with(receiver: T, block: T.() -> Unit) {
        receiver.block()
    }
}