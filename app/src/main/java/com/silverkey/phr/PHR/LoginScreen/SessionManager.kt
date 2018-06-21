package com.silverkey.phr.PHR.LoginScreen

import android.content.Context
import android.content.SharedPreferences
import android.content.Intent


/**
 * Created by Omar Salem 5/29/2018.
 */
class SessionManager {
    var pref: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    var _context: Context? = null
    var PRIVATE_MODE = 0
    // Sharedpref file name
    private val PREF_NAME = "PHR"
    // All Shared Preferences Keys
    private val IS_LOGIN = "IsLoggedIn"
    val KEY_USERNAME = "userName"
    val KEY_PASSWORD = "password"


    constructor(_context: Context?) {
        this._context = _context
        pref = _context?.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref?.edit()
    }

    fun createLoginSession(userName: String, password: String) {
        editor?.putBoolean(IS_LOGIN, true)
        editor?.putString(KEY_USERNAME, userName)
        editor?.putString(KEY_PASSWORD, password)
        editor?.commit()
    }

    fun checkLogin() {
        // Check login status
        if (!this.isLoggedIn()!!) {
            val startNewActivity = Intent(_context, LoginScreen::class.java)
            startNewActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startNewActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            _context?.startActivity(startNewActivity)
        }
    }

    fun getUserDetails(): HashMap<String, String>{
        val user = HashMap<String, String>()
        user[KEY_USERNAME] = pref?.getString(KEY_USERNAME,null)!!
        user[KEY_PASSWORD] = pref?.getString(KEY_PASSWORD,null)!!
        return user
    }


    fun isLoggedIn(): Boolean? {
        return pref?.getBoolean(IS_LOGIN, false)
    }
}
