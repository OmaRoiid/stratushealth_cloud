package com.silverkey.scops.API

import java.util.*

/**
 * Created by SeMbA on 7/25/2016.
 */
class Reply {

    public class Many<T> {
        var items: ArrayList<T>? = null
        var v: ReplyStatus? = null
    }

    public class Single<T> {
        var item: T? = null
        var status: ReplyStatus? = null
    }

    public class Paged<T> {
        var pagingInfo: WithPagingInfo<T>? = null
        var status: ReplyStatus? = null
    }

    public class WithPagingInfo<T> {
        var items: ArrayList<T>? = null
        var paging: PagingInfo? = null
    }

    public class ReplyStatus {

        var status: Int
        var message: String
        var sessionId: String?
        var validationErrors: HashMap<String, ArrayList<String>>? = null

        constructor(status: Int, message: String, sessionId: String?, validationErrors: HashMap<String, ArrayList<String>>?) {
            this.status = status
            this.message = message
            this.sessionId = sessionId
            this.validationErrors = validationErrors
        }
    }

    public class PagingInfo {

        var currentPage: Int
        var pageSize: Int
        var totalResults: Int
        var totalPages: Int

        constructor(currentPage: Int, pageSize: Int, totalResults: Int, totalPages: Int) {
            this.currentPage = currentPage
            this.pageSize = pageSize
            this.totalResults = totalResults
            this.totalPages = totalPages
        }
    }

    class Response<R, S> {
        var requestPayload: R? = null
        var responsePayload: S? = null
        var statusCode: Int? = null
        var errors: HashMap<String, ArrayList<String>>? = null
        var reason: String? = null
    }

}