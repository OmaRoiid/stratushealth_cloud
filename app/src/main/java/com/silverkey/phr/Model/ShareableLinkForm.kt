package com.silverkey.phr.Model

/**
 * Created by Mohamed Taher on 3/9/2018.
 */
class ShareableLinkForm {
    var sessionToken: String? = null
    var blobName: String? = null
    var availableForHours: Int? = null

    constructor(sessionToken: String?, blobName: String?, availableForHours: Int?) {
        this.sessionToken = sessionToken
        this.blobName = blobName
        this.availableForHours = availableForHours
    }
}