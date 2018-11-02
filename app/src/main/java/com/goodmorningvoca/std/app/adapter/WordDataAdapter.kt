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
import com.goodmorningvoca.std.app.model.Data
import com.goodmorningvoca.std.app.model.WordFeed
import com.goodmorningvoca.std.app.model.Word
import com.goodmorningvoca.std.app.ui.RoundedTransformation
import com.squareup.picasso.Picasso

/**
 * Created by briankang on 2018. 3. 15..
 */
class WordDataAdapter(var homeFeed : WordFeed) : RecyclerView.Adapter<WordDataAdapter.VideoViewHolder> (){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val v = LayoutInflater.from(parent.context).inflate( R.layout.item_myword , parent , false )
        return VideoViewHolder(v)
    }
    override fun getItemCount(): Int {
        return homeFeed.datas.count()
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val data = homeFeed.datas.get(position)
        holder.word = data
        holder.bindItems( data , position+1  )

        Picasso.with(holder.iview.context).load( Data.IMAGE_PATH + data.filename2)
                .transform(RoundedTransformation(10, 4))
                .into( holder.imageViewTitle)
        //Picasso.with(holder.iview.context).load( Data.IMAGE_PATH + data.filename2).into( holder.imageViewProfile)

    }


    fun setDatas( dataWrapper  : WordFeed  ){
        this.homeFeed = dataWrapper
    }

    fun getItemAt( position : Int  ) : Word {
        return homeFeed.datas.get( position ) as Word
    }

    fun removeItemAt(position : Int ) {
        //if( holder!!.parent )
        homeFeed.datas.removeAt( position )
        notifyItemRemoved( position )
        notifyItemRangeChanged( position , homeFeed.datas.size )

    }

    class VideoViewHolder(itemView : View  , var word : Word? =null  ) : RecyclerView.ViewHolder(itemView) {

        companion object {
            val VIDEO_TITLE_KEY ="VIDEO_TITLE"
            val VIDEO_ID_KEY = "VIDEO_ID"
        }
        val iview = itemView
        val textViewTitle : TextView
        val textViewSub : TextView
        val textViewSeq : TextView
        val imageViewTitle : ImageView
        //val imageViewProfile : ImageView
        //val textViewDesc : TextView
        init{
            textViewTitle = itemView.findViewById(R.id.tvTitle) as TextView
            textViewSub = itemView.findViewById(R.id.tvSub) as TextView
            textViewSeq = itemView.findViewById(R.id.tv_myword_seq) as TextView
             imageViewTitle = itemView.findViewById(R.id.iv_main) as ImageView
             //imageViewProfile = itemView.findViewById(R.id.iv_wordImage) as ImageView
            //textViewDesc = itemView.findViewById(R.id.tvDesc) as TextView
            /*
            iview.setOnClickListener( View.OnClickListener {
                //val intent = Intent(iview.context , DetailActivity::class.java      )
                /// WordCard Activity 로 날라간다
                val intent = Intent(iview.context , WordCardActivity::class.java               )
                intent.putExtra(VIDEO_TITLE_KEY, word?.word )
                intent.putExtra(VIDEO_ID_KEY, word?.id_no )
                Log.d("bind...","###Hellow... ${word?.word}" )
                //Toast.makeText( iview.context , "name ${item.name}" , Toast.LENGTH_LONG).show()
                iview.context.startActivity(intent )
            })
            */
        }
        fun bindItems( item : Word , cnt : Int? =0 ){
            textViewSeq.text   = cnt.toString()
            textViewTitle.text =  item.word
            textViewSub.text   =   item.meaning
        }
    }
}