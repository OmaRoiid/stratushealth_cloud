package com.silverkey.phr.PHR.UploadedFilesScreen

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.silverkey.phr.Model.Folder
import com.silverkey.phr.R
import kotlinx.android.synthetic.main.file_item_layout.view.*
import kotlinx.android.synthetic.main.layout_file.view.*
import kotlinx.android.synthetic.main.layout_folder_item.view.*

/**
 * TODO: Add class header
 */
class FolderAdapter(private val mContext:Context,private val userFolder:Folder?,private val listener: UploadedFilesScreenListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_FOLDER = 0
    private val VIEW_TYPE_FILES = 1
    private val VIEW_TYPE_TITLE = 2
    private val folderSize:Int=userFolder?.folders?.size!! + userFolder.files?.size!! + 2
    private val internalFoldersSize=userFolder?.folders?.size!!
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        when (viewType) {
            VIEW_TYPE_FOLDER -> {
                var folderView
                        : View = LayoutInflater.from(parent?.context).inflate(R.layout.layout_folder_item, parent, false)
                return FolderViewHolder(folderView)
            }

            VIEW_TYPE_FILES -> {
                var fileView: View = LayoutInflater.from(parent?.context).inflate(R.layout.file_item_layout, parent, false)
                return FileViewHolder(fileView)
            }

            VIEW_TYPE_TITLE -> {
                var fileTextView: View = LayoutInflater.from(parent?.context).inflate(R.layout.layout_file, parent, false)
                return FileLayout(fileTextView)
            }

        }
        return null
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {

        when (holder?.itemViewType) {

            VIEW_TYPE_FOLDER -> {
                (holder as FolderAdapter.FolderViewHolder).folderView.tv_folder_name.text = userFolder?.folders!![position-1].Foldername
                holder.folderView.folder_layout.setOnClickListener {
                    openFolder(userFolder.folders!![position-1],userFolder.folders!![position-1].Foldername)

                }
                holder.folderView.iv_folder_dots.setOnClickListener {
                    userFolder.folders!![position-1].Foldername.let { it1 -> UploadedFilesScreenController.showDotsBottomSheet(R.layout.layout_dots_bottom_sheet, mContext, it1!!, listener) }
                    notifyDataSetChanged()
                }
            }

            VIEW_TYPE_FILES -> {
                (holder as FolderAdapter.FileViewHolder).fileView.file_name_text_view.text = userFolder?.files!![position-internalFoldersSize-2].Filename
                (holder).fileView.iv_files_dots.setOnClickListener {
                    UploadedFilesScreenController.showDotsBottomSheet(R.layout.layout_dots_bottom_sheet, mContext, userFolder.files!![position-internalFoldersSize-2].Filename!!, listener)
                    notifyDataSetChanged()
                }
            }

            VIEW_TYPE_TITLE -> {
                var tittle=""
                if (position == 0 && userFolder?.folders?.size!=0) {
                    tittle = "Folders"
                    (holder as FolderAdapter.FileLayout).itemView.file_text_layout.text = tittle
                }
                else if (position != 0 && userFolder?.files?.size!=0) {
                    tittle="Files"
                    (holder as FolderAdapter.FileLayout).itemView.file_text_layout.text = tittle
                }
            }
        }
    }

    override fun getItemCount(): Int =folderSize

    override fun getItemViewType(position: Int): Int {
        if (position == 0)
            return VIEW_TYPE_TITLE
        else if (position <= internalFoldersSize)
            return VIEW_TYPE_FOLDER
        else if (position==internalFoldersSize+1)
            return VIEW_TYPE_TITLE
        else
            return VIEW_TYPE_FILES
    }

    inner class FileViewHolder(val fileView: View) : RecyclerView.ViewHolder(fileView)
    inner class FolderViewHolder(val folderView: View) : RecyclerView.ViewHolder(folderView){
        /*init{
            folderView.setOnClickListener {
                val folderdetailIntent=Intent(mContext,FolderDetailsScreen::class.java)
                folderdetailIntent.putExtra("Folder_Name",folderView.file_name_text_view.text.toString())

            }


        }*/
    }
    inner class FileLayout(val file: View) : RecyclerView.ViewHolder(file)

    private  fun openFolder(item: Folder,folderName:String?){
        UploadedFilesScreenController.currentFolder = item
        val intent = Intent(mContext, UploadedFilesScreen::class.java)
        intent.putExtra("Flag",true)
        mContext.startActivity(intent)
    }
}