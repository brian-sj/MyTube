package com.goodmorningvoca.std.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.goodmorningvoca.std.app.R


class DropDownAdapter(val context: Context, var listItemsTxt : Array<String>) : BaseAdapter(){

    val mInflater : LayoutInflater = LayoutInflater.from(context )
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view :View
        val vh : ItemRowHolder

        if( convertView == null ){
            view = mInflater.inflate( R.layout.spinner_drop_down_menu , parent , false )
            vh = ItemRowHolder(view )
            view?.tag = vh
        }else {
            view = convertView
            vh = view.tag as ItemRowHolder
        }


        val params = view.layoutParams
        params.height = 60
        view.layoutParams = params

        vh.label.text = listItemsTxt.get(position)
        return view
    }

    override fun getCount(): Int {
        return listItemsTxt.size
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    private class ItemRowHolder(row :View?){

        val label : TextView

        init {
            this.label = row?.findViewById(R.id.txtDropDownLabel) as TextView
        }
    }
}