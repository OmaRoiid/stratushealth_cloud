package com.silverkey.phr.PHR.SplashScreen

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.silverkey.phr.Helpers.startLoginScreen
import com.silverkey.phr.R

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed(Runnable {
            startLoginScreen(this@SplashScreen)
            finish()
        }, 3000)
    }
}
