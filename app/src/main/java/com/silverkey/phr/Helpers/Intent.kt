package com.silverkey.phr.Helpers

import android.content.Context
import android.content.Intent
import com.silverkey.phr.PHR.LoginScreen.LoginScreen
import com.silverkey.phr.PHR.RegisterationScreen.RegistrationScreen
import com.silverkey.phr.PHR.UploadedFilesScreen.UploadedFilesScreen

/**
 * Created by Mohamed Taher on 12/28/2017.
 */

val FILE_NAME = "FILE_NAME"

fun startRegistrationScreen(context: Context) {
    val intent = Intent(context, RegistrationScreen::class.java)
    context.startActivity(intent)
}

fun startLoginScreen(context: Context) {
    val intent = Intent(context, LoginScreen::class.java)
    context.startActivity(intent)
}

fun startUploadScreen(context: Context) {
    val intent = Intent(context, UploadedFilesScreen::class.java)
    context.startActivity(intent)
}

