package com.projectassyifa.jawaraapps.withdraw.data

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.projectassyifa.jawaraapps.R
import java.text.NumberFormat
import java.util.*

class AdapterHistoryExpense (val listHistory : List<WithdrawHistoryModel>, var activity : Activity) :
    RecyclerView.Adapter<TransVH>(){
    val localeID = Locale("in", "ID")
    val formatRupiah = NumberFormat.getCurrencyInstance(localeID)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_history_trans,parent,false)
//        (activity.applicationContext as JawaraApps).applicationComponent.inject(this)
        return TransVH(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TransVH, position: Int) {
        var daftar = listHistory[position]
        holder.money.setText("- ${formatRupiah.format(daftar.nominal)}")
        holder.bank.text = daftar.bank
        holder.tgl.text = daftar.status
    }

    override fun getItemCount(): Int {
        return listHistory.size
    }
}

class TransVH  (v: View) : RecyclerView.ViewHolder(v) {
    var money = v.findViewById<TextView>(R.id.money)
    var bank = v.findViewById<TextView>(R.id.oil)
    var tgl = v.findViewById<TextView>(R.id.tgl_trans)
}
