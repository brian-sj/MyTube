package com.goodmorningvoca.std.app.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.goodmorningvoca.std.app.R
import com.goodmorningvoca.std.app.model.CourseLesson
import com.squareup.picasso.Picasso

//import com.brian.mytube.VideoDetailAdapter.CourseLessonViewHolder



/**
 * Created by briankang on 2018. 3. 19..
 */

//class VideoDataAdapter(var homeFeed : HomeFeed)                   : RecyclerView.Adapter<VideoDataAdapter.VideoViewHolder> (){



class VideoDetailAdapter (val courseLessons : Array<CourseLesson>) : RecyclerView.Adapter<VideoDetailAdapter.CourseLessonViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseLessonViewHolder {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val layoutInfator = LayoutInflater.from(parent?.context)
        val customView  = layoutInfator.inflate(R.layout.item_course_lesson , parent , false )

        return CourseLessonViewHolder(customView)
    }

    override fun getItemCount(): Int {
        return courseLessons.count()
    }

    override fun onBindViewHolder(holder: CourseLessonViewHolder, position: Int) {
        val cl = courseLessons.get(position)

        holder.textViewTitle.text = cl.name
        holder.textViewSub.text = "Episode #${cl.number}"
        holder.textViewDuration.text = cl.duration

        Picasso.with(holder.customView.context ).load(cl.imageUrl).into(holder.imageView)
    }


    class CourseLessonViewHolder(val customView : View )  : RecyclerView.ViewHolder(customView) {
        val textViewTitle : TextView
        val textViewDuration :TextView
        val textViewSub :TextView

        val imageView : ImageView

        init{
            textViewTitle = customView.findViewById( R.id.tvTitle )
            textViewDuration = customView.findViewById(R.id.tvDuration)
            textViewSub = customView.findViewById(R.id.tvSub)
            imageView = customView.findViewById(R.id.imageViewCourseLesson )
            customView.setOnClickListener{

            }
        }
    }


}

