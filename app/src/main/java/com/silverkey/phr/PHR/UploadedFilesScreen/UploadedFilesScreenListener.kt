package com.silverkey.phr.PHR.UploadedFilesScreen

import android.content.Context
import com.silverkey.phr.Model.All
import com.silverkey.phr.Model.UserFile

/**
 * Created by Mohamed Taher on 2/6/2018.
 * Updated by Omar Salem on 5/27/2018.
 */
interface UploadedFilesScreenListener {

    fun onLoading()

    fun onUploadingFile()

    fun onGotUploadedFilesSuccess(files: ArrayList<All>)

    fun onGotUploadedFilesFailed()

    fun onFileUploadedSuccess()

    fun onFileUploadedFailed()

    fun onDownloadingFile()

    fun onFileDownloadedSuccess()

    fun onFileDownloadedFailed()

    fun onDeletingFile()

    fun onDeletingSuccess()

    fun onDeletingFailed()

    fun onLoadingSharableLink()

    fun onGetShareableLink(link: String?)

    fun onFailedToGetShareableLink(errorMsg: String)
}