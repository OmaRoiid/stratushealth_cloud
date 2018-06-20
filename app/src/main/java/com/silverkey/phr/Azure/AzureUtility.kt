package com.silverkey.phr.Azure

import android.os.Environment
import com.microsoft.azure.storage.blob.CloudBlobContainer
import com.microsoft.azure.storage.blob.BlobContainerPermissions
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType
import com.microsoft.azure.storage.blob.CloudBlockBlob
import com.silverkey.phr.Model.UserFile
import com.silverkey.phr.Helpers.Shared
import com.silverkey.phr.Model.All
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.URI

/**
 * Created by Mohamed Taher on 2/5/2018.
 * Updated by Omar Salem on 5/27/2018
 */

object AzureUtility {

    private fun initRequest(): CloudBlobContainer?{

        try {
            val container = CloudBlobContainer(URI(Shared.sasToken))
            return container
        }
        catch (ex: Exception ){
            return null
        }

    }

    fun createContainerIfNotExists(containerName: String): Boolean {

        val container = initRequest() ?: return false
        container.createIfNotExists()

        val containerPermissions = BlobContainerPermissions()
        containerPermissions.publicAccess = BlobContainerPublicAccessType.BLOB
        container.uploadPermissions(containerPermissions)

        val existsResult = container.exists()
        return existsResult
    }

    fun getUserFiles (): ArrayList<All>? {

        try {
            val files = ArrayList<All>()
            val container = initRequest() ?: return null
            for (blobItem in container.listBlobs()) {
                files.add(All( UserFile(Shared.getLastBitFromUrl(blobItem.uri.toString())),null))
            }
            return files

        } catch (ex: Exception ){
            return null
        }

    }

    fun createFile(containerName: String, name: String, filePath: String): Boolean {

        try {
            val container = initRequest() ?: return false
            val blob: CloudBlockBlob = container.getBlockBlobReference(name) // Create or overwrite the file
            val source = File(filePath)
            blob.upload(FileInputStream(source), source.length())
            return true

        } catch (ex: Exception ){
            return false
        }
    }

    fun downloadFile(blobName: String): Boolean {
        try {
            val container = initRequest()
            val blobRef = container?.getBlobReferenceFromServer(blobName)
            val downloadFolder = Environment.getExternalStorageDirectory().path+"/${Shared.sharedFileName}"
            blobRef?.download(FileOutputStream(downloadFolder + "/"+ blobName))

            return true
        } catch(ex: Exception) {
            return false
        }
    }

    fun deleteFile(uniqueFileIdentifier: String):Boolean {
        try {
            val container = initRequest() ?: return false
        val blob: CloudBlockBlob = container.getBlockBlobReference(uniqueFileIdentifier)
        blob.deleteIfExists()
            return true
        }catch (ex: Exception ){
            return false
        }
    }
    /*fun createFolder(folderName:String):Boolean
    {
        try {
            val container = initRequest() ?: return false
            val directory=container.getDirectoryReference(folderName)
            val blob: CloudBlockBlob = directory.getBlockBlobReference(folderName+".txt")
            blob.uploadFromByteArray(byteArrayOf(0), 0, 0)
            return true
        }catch (ex: Exception ){
            return false
        }
    }*/

    fun createFolder(folderName:String):Boolean{
        try {
            val container = initRequest() ?: return false
            val blob: CloudBlockBlob = container.getBlockBlobReference(folderName)
            blob.uploadText(folderName)
            return true
        }catch (ex: Exception ){
            return false
        }
    }
}



















