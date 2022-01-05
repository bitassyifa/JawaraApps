package com.projectassyifa.jawaraapps.pickup.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.projectassyifa.jawaraapps.R

class AdapterWadah (var datasource : List<WadahModel>, val context: Context) : BaseAdapter(){
    private val inflater : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
     return datasource.size
    }

    override fun getItem(i: Int): Any {
        return datasource[i]
    }

    override fun getItemId(i: Int): Long {
       return i.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
       val view : View
       val vh : ItemContent

       if (convertView == null){
           view = inflater.inflate(R.layout.adapter_wadah,parent,false)
           vh = ItemContent(view)
           view?.tag = vh
       } else {
           view = convertView
           vh = view.tag as ItemContent
       }
        vh.namaWadah.text = datasource.get(position).wadah
        vh.berat.text = datasource.get(position).berat
        return view
    }

    class ItemContent(row : View?){
        val namaWadah : TextView = row?.findViewById(R.id.namawadah) as TextView
        val berat : TextView = row?.findViewById(R.id.berat) as TextView
    }

}