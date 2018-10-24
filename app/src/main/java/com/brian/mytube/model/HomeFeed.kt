package com.brian.mytube.model

/**
 * Created by briankang on 2018. 3. 15..
 */


class HomeFeed(val user : User , val videos : ArrayList<Video>){

}


class User ( val id : Int , val name : String , val link: String  )
{

}

class Video(val id : Int , val name : String , val link:String ,  val imageUrl : String, val numberOfViews : Int , val channel : Channel ){

}

class Channel ( val name : String , val profileImageUrl: String , val numberOfSubscriber : Int ){

}


class CourseLesson(val name : String , val duration : String , val number : Int , val imageUrl: String , val link :String)