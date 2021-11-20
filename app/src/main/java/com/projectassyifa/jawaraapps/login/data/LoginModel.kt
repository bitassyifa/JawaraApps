package com.projectassyifa.jawaraapps.login.data

class LoginModel(
  var email : String?,
  var password : String?

){}

class LoginResponse (
  var id :String?,
  var token : String?,
  var email: String?,
  var username : String?
        ){}