package com.projectassyifa.jawaraapps.maps.data

import android.content.Context
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class AgentVM @Inject constructor(var agentRepo: AgentRepo ) {
    var agentResponse : MutableLiveData<List<AgentModel>> = agentRepo.agentResponse

    fun agent(token: String,context: Context){
        agentRepo.agent(token, context)
    }
}