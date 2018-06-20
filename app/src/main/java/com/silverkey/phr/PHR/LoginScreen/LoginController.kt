package com.silverkey.phr.PHR.LoginScreen

import android.annotation.SuppressLint
import android.os.AsyncTask
import com.silverkey.phr.Azure.AzureSettings
import com.silverkey.phr.Azure.AzureUtility
import com.silverkey.phr.Helpers.Shared
import android.os.Handler
import com.silverkey.phr.Model.User
import com.silverkey.scops.API.StratusAPI
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import ru.gildor.coroutines.retrofit.Result
import ru.gildor.coroutines.retrofit.awaitResult
import java.util.*

/**
 * Created by Mohamed Taher on 2/6/2018.
 */
object LoginController {

    fun login(username: String, password: String, listener: LoginScreenListener) = launch(UI) {
        listener.onLoading()
        val service = StratusAPI().getInspectionService()
        val result = service.login(User(username, password, null)).awaitResult()
        when (result) {
            is Result.Ok -> {
                val item = result.value
                val statusCode = item.statusCode
                when(statusCode) {
                    400 -> {
                        listener.onLoginFailed(item.errors, null)
                    }

                    401 -> {
                        listener.onLoginFailed(null, item.reason)
                    }

                    500 -> {
                        listener.onLoginFailed(null, item.reason)
                    }

                    200 -> {

                        Shared.sasToken = item.responsePayload?.sasToken
                        Shared.sessionToken = item.responsePayload?.sessionToken
                        listener.onLoginSuccess()
                    }

                    else -> {
                        listener.onLoginFailed(null, null)
                    }
                }
            }

            is Result.Error -> {
                listener.onLoginFailed(null, null)
            }

            is Result.Exception -> {
                listener.onLoginFailed(null, null)
            }
        }
    }
}