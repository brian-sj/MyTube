package com.goodmorningvoca.std.app

import android.arch.lifecycle.ViewModelProvider
import android.graphics.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import com.goodmorningvoca.std.app.adapter.VideoDataAdapter
import com.goodmorningvoca.std.app.adapter.VideoDetailAdapter
import com.goodmorningvoca.std.app.model.CourseLesson
import com.goodmorningvoca.std.app.R
import com.goodmorningvoca.std.app.adapter.WordDataAdapter
import com.goodmorningvoca.std.app.model.Data
import com.goodmorningvoca.std.app.model.GlobalVariable
import com.goodmorningvoca.std.app.model.WordFeed
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

class WordCardActivity : AppCompatActivity() {

    //private lateinit var words : WordFeed
    private var adapter : WordDataAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_card)

        val navBarTitle = "내 단어장"
        supportActionBar?.title = navBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true )

        val recyclerView = findViewById(R.id.rvCards) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager ( this, LinearLayout.VERTICAL , false )
        fetchJSON( recyclerView)

            //ViewModelProvider.of(this).get(WordFeed::class.java)
        //words =


        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                val word = adapter?.getItemAt(position)
                adapter?.removeItemAt( position )
                fetchJSONdeleteMyCard( word?.id_no ?:0 )
                /*
                if (direction == ItemTouchHelper.LEFT) {
                    adapter!!.removeItem(position)
                } else {
                    removeView()
                    edit_position = position
                    alertDialog!!.setTitle("Edit Name")
                    et_name!!.setText(names[position])
                    alertDialog!!.show()
                }
                */
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

                val icon: Bitmap
                /*
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    val itemView = viewHolder.itemView
                    val height = itemView.bottom.toFloat() - itemView.top.toFloat()
                    val width = height / 3

                    if (dX > 0) {
                        p.color = Color.parseColor("#388E3C")
                        val background = RectF(itemView.left.toFloat(), itemView.top.toFloat(), dX, itemView.bottom.toFloat())
                        c.drawRect(background, p)
                        icon = BitmapFactory.decodeResource(resources, R.drawable.ic_edit_white)
                        val icon_dest = RectF(itemView.left.toFloat() + width, itemView.top.toFloat() + width, itemView.left.toFloat() + 2 * width, itemView.bottom.toFloat() - width)
                        c.drawBitmap(icon, null, icon_dest, p)
                    } else {
                        p.color = Color.parseColor("#D32F2F")
                        val background = RectF(itemView.right.toFloat() + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
                        c.drawRect(background, p)
                        icon = BitmapFactory.decodeResource(resources, R.drawable.ic_delete_white)
                        val icon_dest = RectF(itemView.right.toFloat() - 2 * width, itemView.top.toFloat() + width, itemView.right.toFloat() - width, itemView.bottom.toFloat() - width)
                        c.drawBitmap(icon, null, icon_dest, p)
                    }
                }
                */
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)


    }

    override fun onSupportNavigateUp(): Boolean {

        onBackPressed()
        return true
    }

    private fun fetchJSON( recyclerView : RecyclerView){

        //val videoId = intent.getIntExtra(VideoDataAdapter.VideoViewHolder.VIDEO_ID_KEY ,-1)
        val url = Data.PATH_WORD_MY


        val client = OkHttpClient()

        val request = Request.Builder()
                .addHeader("Authorization","Bearer "+ GlobalVariable.token)
                .url(url).build()

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
                //val courseLessons = gson.fromJson( body , Array<CourseLesson>::class.java)
                val homeFeed = gson.fromJson(body, WordFeed::class.java)

                runOnUiThread {
                    //recyclerView.adapter = VideoDetailAdapter(courseLessons)
                    adapter = WordDataAdapter(homeFeed)
                    recyclerView.adapter = adapter
                    adapter!!.notifyDataSetChanged()
                }
            }

        })
    }

    private fun fetchJSONdeleteMyCard( voca_id_no : Int =0){


        if(voca_id_no == 0 ) return

        val url = Data.PATH_WORD_MY_DELETE +  voca_id_no.toString()
        val client = OkHttpClient()
        val request = Request.Builder()
                .addHeader("Authorization","Bearer "+ GlobalVariable.token)
                .url(url).build()

        client.newCall(request).enqueue( object: Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                Log.e("DATA ERROR " , e?.message )
            }

            override fun onResponse(call: Call?, response: Response?) {

            }
            }
        )
    }
}


