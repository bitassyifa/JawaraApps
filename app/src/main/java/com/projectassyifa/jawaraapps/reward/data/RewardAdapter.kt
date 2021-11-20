package com.projectassyifa.jawaraapps.reward.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.projectassyifa.jawaraapps.R
import de.hdodenhof.circleimageview.CircleImageView

class RewardAdapter(val listReward : ArrayList<RewardModel>) : RecyclerView.Adapter<RewardVH>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardVH {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.adapter_reward,parent,false)
        return RewardVH(view)
    }

    override fun onBindViewHolder(holder: RewardVH, position: Int) {
       val rewardPosition = listReward[position]
        holder.name.text = rewardPosition.nameReward
        holder.koin.text = rewardPosition.koinReward
        Glide.with(holder.itemView.context)
            .load(rewardPosition.imageReward)
            .apply(RequestOptions().override(50,50))
            .into(holder.image)

    }

    override fun getItemCount(): Int {
        return listReward.size
    }
}

class RewardVH(itemView: View) : RecyclerView.ViewHolder(itemView)  {
    var name : TextView = itemView.findViewById(R.id.name_reward)
    var koin : TextView = itemView.findViewById(R.id.koin_reward)
    var image : CircleImageView = itemView.findViewById(R.id.reward_image)

}
