package com.goodmorningvoca.std.app.font

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by briankang on 2018. 3. 15..
 */
object FontManager{
    val ROOT : String = "fonts/"
    val FONTAWSOME : String =  ROOT + "fontawsome-webfont.ttf"
    fun getTypeface(ctx : Context , font: String) : Typeface{
        return  Typeface.createFromAsset(ctx.getAssets() , font )
    }

    fun markAsIconContainer(v: View, typeface: Typeface ){
        if(v  is ViewGroup){
             val vg : ViewGroup  = v as ViewGroup


            for( i in 1..vg.getChildCount()  ){
                val child : View = vg.getChildAt(i)
                markAsIconContainer(child, typeface = typeface)
            }

        }else if( v is TextView){
            (v as TextView).setTypeface( typeface)
        }
    }
}
