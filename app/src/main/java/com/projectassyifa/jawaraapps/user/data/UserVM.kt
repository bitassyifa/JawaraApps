package com.projectassyifa.jawaraapps.user.data

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.projectassyifa.jawaraapps.extra.ResponseAPI
import java.io.File
import javax.inject.Inject

class UserVM @Inject constructor(var userRepo: UserRepo) {
    var userData : MutableLiveData<UserModel> = userRepo.userResponse
    var userApi : MutableLiveData<ResponseAPI> = userRepo.resApi

    fun userById(token : String, id:String,context: Context){
        userRepo.userById(token, id,context)
    }

    fun userUpdate(token: String,updateUserModel: UpdateUserModel,context: Context,file : File){
        userRepo.userUpdate(token,updateUserModel,context,file)
    }
    fun userUpdateNoPhoto(token: String,updateUserModel: UpdateUserModel,context: Context){
        userRepo.userUpdateNoPhoto(token,updateUserModel,context)
    }
}