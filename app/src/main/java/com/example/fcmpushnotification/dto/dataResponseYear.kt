package com.example.fcmpushnotification.dto


class dataResponseYear {

    var status: String? = null
    var message: String? = null
    var response: List<dataResponseMessageYear> ? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as dataResponseYear

        if (status != other.status) return false
        if (message != other.message) return false
        if (response != other.response) return false

        return true
    }

    override fun hashCode(): Int {
        var result = status?.hashCode() ?: 0
        result = 31 * result + (message?.hashCode() ?: 0)
        result = 31 * result + (response?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "dataResponseYear(status=$status, message=$message, response=$response)"
    }


}