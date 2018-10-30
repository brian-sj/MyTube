package com.goodmorningvoca.std.app.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.goodmorningvoca.std.app.R
import com.goodmorningvoca.std.app.model.Data
import com.goodmorningvoca.std.app.model.Word
import com.squareup.picasso.Picasso
import org.sufficientlysecure.htmltextview.HtmlTextView

class SliderAdapter : PagerAdapter{

    var context: Context
    var words : ArrayList<Word>
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
        var content = view.findViewById<HtmlTextView>(R.id.tv_study_slider_content)
        //image.setBackgroundResource( words[position].filename2)

        Picasso.with(image.context).load( Data.IMAGE_PATH + words[position].filename2).into(image)

        word.text = words[position]?.word
        meaning.text = words[position]?.meaning + "AAAA"
        //content.text =
        content.setHtml( words[position]?.content)


        container!!.addView(view)
        return view

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container!!.removeView(`object` as ConstraintLayout )
    }


}