package com.daniel.project.coronapop

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.PreferenceManager

class SplashActivity : AppCompatActivity() {
    private var isFirstStart: Boolean = false

    private var userSharedPref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // save user info in shared pref
        userSharedPref = PreferenceManager.getDefaultSharedPreferences(this) ?: return
        //  Create a new boolean and preference and set it to true
        isFirstStart = userSharedPref!!.getBoolean("firstStart", true)

        if (!isFirstStart) {
            // if the user exist
            startActivity(Intent(this, MainActivity::class.java))
//            startActivity(Intent(this, IntroActivity::class.java))
            finish()
        } else {
            userSharedPref!!.edit().putBoolean("firstStart", false).apply()
            startActivity(Intent(this, IntroActivity::class.java))
            finish()
        }
    }
}
