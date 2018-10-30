package com.goodmorningvoca.std.app.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.goodmorningvoca.std.app.R
import com.goodmorningvoca.std.app.WordCardActivity
import com.goodmorningvoca.std.app.model.HomeFeed
import com.goodmorningvoca.std.app.model.Video
import com.squareup.picasso.Picasso

/**
 * Created by briankang on 2018. 3. 15..
 */
class VideoDataAdapter(var homeFeed : HomeFeed) : RecyclerView.Adapter<VideoDataAdapter.VideoViewHolder> (){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val v = LayoutInflater.from(parent.context).inflate( R.layout.item_video , parent , false )
        return VideoViewHolder(v)
    }
    override fun getItemCount(): Int {
        return homeFeed.videos.count()
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {

        val video = homeFeed.videos.get(position)
        holder.video = video
        holder.bindItems( video )

        Picasso.with(holder.iview.context).load( video.imageUrl).into( holder.imageViewTitle)
        Picasso.with(holder.iview.context).load( video.channel.profileImageUrl).into( holder.imageViewProfile)

    }
    class VideoViewHolder(itemView : View  , var video : Video? =null  ) : RecyclerView.ViewHolder(itemView) {

        companion object {
            val VIDEO_TITLE_KEY ="VIDEO_TITLE"
            val VIDEO_ID_KEY = "VIDEO_ID"
        }
        val iview = itemView
        val textViewTitle : TextView
        val textViewSub : TextView
        val imageViewTitle : ImageView
        val imageViewProfile : ImageView
        //val textViewDesc : TextView
        init{
            textViewTitle = itemView.findViewById(R.id.tvTitle) as TextView
            textViewSub = itemView.findViewById(R.id.tvSub) as TextView
             imageViewTitle = itemView.findViewById(R.id.imageViewTitle) as ImageView
             imageViewProfile = itemView.findViewById(R.id.imageViewProfile) as ImageView
            //textViewDesc = itemView.findViewById(R.id.tvDesc) as TextView

            //textViewDesc.text = item.numberOfViews.toString()
            iview.setOnClickListener( View.OnClickListener {



                //val intent = Intent(iview.context , DetailActivity::class.java      )
                /// WordCard Activity 로 날라간다
                val intent = Intent(iview.context , WordCardActivity::class.java               )
                intent.putExtra(VIDEO_TITLE_KEY, video?.name )
                intent.putExtra(VIDEO_ID_KEY, video?.id )

                Log.d("bind...","###Hellow... ${video?.name}" )
                //Toast.makeText( iview.context , "name ${item.name}" , Toast.LENGTH_LONG).show()


                iview.context.startActivity(intent )
            })


        }
        fun bindItems( item : Video){
            textViewTitle.text = item.name
            textViewSub.text = item.channel.name +" 20K Views \n 4 days ago"
        }
    }
}