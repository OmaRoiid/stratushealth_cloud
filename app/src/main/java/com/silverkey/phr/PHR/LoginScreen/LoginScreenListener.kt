package com.silverkey.phr.PHR.LoginScreen

import java.util.ArrayList
import java.util.HashMap

/**
 * Created by Mohamed Taher on 2/6/2018.
 */
interface LoginScreenListener {

    fun onLoading()

    fun onLoginSuccess()

    fun onLoginFailed(massages: HashMap<String, ArrayList<String>>?, error: String?)
}