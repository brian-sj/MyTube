package com.goodmorningvoca.std.app.model

import java.io.Serializable


enum class GVCtg {
    ELE , MID , HIGH , SAT
}


class User(val id : String , val school  : String, val profile : String , val name : String , var device_id : String?  , val link: String   )
class Users (val users : List<User>)

class Data {
    companion object {
        const val URL_DOMAIN = "http://d.goodmorningvoca.com"
        const val IMAGE_PATH = "http://d.goodmorningvoca.com/public/voca/image/"
        const val FCM_SCORE_KEY = "fcm_score_key"
        //const val URL_LOGIN =   "http://d.goodmorningvoca.com/api/login"
        //const val URL_GET_MSG = "http://d.goodmorningvoca.com/api/get_msg"
        var logged_info : LoginResult? = null
    }

}


object GlobalVariable{
    var token :String? = null
    var user : User? = null
    var device_id : String? = null
    var ver : Int = 0
    var school_name : String? = "학원이름없음"
    var unlead : Int = 0

    var showWord :Boolean = true
    var showContent : Boolean = true
    var showExtra : Boolean = true
}

class LoginResult(val status : String, val token : Token, val school_name : String?, val user : User)
class Token(val token : String )

class HomeFeedBBS(val bbss:List<BBS> )
class BBS( val idx : Int? , val title : String , val writer : String?  )

//class HomeFeed(val datas : List<Notice>)   나중에 이거로 바꾸어야 한다.
class HomeFeed(val user : User, val videos : ArrayList<Video>, val datas : List<Notice>? )
class WordFeed( val datas : ArrayList<Word>)
class Notice( val idx : Int? , val title : String ="" , val writer : String? , val test_date : String?
              , val test_code : String ?, val type : String?, val my_score : Int =0 , val word_count :Int =0
              , val a : Int?=0 , val b : Int? =0, val c : Int?=0 , val d : Int? =0 , val e : Int? =0 )

class Score  ( val total_cnt : Int?, val atype : Int? , val btype : Int? , val ctype : Int? , val dtype : Int? , val etype : Int? , var avg : Int?   , val test_date : String? , val level_str : String?   ) : Serializable

class Level(val title:String  , val level : Int? , val seq : Int , val cleared: Boolean? )

class Word(val id_no : Int , val seq :Int , val means_cnt : Int ,  val word : String , val meaning : String , val content : String
           , val filename1 : String? , val filename2 : String?
           , val mean1 :String? , val mean2 :String?, val mean3 :String?, val mean4 :String?, val mean5 :String?, val mean6 :String?, val mean7 :String?, val mean8 :String?   )
class Video(val id : Int , val name : String , val link:String ,  val imageUrl : String, val numberOfViews : Int , val channel : Channel){}
class Channel ( val name : String , val profileImageUrl: String , val numberOfSubscriber : Int ){}
class CourseLesson(val name : String , val duration : String , val number : Int , val imageUrl: String , val link :String)



