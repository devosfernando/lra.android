package com.example.fcmpushnotification.dto

class dataResponseMessage {

    var hist_date: String? = null
    var hist_EjecEther: String? = null
    var hist_EjecHost: String? = null
    var hist_kpiEstimado: String? = null
    var hist_kpiReal: String? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as dataResponseMessage

        if (hist_date != other.hist_date) return false
        if (hist_EjecEther != other.hist_EjecEther) return false
        if (hist_EjecHost != other.hist_EjecHost) return false
        if (hist_kpiEstimado != other.hist_kpiEstimado) return false
        if (hist_kpiReal != other.hist_kpiReal) return false

        return true
    }

    override fun hashCode(): Int {
        var result = hist_date?.hashCode() ?: 0
        result = 31 * result + (hist_EjecEther?.hashCode() ?: 0)
        result = 31 * result + (hist_EjecHost?.hashCode() ?: 0)
        result = 31 * result + (hist_kpiEstimado?.hashCode() ?: 0)
        result = 31 * result + (hist_kpiReal?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "dataResponseMessage(hist_date=$hist_date, hist_EjecEther=$hist_EjecEther, hist_EjecHost=$hist_EjecHost, hist_kpiEstimado=$hist_kpiEstimado, hist_kpiReal=$hist_kpiReal)"
    }


}