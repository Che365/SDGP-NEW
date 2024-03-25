package com.example.sdgpfrontendnew

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AlertDialog

class SplashScreen : AppCompatActivity() {

    private val SPLASH_TIME: Long =2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            instructions()

        },SPLASH_TIME)
    }
    private fun instructions(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("INSTRUCTIONS")
            .setMessage("1:Please click the Camera button in order to capture the soybean plant.\n" +
                    "2:There you can get a captured image from the gallery too.\n" +
                    "3:Make sure you have a clear image or make sure you get a clear image.\n" +
                    "4:Once done confirm and proceed to see the results.")
            .setPositiveButton("OK"){dialog,which->
                dialog.dismiss()
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
        val alertDialog : AlertDialog = builder.create()
        alertDialog.show()
    }
}