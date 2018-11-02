package com.goodmorningvoca.std.app

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import com.goodmorningvoca.std.app.adapter.MainAdapter
import com.goodmorningvoca.std.app.adapter.WordDataAdapter
import com.goodmorningvoca.std.app.model.Data
import com.goodmorningvoca.std.app.model.GlobalVariable
import com.goodmorningvoca.std.app.model.HomeFeed
import com.goodmorningvoca.std.app.model.WordFeed
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult


import com.google.gson.GsonBuilder

import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
        var token : String?= null
        var _number : Int = 2
        var isStudy : Boolean = false
        private val LOG_TAG =   "MAIN_ACTIViTY"
    //val mContext : Context? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(
                this@MainActivity , OnSuccessListener<InstanceIdResult> {
            instanceIdResult ->
            val device_id = instanceIdResult.token
            if( !device_id.isNullOrEmpty()  &&  !GlobalVariable.user!!.device_id.equals(device_id)){
                registerDeviceId( device_id)
            }else{
                Log.e(LOG_TAG , "같으면 등록 안함... ")
            }
        }

        )

        //mContext = applicationContext()

        ////##1 get ui
        val recyclerView = findViewById(R.id.rvVideos) as RecyclerView

        /// ##2. get Layout Manager
        recyclerView.layoutManager = LinearLayoutManager ( this, LinearLayout.VERTICAL , false )

        /// ##3. set Adapter
        // adapter in fetchJsonData
        fetchJsonData( recyclerView)

        ////## 4 Add ClickListener

        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.action_item1 -> {
                    isStudy = true
                    Toast.makeText( this , "Study"  , Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.action_item2 ->{
                    val intent = Intent(this , StudyActivity::class.java )
                    startActivity(intent)
                    //isStudy = false
                    //Toast.makeText( this , "Exam"  , Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.action_item4 ->{
                    val intent = Intent(this , WordCardActivity::class.java )
                    startActivity(intent)
                    //isStudy = false
                    //Toast.makeText( this , "Exam"  , Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = NMainActivity.newIntent(this, NMainActivity.TYPE_SETTING)
                startActivity( intent)
                true
            }
            R.id.action_notices ->{


                true
            }
            R.id.action_logout ->{
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    //// TOKEN 을 서버에 등록한다...
    fun registerDeviceId( device_id: String ){
        val url = Data.PATH_DEVICE_ADD +  device_id
        token = GlobalVariable.token
        if( GlobalVariable.token.isNullOrEmpty() ){
            return
        }
        val request = Request.Builder()
                .addHeader("Authorization","Bearer "+ GlobalVariable.token)
                .url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object:Callback{

            override fun onFailure(call: Call, e: IOException) {
                //
            }

            override fun onResponse(call: Call, response: Response) {
                //
            }
        })



    }

    fun fetchJsonData( recyclerView : RecyclerView ){

        Log.i("","Attempt to get Fetch JSON")
        //val url : String ="https://api.letsbuildthatapp.com/youtube/home_feed"
        //val url : String ="http://d.goodmorningvoca.com/api/study_by_level/3"
        val url : String = Data.PATH_SCORE

        val client = OkHttpClient()
        val request = Request.Builder()
                .addHeader("Authorization","Bearer "+ GlobalVariable.token)
                .url(url).build()

        //client.newCall(request).execute()
        client.newCall(request).enqueue(object: Callback{
            override fun onFailure(call: Call?, e: IOException?) {
                Log.e("","Error ::: ${e?.message}" )
            }
            override fun onResponse(call: Call?, response: Response?) {
                val body = response?.body()?.string()
                val gson = GsonBuilder().create()
                val homeFeed = gson.fromJson(body , HomeFeed::class.java)
                //val homeFeed = gson.fromJson(body , WordFeed::class.java)
                runOnUiThread {
                    //recyclerView.adapter = VideoDataAdapter(homeFeed)
                    //recyclerView.adapter = WordDataAdapter(homeFeed)
                    recyclerView.adapter = MainAdapter(homeFeed)
                }
                Log.d("",body)
            }

        })



    }
}
