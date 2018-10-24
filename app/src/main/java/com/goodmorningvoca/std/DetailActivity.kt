package com.goodmorningvoca.std

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.LinearLayout
import com.goodmorningvoca.std.adapter.VideoDataAdapter.VideoViewHolder
import com.goodmorningvoca.std.adapter.VideoDetailAdapter
import com.goodmorningvoca.std.model.CourseLesson
import com.brian.mytube.R
import com.google.gson.GsonBuilder


import okhttp3.*
import java.io.IOException

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail)

        val navBarTitle = intent.getStringExtra( VideoViewHolder.VIDEO_TITLE_KEY)
        supportActionBar?.title = navBarTitle


        val recyclerView = findViewById(R.id.rvVideos) as RecyclerView

        recyclerView.layoutManager = LinearLayoutManager ( this, LinearLayout.VERTICAL , false )

        fetchJSON( recyclerView)
        /*
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        */
    }
    private fun fetchJSON( recyclerView : RecyclerView){

        val videoId = intent.getIntExtra(VideoViewHolder.VIDEO_ID_KEY ,-1)
        val couseDetailUrl = "http://api.letsbuildthatapp.com/youtube/course_detail?id=" + videoId


        val client = OkHttpClient()
        val request = Request.Builder().url(couseDetailUrl).build()

        client.newCall(request).enqueue( object:Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                Log.e("DATA ERROR " , e?.message )
            }

            override fun onResponse(call: Call?, response: Response?) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                val body = response?.body()?.string()

                Log.i("",body)

                val gson = GsonBuilder().create()
                val courseLessons = gson.fromJson( body , Array<CourseLesson>::class.java)

                runOnUiThread {
                    recyclerView.adapter = VideoDetailAdapter(courseLessons)
                }
            }

        })
    }

}
