package com.projectassyifa.jawaraapps.wallet.data

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.projectassyifa.jawaraapps.R
import com.projectassyifa.jawaraapps.config.JawaraApps
import java.text.NumberFormat
import java.util.*


class HistoryTransAdapter (val listHistory : List<HistoryTransModel>, var activity : Activity) :RecyclerView.Adapter<TransVH>(){
    val localeID = Locale("in", "ID")
    val formatRupiah = NumberFormat.getCurrencyInstance(localeID)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_history_trans,parent,false)
        (activity.applicationContext as JawaraApps).applicationComponent.inject(this)
        return TransVH(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TransVH, position: Int) {
        var daftar = listHistory[position]
        holder.money.setText("+ ${formatRupiah.format(daftar.uang)}")
        holder.oil.text = "${daftar.jumlah_minyak} Kg"
        holder.tgl.text = daftar.date
    }

    override fun getItemCount(): Int {
        return listHistory.size
    }
}

class TransVH  (v: View) : RecyclerView.ViewHolder(v) {
    var money = v.findViewById<TextView>(R.id.money)
    var oil = v.findViewById<TextView>(R.id.oil)
    var tgl = v.findViewById<TextView>(R.id.tgl_trans)
}
