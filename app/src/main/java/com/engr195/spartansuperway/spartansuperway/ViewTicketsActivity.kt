package com.engr195.spartansuperway.spartansuperway

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.engr195.spartansuperway.spartansuperway.utils.Ticket
import kotlinx.android.synthetic.main.activity_view_tickets.*

class ViewTicketsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_tickets)

        // TODO: Remove fakeTicket
        val fakeTicket = Ticket("Palo Alto", "San Francisco", "10/31/2016", "12:00PM")
        viewPager.adapter = TicketListPagerAdapter(listOf(fakeTicket), applicationContext, supportFragmentManager)
        tabs.setupWithViewPager(viewPager)

    }
}