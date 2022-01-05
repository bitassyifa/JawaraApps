package com.projectassyifa.jawaraapps.pickup.data

class PickModelInsert(
    var id_user : String,
    var id_agent : String,
    var alamat : String ,
    var note : String ,
    var longitude : Double,
    var latitude : Double
)
{}

class PickupModel (
    var id_pick : String,
    var username : String,
    var agent : String,
    var alamat : String ,
    var note : String ,
    var tanggal : String,
    var status_pick : String
        )
{}

class PickStatus(
    var id_sts_pick : String,
    var id_pickup : String,
    var status_pick : String,
    var keterangan : String,
    var date : String
){}

class WadahModel(
    var id_wadah : String ,
    var wadah : String,
    var berat : String
){}