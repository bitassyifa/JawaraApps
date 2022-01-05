package com.projectassyifa.jawaraapps.withdraw.data

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.projectassyifa.jawaraapps.R
import com.projectassyifa.jawaraapps.config.JawaraApps
import com.projectassyifa.jawaraapps.pickup.layout.StatusPickupActivity
import com.projectassyifa.jawaraapps.withdraw.layout.WithdrawActivity
import de.hdodenhof.circleimageview.CircleImageView

class AdapterListRek (val listRek : List<AccountBankModel>,var activity: Activity) :RecyclerView.Adapter<AccountBankVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountBankVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_listrekening,parent,false)
        (activity.applicationContext as JawaraApps).applicationComponent.inject(this)
        return AccountBankVH(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AccountBankVH, position: Int) {
        val list = listRek[position]
        holder.nama.text = list.nama
        holder.rekening.text = "${list.bank} | ${list.no_rek}"
        val linkFoto = "http://202.62.9.138/jawara_api/photo/bank/${list.bank}"
        Glide.with(holder.itemView)
            .load(linkFoto)
            .centerCrop()
            .into(holder.logo)
        holder.itemView.setOnClickListener {
//            it.context.startActivity(Intent(it.context,WithdrawActivity::class.java))
            val toWd = Intent(it.context, WithdrawActivity::class.java)
            toWd.putExtra(WithdrawActivity.EXTRA_NAMA,list.nama)
            toWd.putExtra(WithdrawActivity.EXTRA_NOREK,list.no_rek)
            toWd.putExtra(WithdrawActivity.EXTRA_BANK,list.bank)
            it.context.startActivity(toWd)
        }
    }

    override fun getItemCount(): Int {
        return listRek.size
    }
}

class AccountBankVH (v: View) : RecyclerView.ViewHolder(v) {
    var nama = v.findViewById<TextView>(R.id.nama_pemilik)
    var rekening = v.findViewById<TextView>(R.id.rekening)
    var logo = v.findViewById<CircleImageView>(R.id.logobank)
}
