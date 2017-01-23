package com.engr195.spartansuperway.spartansuperway.ui

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.engr195.spartansuperway.spartansuperway.ui.fragments.TicketListFragment
import com.engr195.spartansuperway.spartansuperway.data.Ticket

class TicketListPagerAdapter(val ticketList: List<Ticket>, val context: Context, fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {

    private val pagerSize = 2
    private val tabTitles = listOf("Current", "Past")

    override fun getItem(position: Int): Fragment {
        return TicketListFragment()
    }

    override fun getCount(): Int = pagerSize

    override fun getPageTitle(position: Int): CharSequence {
        return tabTitles[position]
    }
}