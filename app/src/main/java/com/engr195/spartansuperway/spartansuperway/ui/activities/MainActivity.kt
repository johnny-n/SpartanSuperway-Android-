package com.engr195.spartansuperway.spartansuperway.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.engr195.spartansuperway.spartansuperway.R
import com.engr195.spartansuperway.spartansuperway.ui.fragments.MainFragment
import com.engr195.spartansuperway.spartansuperway.ui.fragments.MainFragment.Companion.key_firebaseUid
import com.engr195.spartansuperway.spartansuperway.ui.fragments.TicketListFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        val tag = MainActivity::class.java.simpleName
    }

    private val tag = "MainActivity"

    var userId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            startActivity(intent)
        }

        viewpager.adapter = FragmentAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewpager)

        intent.extras.get(key_firebaseUid)?.let {
            userId = it as String
        }
        Log.d(tag, userId ?: "userId == null")
    }

    inner class FragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        val fragments = listOf(
                MainFragment.newInstance(),
                TicketListFragment.newInstance())

        override fun getItem(position: Int): Fragment = fragments[position]

        override fun getCount(): Int = fragments.size
    }

    fun <A, B, C> with(a: A, b: B, f: (A, B) -> C) = f(a, b)

    inline fun <T> with(receiver: T, block: T.() -> Unit) {
        receiver.block()
    }

    fun Handler.postDelayed(duration: Long, r: () -> Unit) {
        this.postDelayed(r, duration)
    }
}