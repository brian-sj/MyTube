package com.goodmorningvoca.std.model

/**
 * Created by briankang on 2018. 3. 15..
 */



class Book {

    var id : Int = 0
    var bookName :String =""
    var cnt : Int = 0
    var grade : Int = 1
    var type : ArrayList<String>? = null


    constructor( id: Int , bookName : String){
        this.id = id
        this.bookName = bookName
    }


    constructor( id: Int , bookName : String, cnt :  Int ){
        this.id = id
        this.bookName = bookName
        this.cnt = cnt
    }

}