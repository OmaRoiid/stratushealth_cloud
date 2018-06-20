package com.silverkey.phr.Helpers

import android.content.Context
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Toast
import es.dmoral.toasty.Toasty

/**
 * Created by Mohamed Taher on 2/6/2018.
 */
object Shared {

    var userSessionId: String? = null
    var sharedFileName:String?=null
    var username: String? = null
    var mToast: Toast? = null
    val timeout: Long = 30000
    var sasToken: String? = null
    var sessionToken: String? = null

    fun makeSnackbar(view: View, actionName: Int?, msg: Int, listener: View.OnClickListener?): Snackbar {
        val mySnackbar = Snackbar.make( view, msg, Snackbar.LENGTH_INDEFINITE)
        if (actionName != null) {
            mySnackbar.setAction(actionName, listener)
        }
        mySnackbar.show()
        return mySnackbar
    }

    fun makeToast(context: Context, name: Int) {
        val msg = context.resources?.getString(name)
        mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        mToast!!.show()
        mToast = null
    }

    fun makeToast(context: Context, name: String) {
        mToast = Toast.makeText(context, name, Toast.LENGTH_SHORT)
        mToast!!.show()
        mToast = null
    }
    fun makeSuccessToast(context: Context,Msg:String)
    {
        Toasty.success(context,Msg,Toast.LENGTH_SHORT, true).show()

    }
    fun makeErrorToast(context: Context,Msg:String)
    {
        Toasty.error(context,Msg,Toast.LENGTH_SHORT, true).show()
    }
    fun makeInfoToast(context: Context,Msg:String)
    {
        Toasty.info(context,Msg,Toast.LENGTH_SHORT, true).show()
    }

    fun getLastBitFromUrl(url: String): String {
        return url.replaceFirst(".*/([^/?]+).*".toRegex(), "$1")
    }
}