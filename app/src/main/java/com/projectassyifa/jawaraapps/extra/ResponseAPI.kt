package com.projectassyifa.jawaraapps.extra

class ResponseAPI  (
    var status : Boolean = true,
    var message : String ="",
    var errors : Any,
    var data : Any
) {}

class MessageError(
    var type : String,
    var field : String
)
{}