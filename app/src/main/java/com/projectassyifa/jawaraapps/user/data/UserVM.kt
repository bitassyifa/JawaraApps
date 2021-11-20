package com.projectassyifa.jawaraapps.user.data

import android.content.Context
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class UserVM @Inject constructor(var userRepo: UserRepo) {
    var userData : MutableLiveData<UserModel> = userRepo.userResponse

    fun userById(token : String, id:String,context: Context){
        userRepo.userById(token, id,context)
    }
}