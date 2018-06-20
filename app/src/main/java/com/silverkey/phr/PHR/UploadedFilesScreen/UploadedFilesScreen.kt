package com.silverkey.phr.PHR.UploadedFilesScreen

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import cn.pedant.SweetAlert.SweetAlertDialog
import com.codekidlabs.storagechooser.Content
import com.silverkey.phr.PHR.UploadedFilesScreen.UploadedFilesScreenController.getUploadedFiles
import com.silverkey.phr.PHR.UploadedFilesScreen.UploadedFilesScreenController.uploadedFiles
import com.silverkey.phr.R
import com.silverkey.phr.Helpers.Shared
import kotlinx.android.synthetic.main.activity_upload_screen.*
import com.codekidlabs.storagechooser.StorageChooser
import com.karan.churi.PermissionManager.PermissionManager
import com.silverkey.phr.PHR.UploadedFilesScreen.UploadedFilesScreenController.uploadFile
import android.widget.LinearLayout
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import java.io.File
import android.support.design.widget.BottomSheetDialog
import android.support.v4.app.ShareCompat
import android.support.v7.widget.RecyclerView
import com.silverkey.phr.Model.*
import com.silverkey.phr.PHR.UploadedFilesScreen.UploadedFilesScreenController.currentFolder
import kotlinx.android.synthetic.main.layout_bottom_sheet.view.*
import kotlinx.android.synthetic.main.layout_shareable_link.view.*
import java.io.ByteArrayOutputStream
import kotlin.collections.ArrayList


/**
 * Created by Mohamed Taher on 2/6/2018.
 * Updated by Omar Salem 5/27/2018.
 */

class UploadedFilesScreen : AppCompatActivity(), UploadedFilesScreenListener {
    private var dialog: SweetAlertDialog? = null
    private var permission: PermissionManager? = null
    private val CAPTURE_MEDIA = 368


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_screen)
        var isFirstScreen: Boolean = intent.getBooleanExtra("Flag", false)
        handleOptions()
        fetchData()
        if (!isFirstScreen) {
            levelOfScreen(UploadedFilesScreenController.rootFolder, 0, UploadedFilesScreenController.listOfUserUploads)
            UploadedFilesScreenController.currentFolder = UploadedFilesScreenController.rootFolder
        } else {
            tv_tittle.text = currentFolder?.Foldername
            setSupportActionBar(main_toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            main_toolbar.setNavigationOnClickListener { _ -> onBackPressed() }
        }
        uploaded_files_list.adapter = FolderAdapter(this, UploadedFilesScreenController.currentFolder, this)
        uploaded_files_list.layoutManager = LinearLayoutManager(this)
        hideBtnOnScrolling()
    }


    private fun hideBtnOnScrolling() {
        uploaded_files_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && upload_btn.visibility === View.VISIBLE) {
                    upload_btn.hide()
                } else if (dy < 0 && upload_btn.visibility !== View.VISIBLE) {
                    upload_btn.show()
                }
            }
        })
    }

    private fun handleOptions() {
        uploaded_files_list_swipe.setOnRefreshListener {
            uploadedFiles.clear()
            fetchData()
        }

        upload_btn.setOnClickListener({
            checkPermission()
            showBottomSheet()
        })
    }

    private fun fetchData() {
        getUploadedFiles(Shared.username ?: "", this)
    }

    private fun chooseAndUploadFile() {

        checkPermission()

        val c = Content()
        c.cancelLabel = "Cancel"
        c.selectLabel = "Select"

        // Initialize Builder
        val chooser = StorageChooser.Builder()
                .withActivity(this@UploadedFilesScreen)
                .withFragmentManager(fragmentManager)
                .withMemoryBar(true)
                .allowCustomPath(true)
                .setType(StorageChooser.DIRECTORY_CHOOSER)
                .setType(StorageChooser.FILE_PICKER)
                .withContent(c)
                .build()

        // Show dialog whenever you want by
        chooser.show()

        // get path that the user has chosen
        chooser.setOnSelectListener { path ->
            Log.e("SELECTED_PATH", path)
            val name = Shared.getLastBitFromUrl(path)
            uploadFile(name, path, this)
        }

        chooser.setOnCancelListener {
            Shared.makeInfoToast(this, "No file Selected")
        }

    }

    private fun checkPermission() {

        permission = object : PermissionManager() {}

        //To initiate checking permission
        permission?.checkAndRequestPermissions(this)
    }

    //Loading Main Screen
    override fun onLoading() {
        uploaded_files_list_swipe.isRefreshing = true
    }

    override fun onGotUploadedFilesSuccess(files: ArrayList<All>) {
        uploaded_files_list_swipe.isRefreshing = false
        uploadedFiles.addAll(files)
        uploaded_files_list.adapter.notifyDataSetChanged()
    }

    override fun onGotUploadedFilesFailed() {
        uploaded_files_list_swipe.isRefreshing = false

        dialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
        dialog?.setTitle(getString(R.string.internet_connection_error))
        dialog?.confirmText = getString(R.string.retry)
        dialog?.setConfirmClickListener {
            fetchData()
        }
        dialog?.cancelText = getString(R.string.cancel)
        dialog?.setCancelClickListener {
            dialog?.hide()
        }
    }

    //Uploading File/Folder feature
    override fun onUploadingFile() {
        dialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialog?.progressHelper?.barColor = ContextCompat.getColor(this, R.color.colorPrimary)
        dialog?.titleText = getString(R.string.uploading)
        dialog?.setCancelable(false)
        dialog?.show()
    }

    override fun onFileUploadedSuccess() {
        dialog?.dismiss()
        Shared.makeSuccessToast(this, "Done")
        fetchData()
    }

    override fun onFileUploadedFailed() {
        dialog?.dismiss()
        Shared.makeErrorToast(this, "Internet connection error")
    }

    //Download File Feature
    override fun onDownloadingFile() {
        dialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialog?.progressHelper?.barColor = ContextCompat.getColor(this, R.color.colorPrimary)
        dialog?.titleText = getString(R.string.downloading)
        dialog?.setCancelable(false)
        dialog?.show()
    }

    override fun onFileDownloadedSuccess() {
        dialog?.dismiss()
        Shared.makeSuccessToast(this, "Done")
    }

    override fun onFileDownloadedFailed() {
        dialog?.dismiss()
        Shared.makeToast(this, R.string.internet_connection_error)
    }

    //Deleting File Feature
    override fun onDeletingFile() {
        dialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialog?.progressHelper?.barColor = ContextCompat.getColor(this, R.color.colorPrimary)
        dialog?.titleText = "Deleting.."
        dialog?.setCancelable(false)
        dialog?.show()
    }

    override fun onDeletingSuccess() {
        dialog?.dismiss()
        Shared.makeSuccessToast(this, "Done")
        fetchData()
    }

    override fun onDeletingFailed() {
        dialog?.dismiss()
        Shared.makeToast(this, R.string.internet_connection_error)
    }

    //Get shareable link Feature
    override fun onLoadingSharableLink() {
        dialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialog?.progressHelper?.barColor = ContextCompat.getColor(this, R.color.colorPrimary)
        dialog?.titleText = "Loading.."
        dialog?.setCancelable(false)
        dialog?.show()
    }

    override fun onGetShareableLink(link: String?) {
        dialog?.dismiss()
        val shareView: View = let { View.inflate(this, R.layout.layout_shareable_link, null) }
        val alert: AlertDialog.Builder? = AlertDialog.Builder(this)
        shareView.tv_link.text = link
        alert?.setView(shareView)
        alert?.show()
        ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(link)
                .startChooser()

    }

    override fun onFailedToGetShareableLink(errorMsg: String) {
        dialog?.dismiss()
        val shareView: View = let { View.inflate(this, R.layout.layout_shareable_link, null) }
        val alert: AlertDialog.Builder? = AlertDialog.Builder(this)
        shareView.tv_link.text = errorMsg
        alert?.setView(shareView)
        alert?.show()
    }

    private fun showBottomSheet() {
        var sheetView: View = let { this.layoutInflater.inflate(R.layout.layout_bottom_sheet, null) }
        var mBottomSheetDialog = BottomSheetDialog(this@UploadedFilesScreen)
        mBottomSheetDialog.setContentView(sheetView)
        mBottomSheetDialog.show()
        var uploadFileToApp: LinearLayout = sheetView.layout_upload_file
        val takePhoto: LinearLayout = sheetView.layout_take_photo
        val createFolder: LinearLayout = sheetView.layout_create_folder
        val uploadFolder: LinearLayout = sheetView.layout_upload_folder
        uploadFileToApp.setOnClickListener {
            chooseAndUploadFile()
        }
        takePhoto.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAPTURE_MEDIA)
        }
        createFolder.setOnClickListener {
            UploadedFilesScreenController.createFolderOnApp(applicationContext, R.layout.layout_create_folder, this)
            uploaded_files_list.adapter.notifyDataSetChanged()

        }
        uploadFolder.setOnClickListener {

            UploadedFilesScreenController.uploadFolderToApp(this@UploadedFilesScreen, this)
        }
    }

    //this 3 Funs for  take photo via camera
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAPTURE_MEDIA && resultCode == RESULT_OK) {
            var photo: Bitmap = data?.extras?.get("data") as Bitmap
            val tempUri: Uri = getImageUri(this, photo)
            val savedFile = File(getRealPathFromURI(tempUri))
            Shared.makeToast(this, "$savedFile")
            uploadFile(savedFile.name, savedFile.path, this)
        }

    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun getRealPathFromURI(uri: Uri): String {
        val cursor = getContentResolver().query(uri, null, null, null, null)
        cursor.moveToFirst()
        val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        return cursor.getString(idx)
    }

    private fun levelOfScreen(folder: Folder?, column: Int, listOfUserUploads: ArrayList<ArrayList<String>>) {
        //sort //TODO Solve sort function
        for (level in listOfUserUploads) {
            for (levelTwo in listOfUserUploads) {
                if (level.size <= column) continue
                if (levelTwo.size <= column) continue
                if (level[column] > levelTwo[column]) {
                    level to levelTwo
                }
            }
        }

        val uniqeNames = ArrayList<String>()
        for (paths in 0 until listOfUserUploads.size) {
            if (listOfUserUploads[paths].size <= column) continue
            if (listOfUserUploads[paths] == listOfUserUploads[listOfUserUploads.size - 1]) {
                uniqeNames.add(listOfUserUploads[paths][column])
            } else if (listOfUserUploads[paths][column] != listOfUserUploads[paths + 1][column]) {
                uniqeNames.add(listOfUserUploads[paths][column])
            }
        }

        for (name in uniqeNames) {

            if (name.contains(".")) {
                folder?.files?.add(FileItem(name))
            } else {
                val newFolder = Folder(name)
                folder?.folders?.add(newFolder)

                val folderPath = ArrayList<ArrayList<String>>()
                for (path in listOfUserUploads) {
                    if (path[column] == name) {
                        folderPath.add(path)
                    }
                }

                levelOfScreen(newFolder, column + 1, folderPath)
            }
        }
    }

}