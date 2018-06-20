package com.silverkey.scops.API

import com.silverkey.phr.Model.LoginResponse
import com.silverkey.phr.Model.ShareableLinkForm
import com.silverkey.phr.Model.User
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by Mohamed Taher on 12/12/2017.
 */
interface StratusService {

    @POST(Config.REGISTERATION)
    fun registration(@Body user: User): Call<Reply.Response<User, Any>>

    @POST(Config.LOGIN)
    fun login(@Body user: User): Call<Reply.Response<User, LoginResponse>>

    @POST(Config.GET_SHAREABLE_LINK)
    fun getShareanleLink(@Body shareableLinkForm: ShareableLinkForm): Call<Reply.Response<User, String>>

}