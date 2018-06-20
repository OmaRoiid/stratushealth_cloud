package com.silverkey.phr.PHR.RegisterationScreen

import java.util.ArrayList
import java.util.HashMap

/**
 * Created by Mohamed Taher on 3/9/2018.
 */
interface RegisterationScreenListener {

    fun onLoading()

    fun onRegisterationInSucceed()

    fun onRegisterationFailed(massages: HashMap<String, ArrayList<String>>?, error: String?)

}