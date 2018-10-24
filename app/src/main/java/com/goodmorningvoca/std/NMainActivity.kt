package com.goodmorningvoca.std

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.brian.mytube.R
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_nmain.*
import okhttp3.*
import java.io.File
import java.io.IOException
import java.lang.Exception

class NMainActivity : AppCompatActivity() {

    private lateinit  var title_name  : String



    companion object {
        const val INTENT_ID = "OPTION"
        const val SERVER_URL = "http://d.goodmorningvoca.com/api/upload_profile"

        val TYPE_PROFILE = "PROFILE"
        val TYPE_SETTING = "SETTING"

        fun newIntent( context : Context , type : String ) : Intent {
           val intent = Intent ( context , NMainActivity::class.java )
            //title_name  = type
            intent.putExtra(INTENT_ID, type  )
            return intent
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                txtLevelTitle.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                txtLevelTitle.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                txtLevelTitle.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nmain)
        title_name = intent.getStringExtra(INTENT_ID)
        supportActionBar?.title =  title_name
        supportActionBar?.setDisplayHomeAsUpEnabled(true )

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


        ivProfile.setOnClickListener{
            CropImage.activity()
                    .setGuidelines( CropImageView.Guidelines.ON )
                    .start( this )
        }


    }
    override fun onSupportNavigateUp(): Boolean {
    //d
        onBackPressed()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE ){

            var result = CropImage.getActivityResult( data )
            ivProfile.setImageURI( result.uri )
        }
    }

    private fun uploadImage(file_path  :String  ){
        Log.i("","Attempt to get Fetch JSON")
        //val url : String ="https://api.letsbuildthatapp.com/youtube/home_feed"


        val path = Environment.getExternalStorageDirectory().toString() + "/Pictures"
        val client = OkHttpClient()

        val file  = File(file_path )


        val requrestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("filename" , "profile" )
                .addFormDataPart("upload", file.name , RequestBody.create(MediaType.parse("image/jpg") , file) )
                .build()

        //val request = Request.Builder().url(SERVER_URL).build()
        val request = Request.Builder()
                .url (SERVER_URL)
                .post( requrestBody  )
                .build()


        try {

            //client.newCall(request).execute()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call?, e: IOException?) {
                    Log.e("", "Error ::: ${e?.message}")
                }

                override fun onResponse(call: Call?, response: Response?) {
                    val body = response?.body()?.string()
                    //val gson = GsonBuilder().create()
                    //val homeFeed = gson.fromJson(body, HomeFeed::class.java)

                    runOnUiThread {
                        //  recyclerView.adapter =  VideoDataAdapter(homeFeed)
                    }

                    Log.d("", body)
                }

            })
        }catch (e :Exception ){}


    }

}
