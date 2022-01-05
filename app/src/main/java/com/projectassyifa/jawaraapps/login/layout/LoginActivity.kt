package com.projectassyifa.jawaraapps.login.layout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.projectassyifa.jawaraapps.R

class LoginActivity : AppCompatActivity() {
    private var backpressedTime = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
    override fun onBackPressed() {
        if (backpressedTime + 2000 > System.currentTimeMillis()) {
            finishAffinity()
        } else {
            Toast.makeText(
                applicationContext,
                "Tekan kembali sekali lagi untuk keluar",
                Toast.LENGTH_SHORT
            ).show()
        }
        backpressedTime = System.currentTimeMillis()

    }
}