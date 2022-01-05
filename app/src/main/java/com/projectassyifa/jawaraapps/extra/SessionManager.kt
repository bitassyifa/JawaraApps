//package com.projectassyifa.jawaraapps.extra
//
//import android.content.Context
//import android.content.SharedPreferences
//import com.projectassyifa.jawaraapps.R
//
//class SessionManager(  // Context
//    var _context: Context
//
//) {
//   private val PREF_IS_LOGIN
//    var pref: SharedPreferences
//
//    // Editor for Shared preferences
//    var editor: SharedPreferences.Editor
//
//    // Shared pref mode
//    var PRIVATE_MODE = 0
//
//    companion object {
//        // Sharedpref file name
//        private const val PREF_NAME = "AndroidHivePref"
//
//        // All Shared Preferences Keys
//        private const val IS_LOGIN = "IsLoggedIn"
//
//        // User name (make variable public to access from outside)
//        const val KEY_NAME = "name"
//        const val KEY_ID = "id"
//        // Email address (make variable public to access from outside)
//        const val KEY_EMAIL = "email"
//    }
//
//    // Constructor
//    init {
//        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
//        editor = pref.edit()
//    }
//
//    fun id_user(): String {
//        return pref.getString(KEY_ID,st)
//    }
//}