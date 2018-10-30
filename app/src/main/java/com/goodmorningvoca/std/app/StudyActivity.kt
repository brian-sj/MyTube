package com.goodmorningvoca.std.app

import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.goodmorningvoca.std.app.adapter.SliderAdapter
import com.goodmorningvoca.std.app.adapter.VideoDataAdapter
import com.goodmorningvoca.std.app.adapter.VideoDetailAdapter
import com.goodmorningvoca.std.app.model.CourseLesson
import com.goodmorningvoca.std.app.model.GlobalVariable
import com.goodmorningvoca.std.app.model.Word
import com.goodmorningvoca.std.app.model.WordFeed
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_study.*
import okhttp3.*
import java.io.IOException
import java.lang.Exception

class StudyActivity : AppCompatActivity() {


    lateinit var adapter : PagerAdapter
    var nowPage : Int = 0
    var pageState : Int = 0 // 플립 되었는가 ?
    var ableFlip : Boolean = true
    var level = 1

    private val LOG_TAG = "STUDY_ACTIVITY"

    companion object {
        val Level_ID = "LEVEL_PARAM_ID"
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.action_item_study1 -> {
                //message.setText(R.string.title_home)
                GlobalVariable.showWord = !GlobalVariable.showWord
                return@OnNavigationItemSelectedListener true
            }
            R.id.action_item_study2 -> {
                //message.setText(R.string.title_dashboard)
                GlobalVariable.showContent = !GlobalVariable.showContent
                return@OnNavigationItemSelectedListener true
            }
            R.id.action_item_study3 -> {
                //message.setText(R.string.title_notifications)
                GlobalVariable.showExtra = !GlobalVariable.showExtra
                return@OnNavigationItemSelectedListener true
            }
            R.id.action_item_study4 -> {
                //message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study)


        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


        study_pager.addOnPageChangeListener(  object  : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
                Log.d(LOG_TAG , "STATE_CHANGE" + state.toString()  )
                pageState = state
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                if( true   ) {

                    Log.d(LOG_TAG , "Page Scrolled " + position.toString() +" : "+ adapter?.count.toString()  )
                    if (nowPage +1 == adapter?.count  && position +1 == adapter?.count  && pageState ==1) {
                        Log.d(LOG_TAG, " 다음 페이지 가고 싶다.. 너무나..  ")

                        if( ableFlip){
                            ableFlip = false
                            level++
                            fetchJSON( level.toString() )
                        }
                    }
                }else {
                    Log.d(LOG_TAG , "아직 안 initialized   "    )
                }
            }

            override fun onPageSelected(position: Int) {
                Log.d(LOG_TAG , "PAGE SELECTED" + position.toString() )
                nowPage = position
            }

        } )
        level  = intent.getIntExtra(  StudyActivity.Level_ID ,-1)
        fetchJSON( level.toString() )
    }


    private fun fetchJSON( levelId : String   ){


        val wordsUrl = "http://d.goodmorningvoca.com/api/study_by_level/" + levelId

        val client = OkHttpClient()
        //val request = Request.Builder().url(couseDetailUrl).build()
        val request = Request.Builder()
                .addHeader("Authorization","Bearer "+ GlobalVariable.token)
                .url(wordsUrl).build()

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
                try {
                    val homeFeed = gson.fromJson(body, WordFeed::class.java)

                    runOnUiThread {
                        //recyclerView.adapter = VideoDetailAdapter(courseLessons)
                        adapter = SliderAdapter( applicationContext , homeFeed.datas)
                        study_pager.adapter = adapter
                        ableFlip = true
                    }
                }catch (e:Exception){
                    Log.i(LOG_TAG , e.message )
                }
            }

        })
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
