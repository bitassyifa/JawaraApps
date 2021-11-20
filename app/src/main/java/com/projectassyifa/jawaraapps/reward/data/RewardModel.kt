package com.projectassyifa.jawaraapps.reward.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RewardModel (
        val nameReward:String,
        val koinReward:String,
        val imageReward:Int
        ) :Parcelable