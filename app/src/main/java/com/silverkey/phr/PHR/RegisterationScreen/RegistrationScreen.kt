package com.silverkey.phr.PHR.RegisterationScreen

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.inputmethod.EditorInfo
import cn.pedant.SweetAlert.SweetAlertDialog
import com.silverkey.phr.Helpers.Shared
import com.silverkey.phr.Helpers.TextUtils
import com.silverkey.phr.Helpers.startLoginScreen
import com.silverkey.phr.R
import kotlinx.android.synthetic.main.activity_registeration_screen.*
import java.util.ArrayList
import java.util.HashMap

class RegistrationScreen : AppCompatActivity(), RegisterationScreenListener {

    private var dialog: SweetAlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registeration_screen)

        handleOptions()
    }

    private fun handleOptions() {

        username_edit_text.setOnFocusChangeListener { _ , b ->
            TextUtils.handleEditTextColor(username_edit_text, b, TextUtils.isUsernameValid(username_edit_text.text.toString().trim()), resources.getString(R.string.username_error), ContextCompat.getColor(this, R.color.colorPrimaryDark))
        }

        email_edit_text.setOnFocusChangeListener { _ , b ->
            TextUtils.handleEditTextColor(email_edit_text, b, TextUtils.isEmailValid(email_edit_text.text.toString().trim()), resources.getString(R.string.email_error), ContextCompat.getColor(this, R.color.colorPrimaryDark))
        }

        password_edit_text.setOnFocusChangeListener { _ , b ->
            TextUtils.handleEditTextColor(password_edit_text, b, TextUtils.isPasswordValid(password_edit_text.text.toString().trim()), resources.getString(R.string.password_error), ContextCompat.getColor(this, R.color.colorPrimaryDark))
        }

        password_edit_text.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                enter_button.performClick()
                true
            } else {
                false
            }
        }

        enter_button.setOnClickListener({
            if(!validateAttributes()) return@setOnClickListener
            createNewAccount()
        })

        login_text_view.setOnClickListener({
            startLoginScreen(this)
        })
    }

    private fun validateAttributes(): Boolean {
        var valid = true

        if (!TextUtils.handleEditTextColor(username_edit_text, false, TextUtils.isUsernameValid(username_edit_text.text.toString().trim()), resources.getString(R.string.username_error), ContextCompat.getColor(this, R.color.colorPrimaryDark))) {
            valid = false
        }

        if (!TextUtils.handleEditTextColor(email_edit_text, false, TextUtils.isEmailValid(email_edit_text.text.toString().trim()), resources.getString(R.string.username_error), ContextCompat.getColor(this, R.color.colorPrimaryDark))) {
            valid = false
        }

        if (!TextUtils.handleEditTextColor(password_edit_text, false, TextUtils.isPasswordValid(password_edit_text.text.toString().trim()), resources.getString(R.string.username_error), ContextCompat.getColor(this, R.color.colorPrimaryDark))) {
            valid = false
        }

        return valid
    }

    private fun createNewAccount() {
        val username = username_edit_text.text.toString()
        val password = password_edit_text.text.toString()
        val email = email_edit_text.text.toString()

        RegistrationController.registration(email, password, username, this)
    }

    override fun onLoading() {
        dialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialog?.progressHelper?.barColor = ContextCompat.getColor(this, R.color.colorPrimary)
        dialog?.titleText = getString(R.string.loading)
        dialog?.setCancelable(false)
        dialog?.show()
    }

    override fun onRegisterationInSucceed() {
        dialog?.dismiss()
        startLoginScreen(this)
        finish()
    }

    override fun onRegisterationFailed(massages: HashMap<String, ArrayList<String>>?, error: String?) {
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
        }

        dialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
        dialog?.progressHelper?.barColor = ContextCompat.getColor(this, R.color.colorPrimary)
        dialog?.titleText = getString(R.string.error)
        dialog?.contentText = errorMassage
        dialog?.setCancelable(false)
        dialog?.show()
    }

}
