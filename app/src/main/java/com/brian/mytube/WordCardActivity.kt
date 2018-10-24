package com.brian.mytube

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.LinearLayout
import com.brian.mytube.adapter.VideoDataAdapter
import com.brian.mytube.adapter.VideoDetailAdapter
import com.brian.mytube.model.CourseLesson
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

class WordCardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_card)

        val navBarTitle = intent.getStringExtra( VideoDataAdapter.VideoViewHolder.VIDEO_TITLE_KEY)
        supportActionBar?.title = navBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true )


        val recyclerView = findViewById(R.id.rvCards ) as RecyclerView

        recyclerView.layoutManager = LinearLayoutManager ( this, LinearLayout.VERTICAL , false )

        fetchJSON( recyclerView)
    }

    override fun onSupportNavigateUp(): Boolean {

        onBackPressed()
        return true
    }

    private fun fetchJSON( recyclerView : RecyclerView){

        val videoId = intent.getIntExtra(VideoDataAdapter.VideoViewHolder.VIDEO_ID_KEY ,-1)
        val couseDetailUrl = "http://api.letsbuildthatapp.com/youtube/course_detail?id=" + videoId


        val client = OkHttpClient()
        val request = Request.Builder().url(couseDetailUrl).build()

        client.newCall(request).enqueue( object: Callback {
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
