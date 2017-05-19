package com.engr195.spartansuperway.spartansuperway.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.engr195.spartansuperway.spartansuperway.R
import com.engr195.spartansuperway.spartansuperway.data.key_firebaseUid
import com.engr195.spartansuperway.spartansuperway.ui.fragments.MainFragment
import com.engr195.spartansuperway.spartansuperway.ui.fragments.TicketListFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    enum class TabNames(val title: String) {
        MAIN("Main"),
        HISTORY("History")
    }

    companion object {
        val tag = MainActivity::class.java.simpleName
    }

    val tag = "MainActivity"
    lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userId = intent.extras.getString(key_firebaseUid)
        Log.d(tag, userId)

        viewpager.adapter = FragmentAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewpager)

        fab.setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            startActivity(intent)
        }
    }

    fun showFabButton(isVisible: Boolean) {
        val scaleFactor = if (isVisible) 1f else 0f
        fab.animate()
                .scaleX(scaleFactor)
                .scaleY(scaleFactor)
                .setDuration(600L)
                .start()
    }

    override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount == 0) {
            super.onBackPressed()
        } else {
            fragmentManager.popBackStack()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sign_out_settings -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    inner class FragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        val fragments = mapOf<TabNames, Fragment>(
                TabNames.MAIN.to(MainFragment.newInstance(userId)),
                TabNames.HISTORY.to(TicketListFragment.newInstance())
        )

        override fun getItem(position: Int): Fragment = fragments.get(TabNames.values()[position])!!

        override fun getCount(): Int = fragments.size

        override fun getPageTitle(position: Int): CharSequence = TabNames.values()[position].title

    }

    fun <A, B> A.to(that: B): Pair<A, B> = Pair(this, that)

    fun <A, B, C> with(a: A, b: B, f: (A, B) -> C) = f(a, b)

    inline fun <T> with(receiver: T, block: T.() -> Unit) {
        receiver.block()
    }

    fun Handler.postDelayed(duration: Long, r: () -> Unit) {
        this.postDelayed(r, duration)
    }
}