package com.projectassyifa.jawaraapps.withdraw.data

class WithdrawModel (
    var id_user : String ,
    var no_rek : String ,
    var bank : String ,
    var nama : String,
    var nominal : Int,
    var note : String,
        ){
}

class WithdrawHistoryModel (
    var id_user : String ,
    var no_rek : String ,
    var bank : String ,
    var nama : String,
    var nominal : Int,
    var note : String,
    var status : String
){
}

class ListBankModel(
    var kode_bank : String,
    var nama_bank : String
)
{}

class AccountBankModel(
    var id_user : String ,
    var no_rek : String ,
    var bank : String ,
    var nama : String
){
}
