package com.silverkey.phr.PHR.RegisterationScreen

import android.widget.Switch
import com.silverkey.phr.Model.User
import com.silverkey.scops.API.StratusAPI
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import ru.gildor.coroutines.retrofit.Result
import ru.gildor.coroutines.retrofit.awaitResult

/**
 * Created by Mohamed Taher on 3/9/2018.
 */
object RegistrationController {

    fun registration(email: String, password: String, username: String, listener: RegisterationScreenListener) = launch(UI) {

        listener.onLoading()
        val service = StratusAPI().getInspectionService()
        val result = service.registration(User(email, password, username)).awaitResult()
        when (result) {
            is Result.Ok -> {
                val item = result.value
                val statusCode = item.statusCode
                when(statusCode) {
                    400 -> {
                        listener.onRegisterationFailed(item.errors, null)
                    }

                    500 -> {
                        listener.onRegisterationFailed(null, item.reason)
                    }

                    200 -> {
                        listener.onRegisterationInSucceed()
                    }

                    else -> {
                        listener.onRegisterationFailed(null, null)
                    }
                }
            }

            is Result.Error -> {
                listener.onRegisterationFailed(null, null)
            }

            is Result.Exception -> {
                listener.onRegisterationFailed(null, null)
            }
        }
    }

}