package com.comparesmartphoneprice

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlin.Exception

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent: Intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

        val myAsyncsSamsung: FirebaseAsyncTask = FirebaseAsyncTask()
        val myAsyncLG: FirebaseAsyncTask = FirebaseAsyncTask()
        val myAsyncApple: FirebaseAsyncTask = FirebaseAsyncTask()
        val myAsyncPrice: FirebaseAsyncTask = FirebaseAsyncTask()

        try {
            myAsyncsSamsung.execute("Samsung")
            myAsyncLG.execute("LG")
            myAsyncApple.execute("Apple")
            myAsyncPrice.execute("Price")
        } catch (e: Exception) {
            e.printStackTrace()
        }


        finish()
    }
}