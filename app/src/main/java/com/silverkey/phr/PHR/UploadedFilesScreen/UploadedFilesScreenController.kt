package com.silverkey.phr.PHR.UploadedFilesScreen

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.Environment
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Button
import com.obsez.android.lib.filechooser.ChooserDialog
import com.silverkey.phr.Azure.AzureUtility
import com.silverkey.phr.Azure.AzureUtility.createFolder
import com.silverkey.phr.Azure.AzureUtility.getUserFiles
import com.silverkey.phr.Helpers.Shared
import com.silverkey.phr.Model.*
import com.silverkey.scops.API.StratusAPI
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import ru.gildor.coroutines.retrofit.Result
import ru.gildor.coroutines.retrofit.awaitResult
import java.util.*
import com.silverkey.phr.R
import kotlinx.android.synthetic.main.layout_create_folder.view.*
import java.io.File
import kotlin.collections.ArrayList


/**
 * Created by Mohamed Taher on 2/6/2018.
 * Updated by Omar Salem on 5/27/2018
 */
object UploadedFilesScreenController {

    val uploadedFiles = ArrayList<All>()
    val uploadFolder = ArrayList<All>()
    var getFilesTask: AsyncTask<Void, Void, ArrayList<All>>? = null
    var uploadFileTask: AsyncTask<Void, Void, Boolean?>? = null
    var deleteFileTask: AsyncTask<Void, Void, Boolean?>? = null
    var createFolderTask: AsyncTask<Void, Void, Boolean?>? = null
    var uiHandler: Handler? = null
    var rootFolder: Folder? = null
    var currentFolder: Folder? = null
    val str1: String = "FolderA/FolderAA/FileAAA.txt"
    val str2: String = "FolderA/FolderAB/FileABA.txt"
    val str3: String = "FolderA/FileAC.txt"
    val str4: String = "FolderB/FileBA.txt"
    val str5: String = "FolderC/FileCA.txt"
    val str6: String = "FileD.txt"
    val allPathes = java.util.ArrayList<String>()
    val listOfUserUploads = java.util.ArrayList<java.util.ArrayList<String>>()

    init {
        rootFolder = Folder()

        /* val folders2 = arrayListOf(
                Folder("phr11", arrayListOf(), arrayListOf())
        )

        val files2 = arrayListOf(
                FileItem("file11")
        )


        val folders = arrayListOf(
                  Folder("phr1", folders2, files2)
                , Folder("phr2", arrayListOf(), arrayListOf())
                , Folder("phr3", arrayListOf(), arrayListOf())
        )

        val files = arrayListOf(
                 FileItem("file1")
                , FileItem("file1")
        )
*/
/*        rootFolder?.folders = folders
        rootFolder?.files = files*/


//TODO split the api list4
        var list1 = ArrayList(str1.split("/"))
        var list2 = ArrayList(str2.split("/"))
        var list3 = ArrayList(str3.split("/"))
        var list4 = ArrayList(str5.split("/"))
        var list5 = ArrayList(str4.split("/"))
        var list6 = ArrayList(str6.split("/"))
        listOfUserUploads += list1
        listOfUserUploads += list2
        listOfUserUploads += list3
        listOfUserUploads += list4
        listOfUserUploads += list5
        listOfUserUploads += list6

    }


    //App features Apis
    fun getUploadedFiles(username: String, listener: UploadedFilesScreenListener) {

        listener.onLoading()

        uploadedFiles.clear()
        var isFinished = false
        uiHandler = Handler()

        getFilesTask = @SuppressLint("StaticFieldLeak")
        object : AsyncTask<Void, Void, ArrayList<All>>() {

            override fun onPreExecute() {
                val timer = Timer()
                timer.schedule(
                        object : TimerTask() {
                            override fun run() {
                                if (!isFinished) {
                                    uiHandler?.post {
                                        listener.onGotUploadedFilesFailed()
                                    }
                                    getFilesTask?.cancel(false)
                                }
                                timer.cancel()
                            }
                        }
                        , Shared.timeout
                )
            }

            override fun doInBackground(vararg p0: Void?): ArrayList<All>? {
                return getUserFiles()
            }

            override fun onPostExecute(result: ArrayList<All>?) {
                if (result != null)
                    listener.onGotUploadedFilesSuccess(result)
                else
                    listener.onGotUploadedFilesFailed()

                isFinished = true
            }
        }

        getFilesTask?.execute()
    }

    fun uploadFile(filesName: String, path: String, listener: UploadedFilesScreenListener) {

        listener.onUploadingFile()
        var isFinished = false
        uiHandler = Handler()

        val uploadFileTask = @SuppressLint("StaticFieldLeak")
        object : AsyncTask<Void, Void, Boolean?>() {

            override fun onPreExecute() {
                val timer = Timer()
                timer.schedule(
                        object : TimerTask() {
                            override fun run() {
                                if (!isFinished) {
                                    uiHandler?.post {
                                        listener.onFileUploadedFailed()
                                    }
                                    uploadFileTask?.cancel(false)
                                }
                                timer.cancel()
                            }
                        }
                        , Shared.timeout * 30
                )
            }

            override fun doInBackground(vararg p0: Void?): Boolean? {
                return AzureUtility.createFile(Shared.username ?: "", filesName, path)
            }

            override fun onPostExecute(result: Boolean?) {
                if (result ?: false)
                    listener.onFileUploadedSuccess()
                else
                    listener.onFileUploadedFailed()

                isFinished = true
            }
        }

        uploadFileTask.execute()
    }

    fun downloadFile(name: String, listener: UploadedFilesScreenListener) {
        listener.onDownloadingFile()
        var isFinished = false
        uiHandler = Handler()

        val uploadFileTask = @SuppressLint("StaticFieldLeak")
        object : AsyncTask<Void, Void, Boolean?>() {

            override fun onPreExecute() {
                val timer = Timer()
                timer.schedule(
                        object : TimerTask() {
                            override fun run() {
                                if (!isFinished) {
                                    uiHandler?.post {
                                        listener.onFileDownloadedFailed()
                                    }
                                    uploadFileTask?.cancel(false)
                                }
                                timer.cancel()
                            }
                        }
                        , Shared.timeout * 30
                )

            }

            override fun doInBackground(vararg p0: Void?): Boolean? {
                return AzureUtility.downloadFile(name)
            }

            override fun onPostExecute(result: Boolean?) {
                if (result ?: false)

                    listener.onFileDownloadedSuccess()
                else
                    listener.onFileDownloadedFailed()

                isFinished = true
            }
        }

        uploadFileTask.execute()
    }

    fun deleteFile(fileName: String, listener: UploadedFilesScreenListener) {

        listener.onDeletingFile()
        var isFinished = false
        uiHandler = Handler()
        val deleteFileTask = @SuppressLint("StaticFieldLeak")
        object : AsyncTask<Void, Void, Boolean?>() {

            override fun onPreExecute() {

                val timer = Timer()
                timer.schedule(
                        object : TimerTask() {
                            override fun run() {
                                if (!isFinished) {
                                    uiHandler?.post {
                                        listener.onDeletingFailed()
                                    }
                                    deleteFileTask?.cancel(false)
                                }
                                timer.cancel()
                            }
                        }
                        , Shared.timeout * 30
                )
            }

            override fun doInBackground(vararg p0: Void?): Boolean? {
                return AzureUtility.deleteFile(fileName)
            }

            override fun onPostExecute(result: Boolean?) {
                if (result ?: false)
                    listener.onDeletingSuccess()
                else
                    listener.onDeletingFailed()

                isFinished = true
            }
        }

        deleteFileTask.execute()
    }

    fun getShareableLink(name: String, hours: Int, listener: UploadedFilesScreenListener) = launch(UI) {
        listener.onLoadingSharableLink()
        val service = StratusAPI().getInspectionService()
        val result = service.getShareanleLink(ShareableLinkForm(Shared.sessionToken, name, hours)).awaitResult()
        when (result) {
            is Result.Ok -> {
                val item = result.value
                val statusCode = item.statusCode
                when (statusCode) {
                    400 -> {
                        listener.onFailedToGetShareableLink("Error To get Link..!!")
                    }

                    401 -> {

                        listener.onFailedToGetShareableLink("Error To get Link..!!")
                    }

                    500 -> {
                        listener.onFailedToGetShareableLink("Error To get Link..!!")
                    }

                    200 -> {
                        listener.onGetShareableLink(item.responsePayload)
                    }

                    else -> {
                        listener.onFailedToGetShareableLink("Error To get Link..!!")
                    }
                }
            }

            is Result.Error -> {
                listener.onFailedToGetShareableLink("Error To get Link..!!")
            }

            is Result.Exception -> {
                listener.onFailedToGetShareableLink("Error To get Link..!!")
            }

        }

    }

    fun showDotsBottomSheet(viewResource: Int, context: Context, fileName: String, listener: UploadedFilesScreenListener) {
        val bottomSheetDots = BottomSheet(viewResource, context)
        bottomSheetDots.show()
        bottomSheetDots.downloadFileOrFolder(fileName, listener)
        bottomSheetDots.shareFileOrFolder(fileName, listener)
        bottomSheetDots.removeFile(fileName, listener)
    }

    fun uploadFolderToApp(context: Context, listener: UploadedFilesScreenListener) {
        ChooserDialog().with(context)
                .withFilter(true, false)
                .withStartFile(Environment.getExternalStorageDirectory().path)
                .withChosenListener { path, pathFile ->
                    for (file in pathFile.listFiles()) {
                        uploadFile(file.name, file.path, listener)
                    }
                    listener.onFileUploadedSuccess()
                }
                .build()
                .show()

    }

    fun createFolderOnApp(mContext: Context, Resource: Int, listener: UploadedFilesScreenListener) {
        val customSheetView: View = let { View.inflate(mContext, Resource, null) }
        val alert: AlertDialog = AlertDialog.Builder(mContext).create()
        val folderName = customSheetView.et_file_name
        customSheetView.bt_yes.setOnClickListener {
            val fileNameText = folderName?.text.toString()
            val file = File(Environment.getExternalStorageDirectory(), fileNameText)
            if (file.mkdir()) {
                Shared.makeSuccessToast(mContext, "Created")
                createFolder(fileNameText)
                //uploadFolder.add(All(UserFolder(fileNameText),UserFile(" ")))
                alert.dismiss()
            }
        }

        customSheetView.findViewById<Button>(R.id.bt_no).setOnClickListener {
            Shared.makeErrorToast(mContext, "Canceled")
            alert.setCancelable(true)
            alert.dismiss()
        }
        alert.setView(customSheetView)
        alert.show()

    }

}


