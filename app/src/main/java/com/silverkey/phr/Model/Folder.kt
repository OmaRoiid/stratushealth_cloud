package com.silverkey.phr.Model



class FileItem {
    var Filename: String? = null

    constructor(name: String?) {
        this.Filename = name
    }
}

class Folder {
    var Foldername: String? = null
    var folders: ArrayList<Folder>? = null
    var files: ArrayList<FileItem>? = null
    var path: String? = null

    constructor() {
        this.folders = ArrayList()
        this.files = ArrayList()
    }

    constructor(name: String?) {
        this.Foldername = name
        this.folders = ArrayList()
        this.files = ArrayList()
    }

    constructor(name: String?, folders: ArrayList<Folder>?, files: ArrayList<FileItem>?) {
        this.Foldername = name
        this.folders = folders
        this.files = files
    }
}