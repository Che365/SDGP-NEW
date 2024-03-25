package com.example.sdgpfrontendnew

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.provider.MediaStore




class MainActivity : AppCompatActivity() {

    private var IMAGE_CAPTURE_CODE =1

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val NextButton =findViewById<Button>(R.id.next)
        NextButton.setOnClickListener {
            startActivity(Intent(this,UploadPhotos::class.java))

        }

        val CapButton =findViewById<Button>(R.id.capbutton)
        CapButton.setOnClickListener {
            openCamera()
        }
    }
    private fun openCamera(){
        val takePicture =Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePicture.resolveActivity(packageManager) != null){
            startActivityForResult(takePicture,IMAGE_CAPTURE_CODE) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode ==IMAGE_CAPTURE_CODE && resultCode == RESULT_OK){
            val intent = Intent(this,UploadPhotos ::class.java)
            startActivity(intent)
        }
    }

}
