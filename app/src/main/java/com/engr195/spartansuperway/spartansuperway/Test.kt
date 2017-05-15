package com.engr195.spartansuperway.spartansuperway

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.engr195.spartansuperway.spartansuperway.ui.activities.MainActivity
import java.util.concurrent.Semaphore

class TestActivity : AppCompatActivity() {

    companion object {
        val sem: Semaphore = Semaphore(2, true)
    }
    override fun onResume() {
        super.onResume()

        Thread().run {
            // Do something for a long time
            sem.acquire()
            if (sem.availablePermits() == 0) {
                startActivity(Intent(this@TestActivity, MainActivity.javaClass))
            }
            sem.release()
        }

        Thread().run {
            // Do something for a long time
            sem.acquire()
            if (sem.availablePermits() == 0) {
                startActivity(Intent(this@TestActivity, MainActivity.javaClass))
            }
            sem.release()
        }

    }
}
