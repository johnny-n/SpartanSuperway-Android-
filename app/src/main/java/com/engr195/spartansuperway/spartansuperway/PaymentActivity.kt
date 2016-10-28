package com.engr195.spartansuperway.spartansuperway

import android.graphics.Point
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.transition.TransitionManager
import android.util.Log
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import kotlinx.android.synthetic.main.activity_payment.*

class PaymentActivity: AppCompatActivity() {

    val tag = "PaymentActivity"

    var startX = 0f
    var startY = 0f
    var startWidth = 0
    var startHeight = 0

    companion object {
        val key_startX = "key_startX"
        val key_startY = "key_startY"
        val key_width = "key_width"
        val key_height = "key_height"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        postponeEnterTransition()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val bundle = intent.extras
        startX = bundle.getFloat(key_startX)
        startY = bundle.getFloat(key_startY)
        startWidth = bundle.getInt(key_width)
        startHeight = bundle.getInt(key_height)

        Log.d(tag, "startX = $startX")
        Log.d(tag, "startY = $startY")
        Log.d(tag, "startWidth = $startWidth")
        Log.d(tag, "startHeight = $startHeight")

    }

    override fun onResume() {
        super.onResume()

        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val windowWidth = size.x
        val windowHeight = size.y

        val translateAnimation = TranslateAnimation(startX, 0f, startY, 0f)
        translateAnimation.duration = 1000

        frame.layoutParams.width = startWidth
        frame.layoutParams.height = startHeight
        frame.animate().apply {
            duration = 0
            x(startX)
            y(startY)
        }.withEndAction {
            frame.startAnimation(translateAnimation)
        }.start()
    }

}