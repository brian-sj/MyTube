package com.goodmorningvoca.std

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import com.goodmorningvoca.std.adapter.VideoDataAdapter
import com.goodmorningvoca.std.model.HomeFeed
import com.brian.mytube.R
import com.goodmorningvoca.std.adapter.WordDataAdapter
import com.goodmorningvoca.std.model.GlobalVariable
import com.goodmorningvoca.std.model.WordFeed


import com.google.gson.GsonBuilder

import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

        var _number : Int = 2
        var isStudy : Boolean = false
    //val mContext : Context? =null
    //


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)



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
                    isStudy = false
                    Toast.makeText( this , "Exam"  , Toast.LENGTH_SHORT).show()
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

    fun fetchJsonData( recyclerView : RecyclerView ){

        Log.i("","Attempt to get Fetch JSON")
        //val url : String ="https://api.letsbuildthatapp.com/youtube/home_feed"
        val url : String ="http://d.goodmorningvoca.com/api/study_by_level/3"

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
                //val homeFeed = gson.fromJson(body , HomeFeed::class.java)
                val homeFeed = gson.fromJson(body , WordFeed::class.java)
                runOnUiThread {
                    //recyclerView.adapter = VideoDataAdapter(homeFeed)
                    recyclerView.adapter = WordDataAdapter(homeFeed)
                }
                Log.d("",body)
            }

        })



    }
}
