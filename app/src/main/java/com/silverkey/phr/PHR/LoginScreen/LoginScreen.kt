package com.silverkey.phr.PHR.LoginScreen

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.inputmethod.EditorInfo
import cn.pedant.SweetAlert.SweetAlertDialog
import com.silverkey.phr.Helpers.Shared
import com.silverkey.phr.PHR.LoginScreen.LoginController.login
import com.silverkey.phr.R
import com.silverkey.phr.Helpers.TextUtils
import com.silverkey.phr.Helpers.startRegistrationScreen
import com.silverkey.phr.Helpers.startUploadScreen
import kotlinx.android.synthetic.main.activity_login_screen.*
import java.util.ArrayList
import java.util.HashMap
import android.content.SharedPreferences
import android.R.id.edit
import android.annotation.SuppressLint
import android.util.Log


class LoginScreen : AppCompatActivity(), LoginScreenListener {

    private var dialog: SweetAlertDialog? = null
    lateinit var userSission:SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userSission= SessionManager(this)
    //if( userSission.isLoggedIn()==false) {
        setContentView(R.layout.activity_login_screen)
        handleOptions()
   // }
   /* else{
        startUploadScreen(this@LoginScreen)
        finish()
    }*/
    }


    private fun handleOptions() {

        username_edit_text.setOnFocusChangeListener { _, stateOfFocus ->
            TextUtils.handleEditTextColor(username_edit_text, stateOfFocus, TextUtils.isUsernameValid(username_edit_text.text.toString().trim()), resources.getString(R.string.username_error), ContextCompat.getColor(this, R.color.colorPrimaryDark))
        }

        password_edit_text.setOnFocusChangeListener { _, stateOfFocus ->
            TextUtils.handleEditTextColor(password_edit_text, stateOfFocus, TextUtils.isPasswordValid(password_edit_text.text.toString().trim()), resources.getString(R.string.password_error), ContextCompat.getColor(this, R.color.colorPrimaryDark))
        }

        password_edit_text.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                enter_button.performClick()
                true
            } else {
                false
            }
        }
        userSission.createLoginSession(username_edit_text.text.toString(),password_edit_text.text.toString())

        enter_button.setOnClickListener {

            if (!validateAttributes()) return@setOnClickListener
            else {
                login(username_edit_text.text.toString(), password_edit_text.text.toString(), this@LoginScreen)
            }
        }

        register_text_view.setOnClickListener({
            startRegistrationScreen(this)
            finish()
        })
        }




    private fun validateAttributes(): Boolean {
        var valid = true

        if (!TextUtils.handleEditTextColor(username_edit_text, false, TextUtils.isUsernameValid(username_edit_text.text.toString().trim()), resources.getString(R.string.username_error), ContextCompat.getColor(this, R.color.colorPrimaryDark))) {
            valid = false
        }

        if (!TextUtils.handleEditTextColor(password_edit_text, false, TextUtils.isPasswordValid(password_edit_text.text.toString().trim()), resources.getString(R.string.password_error), ContextCompat.getColor(this, R.color.colorPrimaryDark))) {
            valid = false
        }

        return valid
    }

    override fun onLoading() {
        dialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialog?.progressHelper?.barColor = ContextCompat.getColor(this, R.color.colorPrimary)
        dialog?.titleText = getString(R.string.loading)
        dialog?.setCancelable(false)
        dialog?.show()
    }

    override fun onLoginSuccess() {

        dialog?.dismiss()
        startUploadScreen(this@LoginScreen)
        finish()
    }

    override fun onLoginFailed(massages: HashMap<String, ArrayList<String>>?, error: String?) {
        dialog?.dismiss()

        val errorMassage: String?

        if (massages != null) {
            var massage = ""
            for ((_, value) in massages) {
                for (str in value) {
                    massage += str
                }
            }
            massage = massage.replace("\\n", System.getProperty("line.separator"))
            errorMassage = massage

        } else if (error != null) {
            errorMassage = error

        } else {
            errorMassage = getString(R.string.internet_connection_error)
           // Shared.makeErrorToast(this,errorMassage)

        }

        dialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
        dialog?.progressHelper?.barColor = ContextCompat.getColor(this, R.color.colorPrimary)
        dialog?.titleText = getString(R.string.error)
        dialog?.contentText = errorMassage
        dialog?.setCancelable(false)
        dialog?.show()
    }

}
