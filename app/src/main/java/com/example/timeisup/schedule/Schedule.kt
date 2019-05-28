package com.example.timeisup.schedule

/**
 * Created by hclee on 2019-05-17.
 */

class Schedule() {
    private var time: Long? = 0
    private var placeName: String? = ""
    private var latitude: Double? = 0.0
    private var longitude: Double? = 0.0
    private var isConfirmed: Boolean? = false

    constructor(time: Long?, placeName: String?, latitude: Double?, longitude: Double?, isConfirmed: Boolean?): this() {
        this.time = time
        this.placeName = placeName
        this.latitude = latitude
        this.longitude = longitude
        this.isConfirmed = isConfirmed
    }

    fun getTime(): Long? = time
    fun getPlaceName(): String? = placeName
    fun getLatitude(): Double? = latitude
    fun getLongitude(): Double? = longitude
    fun getIsConfirmed(): Boolean? = isConfirmed

    fun setTime(time: Long) {
        this.time = time
    }

    fun setPlaceName(placeName: String) {
        this.placeName = placeName
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