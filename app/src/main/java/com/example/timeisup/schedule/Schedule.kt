package com.example.timeisup.schedule

import com.google.android.gms.maps.model.LatLng

/**
 * Created by hclee on 2019-05-17.
 */

class Schedule() {
    private var time: Long? = 0
    private var latLng: LatLng? = null
    private var isConfirmed: Boolean? = false

    constructor(time: Long?, latLng: LatLng?, isConfirmed: Boolean?): this() {
        this.time = time
        this.latLng = latLng
        this.isConfirmed = isConfirmed
    }

    fun getTime(): Long? = time
    fun getLatLng(): LatLng? = latLng
    fun getIsConfirmed(): Boolean? = isConfirmed
}