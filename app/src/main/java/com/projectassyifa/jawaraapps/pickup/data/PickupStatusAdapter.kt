package com.projectassyifa.jawaraapps.pickup.data

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.projectassyifa.jawaraapps.R

class PickupStatusAdapter(var listStatus : List<PickStatus>,var activity: Activity):RecyclerView.Adapter<StatusPickPH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusPickPH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_status_pick,parent,false)
        return StatusPickPH(view)
    }

    override fun onBindViewHolder(holder: StatusPickPH, position: Int) {
        var positionList = listStatus[position]
        holder.nameStatus.text = positionList.status_pick
        holder.ketStatus.text = positionList.keterangan
        holder.tglStatus.text = positionList.date

        var linkFoto = "http://202.62.9.138/jawara_api/photo/sts_pick/${positionList.id_sts_pick}"
        Glide.with(holder.itemView)
            .load(linkFoto)
            .transition(DrawableTransitionOptions.withCrossFade())
            .centerCrop()
            .apply(RequestOptions().override(50,50))
            .placeholder(R.drawable.ic_re)
            .into(holder.imageStatus)

    }

    override fun getItemCount(): Int {
     return listStatus.size
    }
}

class StatusPickPH (v:View) : RecyclerView.ViewHolder(v) {
    var imageStatus = v.findViewById<ImageView>(R.id.img_sts)
    var nameStatus = v.findViewById<TextView>(R.id.name_sts)
    var ketStatus = v.findViewById<TextView>(R.id.ket_sts)
    var tglStatus = v.findViewById<TextView>(R.id.tgl_sts)
}
