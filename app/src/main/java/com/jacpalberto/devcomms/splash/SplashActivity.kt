package com.jacpalberto.devcomms.splash

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.animation.AccelerateDecelerateInterpolator
import com.jacpalberto.devcomms.R
import com.jacpalberto.devcomms.events.MainActivity
import com.jacpalberto.devcomms.utils.createAnimationOnAnimationEnd
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * Created by Alberto Carrillo on 7/12/18.
 */
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        init()
    }

    private fun init() {
        logoImageView.animate()
                .setDuration(1500L)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .alpha(1F)
                .setListener(createAnimationOnAnimationEnd {
                    startActivity(MainActivity.newIntent(this@SplashActivity))
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                })
    }
}