package com.goodmorningvoca.std.app.adapter

import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.goodmorningvoca.std.app.DetailActivity
import com.goodmorningvoca.std.app.R
import com.goodmorningvoca.std.app.WordCardActivity
import com.goodmorningvoca.std.app.model.*
import com.goodmorningvoca.std.app.ui.RoundedTransformation
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_score_message.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by briankang on 2018. 3. 15..
 */
class MainAdapter(var homeFeed : HomeFeed) : RecyclerView.Adapter<MainAdapter.CustomViewHolder> (){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val v = LayoutInflater.from(parent.context).inflate( R.layout.item_score_message , parent , false )
        return CustomViewHolder(v)
    }
    override fun getItemCount(): Int {
        return homeFeed.datas?.size ?:0
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = homeFeed.datas?.get(position) as Notice
        //holder.word = data
        holder.bindItems( item , position+1  )


        if (item.a !=null){
            holder?.itemView?.tv_type_a_desc?.text =  "연상 : ${item.a} 점"
            holder?.itemView?.tv_type_a_desc?.setTextColor(  Color.parseColor("#000000"))
        } else {
            holder?.itemView?.tv_type_a_desc?.text = "연상 : - "
            holder?.itemView?.tv_type_a_desc?.setTextColor(  Color.parseColor("#cccccc"))
        }
        if (item.b !=null){
            holder?.itemView?.tv_type_b_desc?.text =  "스펠링 : ${item.b} 점"
            holder?.itemView?.tv_type_b_desc?.setTextColor(  Color.parseColor("#000000"))
        }else {
            holder?.itemView?.tv_type_b_desc?.text = "스펠링 : - "
            holder?.itemView?.tv_type_b_desc?.setTextColor(  Color.parseColor("#cccccc"))
        }
        if (item.c !=null){
            holder?.itemView?.tv_type_c_desc?.text =   "발음 : ${item.c} 점"
            holder?.itemView?.tv_type_c_desc?.setTextColor(  Color.parseColor("#000000"))
        }else {
            holder?.itemView?.tv_type_c_desc?.text = "발음 : -"
            holder?.itemView?.tv_type_c_desc?.setTextColor(  Color.parseColor("#cccccc"))
        }
        if (item.d !=null){
            holder?.itemView?.tv_type_d_desc?.text =   "뜻 : ${item.d} 점"
            holder?.itemView?.tv_type_d_desc?.setTextColor(  Color.parseColor("#000000"))
        }else {
            holder?.itemView?.tv_type_d_desc?.text = "뜻 : -"
            holder?.itemView?.tv_type_d_desc?.setTextColor(  Color.parseColor("#cccccc"))
        }
        if (item.e !=null){
            holder?.itemView?.tv_type_e_desc?.text =  "다의어 : ${item.e} 점"
            holder?.itemView?.tv_type_e_desc?.setTextColor(  Color.parseColor("#000000"))
        }else{
            holder?.itemView?.tv_type_e_desc?.text =  "다의어 : -"
            holder?.itemView?.tv_type_e_desc?.setTextColor(  Color.parseColor("#cccccc"))
        }

        //Picasso.with(holder.iview.context).load( Data.IMAGE_PATH + data.filename2).into( holder.imageViewProfile)
    }



    class CustomViewHolder(itemView : View  , var notice  : Notice? =null  ) : RecyclerView.ViewHolder(itemView) {
        private val LOG_TAG ="MAIN ADAPTER"
        companion object {
            val TITLE_KEY ="NOTICE_TITLE"
            val URL_KEY = "NOTICE_ID"
        }

        val tv_date : TextView
        val tv_type : TextView
        val tv_my_score : TextView
        val tv_word_cnt : TextView
        val textViewf_new : TextView
        //val imageViewProfile : ImageView
        //val textViewDesc : TextView
        init{
            tv_date = itemView.findViewById(R.id.tv_date) as TextView
            tv_type = itemView.findViewById(R.id.tv_type) as TextView
            tv_my_score = itemView.findViewById(R.id.tv_my_score) as TextView
            tv_word_cnt = itemView.findViewById(R.id.tv_word_cnt) as TextView
            textViewf_new = itemView.findViewById(R.id.textViewf_new) as TextView


            itemView.setOnClickListener{
                val intent = Intent(itemView.context , DetailActivity::class.java)

                if(notice?.type == Data.STRING_BATTLE){
                    intent.putExtra( TITLE_KEY , "${notice?.test_date}일 학원단어배틀 성적" )
                    intent.putExtra( URL_KEY , Data.PATH_SCORE_BATTLE + notice?.test_code )
                }else {
                    intent.putExtra( TITLE_KEY , "${notice?.test_date}일 집중학습 성적" )
                    val dt = notice?.test_date?.replace("/" ,"-", false )
                    intent.putExtra( URL_KEY , Data.PATH_SCORE_SCHEDULED + dt )
                }
                itemView.context.startActivity(intent )
            }

        }
        fun bindItems( item : Notice , cnt : Int? =0 ){
            tv_date.text   = item.test_date
            tv_type.text =  item.type
            tv_my_score.text   =   item.my_score.toString()
            tv_word_cnt.text   = item.word_count .toString()

            val to_day = Calendar.getInstance().time
            val format = SimpleDateFormat("MM월 dd일")
            var to_date_string = ""

            try {
                to_date_string = format.format(to_day)
            }catch ( e : Exception){
                Log.e(LOG_TAG , e.message)
            }


            if( to_date_string.equals( item.test_date)){
                textViewf_new?.visibility = View.VISIBLE
            }else{
                textViewf_new?.visibility = View.INVISIBLE
            }


        }
    }
}