package com.projectassyifa.jawaraapps.user.data

class UserModel(
    var id_user : String?,
    var username : String?,
    var email : String?,
    var nama : String?,
    var nik : String?,
    var jk : String?,
    var tgl_lahir : String?,
    var no_tlp : String?,
    var foto_diri : String?,
    var foto_ktp : String?,
    var alamat : String?,
    var saldo  : Int = 0,
    var total_minyak : String?,
    var sts_verifikasi : String?,
)
{}

class UpdateUserModel(
    var id_user : String?,
    var nama : String?,
    var nik : String?,
    var jk : String?,
    var tgl_lahir : String?,
    var no_tlp : String?,
//    var foto_diri : String?,
    var foto_ktp : String?,
    var alamat : String?
)
{}