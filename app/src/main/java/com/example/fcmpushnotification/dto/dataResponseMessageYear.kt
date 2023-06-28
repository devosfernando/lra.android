package com.example.fcmpushnotification.dto

class dataResponseMessageYear {

    var ultimo_dia_del_mes: String? = null
    var ether: String? = null
    var host: String? = null
    var kpiestimado: String? = null
    var kpireal: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as dataResponseMessageYear

        if (ultimo_dia_del_mes != other.ultimo_dia_del_mes) return false
        if (ether != other.ether) return false
        if (host != other.host) return false
        if (kpiestimado != other.kpiestimado) return false
        if (kpireal != other.kpireal) return false

        return true
    }

    override fun hashCode(): Int {
        var result = ultimo_dia_del_mes?.hashCode() ?: 0
        result = 31 * result + (ether?.hashCode() ?: 0)
        result = 31 * result + (host?.hashCode() ?: 0)
        result = 31 * result + (kpiestimado?.hashCode() ?: 0)
        result = 31 * result + (kpireal?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "dataResponseMessageYear(ultimo_dia_del_mes=$ultimo_dia_del_mes, ether=$ether, host=$host, kpiestimado=$kpiestimado, kpireal=$kpireal)"
    }


}