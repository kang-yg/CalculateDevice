package com.comparesmartphoneprice

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        toolbar.title = getString(R.string.app_name)
//        setSupportActionBar(toolbar)


        supportFragmentManager.beginTransaction()
            .replace(R.id.main_framelayout, CalculateFragment()).commit()
    }

    //툴바 메뉴 추가
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.toolbar_menu, menu)
//
//        return true
//    }

    //메뉴선택 기능
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.menu1 -> {
                Log.d("onOptionsItemSelected", "menu01")
                return true
            }
            R.id.menu2 -> {
                Log.d("onOptionsItemSelected", "menu02")
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
