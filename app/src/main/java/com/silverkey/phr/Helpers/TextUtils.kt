package com.silverkey.phr.Helpers

import android.widget.EditText
import java.util.regex.Pattern

/**
 * Created by Mohamed Taher on 2/6/2018.
 */
object TextUtils {

    public val textColorHint = 0xffffff.toInt()
    //public val textColor = 0xffffff.toInt()
    public val textColorErrorHint = 0xffFF9494.toInt()
    public val textColorError = 0xffFF3838.toInt()

    fun handleEditTextColor(et: EditText?, focus: Boolean, validData: Boolean, errorMessage: String, textColor: Int): Boolean {
        if (focus) {
            ColorEditText_Text(et, true, textColor)

        } else if (validData) {
            ColorEditText_Text(et, true, textColor)
            et?.error = null
        } else {
            ColorEditText_Text(et, false, textColor)

            et?.error = errorMessage

            return false
        }

        return true
    }

    fun isEmailValid(email: String): Boolean {
        val regExpn = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"

        val pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)

        return matcher.matches()
    }

    fun isUsernameValid(username: String): Boolean {

        //TODO Check regEXPN
//        val regExpn = "\\\"^[a-z0-9](?!.*--)[a-z0-9-]{1,61}[a-z0-9]$\\\""
//
//        val pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE)
//        val matcher = pattern.matcher(username)
//
//        return matcher.matches()

        return true
    }

    fun isFullNameValid(fullName: String): Boolean {
        return fullName.isNotEmpty()
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length >= 3
    }

    fun isRePasswordValid(password: String, passwordConfirm: String): Boolean {
        return password.equals(passwordConfirm) && passwordConfirm.length > 0
    }

    fun isMobileNumberValid(mobileNumber: String): Boolean {
        return mobileNumber.length == 11
    }

    fun isDateValid(date: String): Boolean {
        return date.length > 0
    }

    fun isAddressValid(address: String): Boolean {
        return address.length > 0
    }

    fun ColorEditText_Text(et: EditText?, valid: Boolean, textColor: Int) {
        if (valid) {
            et?.setTextColor(textColor)
            et?.setHintTextColor(textColorHint)
        } else {
            et?.setTextColor(textColorError)
            et?.setHintTextColor(textColorErrorHint)
        }
    }

    fun validateText(text: String?): Boolean {
        if (text != null && text.trim { it <= ' ' } != "") {
            return true
        }

        return false
    }

    fun validateTextWithMinimumNumber(text: String, minimumNumber: Int): Boolean {
        if (validateText(text)) {
            return text.length >= minimumNumber
        }

        return false
    }

}