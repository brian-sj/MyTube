package com.goodmorningvoca.std.app.adapter

import android.content.Context
import android.graphics.Color
import android.opengl.Visibility
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.goodmorningvoca.std.app.R
import com.goodmorningvoca.std.app.model.Data
import com.goodmorningvoca.std.app.model.GlobalVariable
import com.goodmorningvoca.std.app.model.Word
import com.squareup.picasso.Picasso
import org.sufficientlysecure.htmltextview.HtmlTextView

class SliderAdapter : PagerAdapter{

    var context: Context
    public var words : ArrayList<Word>
    lateinit var inflater : LayoutInflater


    constructor(context: Context ,  datas : ArrayList<Word>) : super (){
        this.context = context
        this.words = datas
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean  {
      //return view == `object` as RelativeLayout
        return view == `object` as ConstraintLayout
    }

    override fun getCount(): Int {
        return words.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var image : ImageView
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view : View = inflater.inflate(R.layout.slider_word_item , container , false )
        image =    view.findViewById(R.id.slider_image)
        var word = view.findViewById<TextView>( R.id.tv_study_slider_word)
        var meaning = view.findViewById<TextView>(R.id.tv_study_slider_meaning)


        val ctg = view.findViewById<TextView>(R.id.tv_study_slider_ctg)
        val part = view.findViewById<TextView>(R.id.tv_study_slider_part)
        val seq = view.findViewById<TextView>(R.id.tv_study_slider_seq)
        var content = view.findViewById<HtmlTextView>(R.id.tv_study_slider_content)
        //image.setBackgroundResource( words[position].filename2)

        Picasso.with(image.context).load( Data.IMAGE_PATH + words[position].filename2).into(image)

        val wd : Word = words[position]

        word.text = wd?.word
        meaning.text = wd?.meaning
        content.setHtml( wd?.content)
        ctg.text = wd?.ctg
        part.text = wd?.part
        seq.text = wd?.seq?.toString()


        word   .setTextColor ( if(GlobalVariable.showWord)    ContextCompat.getColor(context , R.color.common_google_signin_btn_text_dark_focused) else Color.parseColor("#ffffff"))  //GlobalVariable.showWord
        meaning.setTextColor ( if(GlobalVariable.showMeaning) ContextCompat.getColor(context , R.color.common_google_signin_btn_text_dark_focused) else Color.parseColor("#ffffff"))
        content.setTextColor ( if(GlobalVariable.showExtra)   ContextCompat.getColor(context , R.color.common_google_signin_btn_text_dark_focused) else Color.parseColor("#ffffff"))

        container!!.addView(view)
        return view

    }

    fun getWord (position :Int ) : Word
    {
        return words[position] as Word
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container!!.removeView(`object` as ConstraintLayout )
    }


}