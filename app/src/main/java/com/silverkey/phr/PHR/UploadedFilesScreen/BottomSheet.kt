package com.silverkey.phr.PHR.UploadedFilesScreen
import android.content.Context
import android.os.Environment
import android.support.design.widget.BottomSheetDialog
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.silverkey.phr.Helpers.Shared
import com.silverkey.phr.R
import kotlinx.android.synthetic.main.layout_dots_bottom_sheet.view.*
import java.io.File


/**
 * Created by omar salem 16/5/2018
 * Updated  on 5/27/2018
 */
class BottomSheet(viewResource: Int, private val mContext: Context) {


    private val bottomSheetView: View = let { View.inflate(mContext, viewResource, null) }

    fun show() {
        val mBottomSheetDialog = BottomSheetDialog(mContext)
        mBottomSheetDialog.setContentView(bottomSheetView)
        mBottomSheetDialog.show()
    }

    fun downloadFileOrFolder(fileName: String, listener: UploadedFilesScreenListener) {
        val downloadItem: LinearLayout = bottomSheetView.layout_download
        downloadItem.setOnClickListener {
            val view: View = View.inflate(mContext, R.layout.layout_create_folder, null)
            val alert: AlertDialog = AlertDialog.Builder(mContext).create()
            val folderName = view.findViewById<EditText>(R.id.et_file_name)
            view.findViewById<Button>(R.id.bt_yes).setOnClickListener {
                val fileNameText = folderName?.text.toString()
                //Make file on mobile by name
                val file = File(Environment.getExternalStorageDirectory(), fileNameText).mkdir()
                if (file) {
                    Shared.makeToast(mContext, "your file name is  $fileNameText")
                    Shared.sharedFileName = fileNameText
                    UploadedFilesScreenController.downloadFile(fileName, listener)
                    alert.dismiss()
                } else {
                    Shared.makeErrorToast(mContext, " $fileNameText  is not created")
                }
            }
            view.findViewById<Button>(R.id.bt_no).setOnClickListener {
                Shared.makeErrorToast(mContext, "DownLoading Canceled")
                alert.dismiss()
            }
            alert.setView(view)
            alert.show()
        }
    }

    fun shareFileOrFolder(fileName: String, listener: UploadedFilesScreenListener) {
        val shareLayout: LinearLayout = bottomSheetView.layout_share
        shareLayout.setOnClickListener {
            UploadedFilesScreenController.getShareableLink(fileName, 24, listener)
        }

    }

    fun removeFile(fileName: String, listener: UploadedFilesScreenListener) {
        val removeFileLayout = bottomSheetView.layout_remove
        removeFileLayout.setOnClickListener {
            UploadedFilesScreenController.deleteFile(fileName, listener)
        }
    }

    fun moveFileOrFolder() {
        //TODO MOVE

    }


}
