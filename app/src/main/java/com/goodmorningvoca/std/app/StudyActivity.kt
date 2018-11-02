package com.goodmorningvoca.std.app

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import com.goodmorningvoca.std.app.adapter.SliderAdapter
import com.goodmorningvoca.std.app.adapter.VideoDataAdapter
import com.goodmorningvoca.std.app.adapter.VideoDetailAdapter
import com.goodmorningvoca.std.app.model.*
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_study.*
import okhttp3.*
import java.io.IOException
import java.lang.Exception

class StudyActivity : AppCompatActivity() {


    //lateinit var adapter : PagerAdapter
    lateinit var adapter : SliderAdapter
    var nowPage : Int = 0
    var pageState : Int = 0 // 플립 되었는가 ?
    var ableFlip : Boolean = true        // 플립명령을 내리고 끝나지 않은 상태라면 다음 액션명령을 무시한다.
    var reverseOrder : Boolean = false   // 뒤로 가는 페이지 변경인가?
    //var level = 1
    var mediaPlayer : MediaPlayer? = null


    private val LOG_TAG = "STUDY_ACTIVITY"

    companion object {
        val Level_ID = "LEVEL_PARAM_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        // navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


        tbtn_hide_extra.setOnClickListener {
            GlobalVariable.showExtra = tbtn_hide_extra.isChecked
        }
        tbtn_hide_meaning.setOnClickListener {
            GlobalVariable.showMeaning = tbtn_hide_meaning.isChecked
        }
        tbtn_hide_word.setOnClickListener {
            GlobalVariable.showWord = tbtn_hide_word.isChecked
        }
        btn_word_save.setOnClickListener {
            fetchJSONSaveMyWord()
        }


        study_pager.addOnPageChangeListener(  object  : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
                Log.d(LOG_TAG , "STATE_CHANGE" + state.toString()  )
                pageState = state
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                var level = GlobalVariable.level
                Log.d(LOG_TAG , " page State  ${pageState}; ${ableFlip} : nowPage ${nowPage} Page ${level} Scrolled " + position.toString() +" : "+ adapter?.count.toString()  )

                if(pageState == 1 && ableFlip  ) {

                    //// NEXT PAGE
                    if (nowPage + 1 == adapter?.count && position + 1 == adapter?.count  ) {
                        Log.d(LOG_TAG, " 다음 페이지 가고 싶다.. 너무나..  ${level} : pageState ${nowPage}")
                        if (level < 1050   ) {
                            GlobalVariable.level++
                            fetchJSON( )
                            ableFlip = false
                            reverseOrder = false
                            Log.d(LOG_TAG, " 이후 OK... ")
                        }
                    }

                    ///// PREV PAGE
                    /*
                    if (  position == 0 && nowPage == 0) {
                        Log.d(LOG_TAG, " 이전  페이지 가고 싶다.. 너무나..  ${level} : pageState  ${nowPage}")
                        if (level > 1  ){
                            level--
                            fetchJSON(level.toString())
                            ableFlip = false
                            reverseOrder = true
                            Log.d(LOG_TAG, " 이전 OK... ")
                        }
                    }
                    */
                }
            }
            override fun onPageSelected(position: Int) {
                Log.d(LOG_TAG , "PAGE SELECTED" + position.toString() )
                nowPage = position
                playMp3( )
            }
        } )
        //level  = intent.getIntExtra(  StudyActivity.Level_ID ,-1)
        fetchJSON(  )
    }


    private fun fetchJSON(    ){
        val wordsUrl = Data.LEVEL_PATH + GlobalVariable.level

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
                        if( reverseOrder ){
                            //study_pager.setCurrentItem( adapter.count -1 , true )
                        }else {
                            nowPage = 0
                        }
                        ableFlip = true
                        playMp3()
                    }
                }catch (e:Exception){
                    Log.i(LOG_TAG , e.message )
                }
            }

        })
    }
    private fun fetchJSONSaveMyWord( ) {
        val url = Data.PATH_SAVE_MYWORD + adapter.getWord(nowPage).id_no
        val client = OkHttpClient()
        //val request = Request.Builder().url(couseDetailUrl).build()
        val request = Request.Builder()
                .addHeader("Authorization","Bearer "+ GlobalVariable.token)
                .url(url).build()
        client.newCall(request).enqueue( object: Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                Log.e("DATA ERROR ", e?.message)
            }

            override fun onResponse(call: Call?, response: Response?) {
                val body = response?.body()?.string()
                runOnUiThread {
                    Toast.makeText(applicationContext , "내 단어장에 저장되었습니다." , Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    private fun playMp3(){
        val data = adapter.getWord( nowPage )
        val url = Data.MP3_PATH + data?.filename1
        val url2 = "http://www.all-birds.com/Sound/western%20bluebird.wav"

        if(mediaPlayer != null ){
            mediaPlayer?.stop()
            mediaPlayer = MediaPlayer.create(this , Uri.parse(url))
        } else {
            mediaPlayer = MediaPlayer.create(this , Uri.parse(url))
        }


        //mediaPlayer?.setAudioStreamType( AudioManager.STREAM_MUSIC)
        //mediaPlayer?.setAudioAttributes( AudioAttributes.CONTENT_TYPE_MUSIC )
        //mediaPlayer?.setDataSource(  url2   )
        //mediaPlayer?.prepare()
        mediaPlayer?.start()

        mediaPlayer?.setOnCompletionListener {
            Log.d(LOG_TAG ,"------")
        }
    }
}
