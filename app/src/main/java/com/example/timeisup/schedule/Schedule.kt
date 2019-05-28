package com.example.timeisup.schedule

/**
 * Created by hclee on 2019-05-17.
 */

class Schedule() {
    private var time: Long? = 0
    private var latitude: Double? = 0.0
    private var longitude: Double? = 0.0
    private var isConfirmed: Boolean? = false

    constructor(time: Long?, latitude: Double?, longitude: Double?, isConfirmed: Boolean?): this() {
        this.time = time
        this.latitude = latitude
        this.longitude = longitude
        this.isConfirmed = isConfirmed
    }

    fun getTime(): Long? = time
    fun getLatitude(): Double? = latitude
    fun getLongitude(): Double? = longitude
    fun getIsConfirmed(): Boolean? = isConfirmed

    fun setTime(time: Long) {
        this.time = time
    }

    fun setLatitude(latitude: Double) {
        this.latitude = latitude
    }

    fun setLongitude(longitude: Double) {
        this.longitude = longitude
    }

    fun setIsConfirmed(isConfirmed: Boolean) {
        this.isConfirmed = isConfirmed
    }
}