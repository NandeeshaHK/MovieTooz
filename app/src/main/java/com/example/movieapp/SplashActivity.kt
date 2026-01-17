package com.example.movieapp

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.ComponentActivity
import com.airbnb.lottie.LottieAnimationView

import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val animation = findViewById<LottieAnimationView>(R.id.splashAnimation)
        val staticLogo = findViewById<View>(R.id.staticLogo)

        // Try to load lottie raw resource if it exists, otherwise fallback to static image
        val resId = resources.getIdentifier("movietooz_splash", "raw", packageName)
        
        if (resId != 0) {
            animation.setAnimation(resId)
            animation.addAnimatorListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    navigateToMain()
                }
            })
        } else {
            // Fallback: Show static image and delay
            animation.visibility = View.GONE
            staticLogo.visibility = View.VISIBLE
            
            // Artificial delay for static splash
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                navigateToMain()
            }, 2000)
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finish()
    }
}
