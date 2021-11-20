package com.projectassyifa.jawaraapps.pickup.data

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.projectassyifa.jawaraapps.R
import com.projectassyifa.jawaraapps.config.JawaraApps

class PickupAdapter(var listPick : List<PickupModel>, var activity: Activity) :RecyclerView.Adapter<PickVH>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PickVH {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_pick,parent,false)
        (activity?.applicationContext as JawaraApps).applicationComponent.inject(this)
        return PickVH(view)
    }

    override fun onBindViewHolder(holder: PickVH, position: Int) {
     var daftar = listPick[position]
        holder.idPick.text = daftar.id_pick
        holder.agent.text = daftar.agent
        holder.stsPick.text =daftar.status_pick

    }

    override fun getItemCount(): Int {
        return listPick.size
    }
}

class PickVH (v: View) : RecyclerView.ViewHolder(v) {
    var idPick = v.findViewById<TextView>(R.id.idPick)
    var agent = v.findViewById<TextView>(R.id.agentTv)
    var stsPick = v.findViewById<TextView>(R.id.stsPickTv)
}
