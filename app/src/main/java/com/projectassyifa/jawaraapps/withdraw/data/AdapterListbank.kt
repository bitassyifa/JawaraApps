package com.projectassyifa.jawaraapps.withdraw.data

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.projectassyifa.jawaraapps.R
import com.projectassyifa.jawaraapps.config.JawaraApps
import com.projectassyifa.jawaraapps.wallet.data.TransVH

class AdapterListbank (val listbank : List<ListBankModel>,var activity: Activity) : RecyclerView.Adapter<ListBankVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListBankVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_listbank,parent,false)
        (activity.applicationContext as JawaraApps).applicationComponent.inject(this)
        return ListBankVH(view)
    }

    override fun onBindViewHolder(holder: ListBankVH, position: Int) {
       val list = listbank[position]
        holder.kode.text = list.kode_bank
        holder.nama.text = list.nama_bank
    }

    override fun getItemCount(): Int {
       return listbank.size
    }
}

class ListBankVH (v:View) : RecyclerView.ViewHolder(v){
    var kode = v.findViewById<TextView>(R.id.kodebank)
    var nama = v.findViewById<TextView>(R.id.namabank)

}
