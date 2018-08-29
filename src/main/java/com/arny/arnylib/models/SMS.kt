package com.arny.arnylib.models

data class SMS(var _id: String? = null) {
    var address: String? = null
    var body: String? = null
    var service_center: String? = null
    var subject: String? = null
    var creator: String? = null//not changed by app
    var date: Long = 0
    var date_sent: Long = 0
    var error_code: Int = 0
    var _count: Int = 0
    var person: Int = 0
    var status: Int = 0
    var isRead: Boolean = false
    var isLocked: Boolean = false
    var isSeen: Boolean = false

    override fun toString(): String {
        return "_id:$_id address:$address creator:$creator person:$person"
    }

    companion object {
        val STATUS_COMPLETE = 0
        val STATUS_FAILED = 64
        val STATUS_NONE = -1
        val STATUS_PENDING = 32
        val MESSAGE_TYPE_ALL = 0
        val MESSAGE_TYPE_DRAFT = 3
        val MESSAGE_TYPE_FAILED = 5
        val MESSAGE_TYPE_INBOX = 1
        val MESSAGE_TYPE_OUTBOX = 4
        val MESSAGE_TYPE_QUEUED = 6
        val MESSAGE_TYPE_SENT = 2
    }
}
