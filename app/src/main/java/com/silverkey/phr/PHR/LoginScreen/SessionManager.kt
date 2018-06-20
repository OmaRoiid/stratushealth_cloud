package com.silverkey.phr.PHR.LoginScreen

import android.content.Context
import android.content.SharedPreferences
import android.content.Intent


/**
 * TODO: Add class header
 */
class SessionManager {
    // Shared Preferences
    var pref: SharedPreferences? = null
    // Editor for Shared preferences
    var editor: SharedPreferences.Editor? = null
    // Context
    var _context: Context? = null
    // Shared pref mode
    var PRIVATE_MODE = 0
    // Sharedpref file name
    private val PREF_NAME = "PHR"
    // All Shared Preferences Keys
    private val IS_LOGIN = "IsLoggedIn"
    // User name (make variable public to access from outside)
    val KEY_USERNAME = "userName"
    // Email address (make variable public to access from outside)
    val KEY_PASSWORD = "password"


    constructor(_context: Context?) {
        this._context = _context
        pref = _context?.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref?.edit()
    }

    fun createLoginSession(userName: String, password: String) {
        // Storing login value as TRUE
        editor?.putBoolean(IS_LOGIN, true)
        // Storing userName in pref
        editor?.putString(KEY_USERNAME, userName)
        // Storing password in pref
        editor?.putString(KEY_PASSWORD, password)
        // commit changes
        editor?.commit()
    }

    fun checkLogin() {
        // Check login status
        if (!this.isLoggedIn()!!) {
            // user is not logged in redirect him to Login Activity
            val i = Intent(_context, LoginScreen::class.java)
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            // Add new Flag to start new Activity
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            // Staring Login Activity
            _context?.startActivity(i)
        }
    }

    fun getUserDetails(): HashMap<String, String>{
        val user = HashMap<String, String>()
        // user name
        user[KEY_USERNAME] = pref?.getString(KEY_USERNAME,null)!!

        // user email id
        user[KEY_PASSWORD] = pref?.getString(KEY_PASSWORD,null)!!

        // return user
        return user
    }


    fun isLoggedIn(): Boolean? {
        return pref?.getBoolean(IS_LOGIN, false)
    }
}