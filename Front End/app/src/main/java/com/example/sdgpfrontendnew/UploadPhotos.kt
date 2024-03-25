package com.example.sdgpfrontendnew

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.sdgpfrontendnew.ml.TensorFlowLite
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.net.HttpURLConnection
import java.net.URL
import java.nio.ByteBuffer
import java.nio.ByteOrder
import okhttp3.OkHttpClient
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.*
import java.io.File

class UploadPhotos : AppCompatActivity() {

    private val REQ_CODE = 1
    private lateinit var chosenimage: ImageView
    var imguri : Uri? = null

    private var imageBitmap: Bitmap?=null
    private val httpClient = OkHttpClient()



    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_photos)

        val choseImageButton = findViewById<Button>(R.id.choosephoto)
        choseImageButton.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, REQ_CODE)
        }
        val getPredButton = findViewById<Button>(R.id.predictions)
        getPredButton.setOnClickListener {
            imageBitmap?.let { bitmap->
            val imageFile = bitmapConvert(bitmap,this)
            httpService(imageFile)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE && resultCode == RESULT_OK && data !=null && data.data !=null){
            imguri = data.data
            imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver,imguri)
            val chosenImage = findViewById<ImageView>(R.id.chosenimage)

            chosenImage.setImageBitmap(imageBitmap)

        }
    }
     fun httpService(imageFile: File){
         println("Sending the request")
         val reqBody : RequestBody = MultipartBody.Builder()
             .setType(MultipartBody.FORM)
             .addFormDataPart("file",imageFile.name,RequestBody.create("image/jpeg".toMediaTypeOrNull(),imageFile))
             .build()


         val request: Request = Request.Builder().url("http://127.0.0.1:5000/Predict").post(reqBody).build()
         println("Making the request")

         httpClient.newCall(request).enqueue(object : Callback {
             override fun  onResponse(call: Call, response: Response){
                 response.use {
                     if (!response.isSuccessful){
                         throw IOException("Unexpected $response")
                         println("Success")
                     }
                     val responseBodyString = response.body?.string()
                     runOnUiThread{
                         val predictionsView =findViewById<TextView>(R.id.predictions)
                         predictionsView.text = responseBodyString
                     }
                 }

             }

             override fun onFailure(call: Call, e: IOException) {
                 runOnUiThread {
                     Toast.makeText(this@UploadPhotos,"Failed to connect",Toast.LENGTH_LONG).show()
                 }
             }
         })



    }
    private fun bitmapConvert(bitmap: Bitmap,context: Context): File {
        val file = File(context.cacheDir,"jpg")

        try {
            val writeData = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,writeData)

            writeData.close()
            return file
        }catch (e: IOException){
            e.printStackTrace()
            throw  e
        }
    }



}