package com.example.timeisup.scheduleadding

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.libraries.places.api.model.*

/**
 * Created by hclee on 2019-05-28.
 */

/**
 * This class must be used only when reschedule.
 */
class ExtendedPlace() : Place() {
    private lateinit var mPlaceName: String
    private lateinit var mLatLng: LatLng

    constructor(parcel: Parcel) : this() {
    }

    constructor(placeName: String, latLng: LatLng): this() {
        mPlaceName = placeName
        mLatLng = latLng
    }

    override fun getUserRatingsTotal(): Int? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getName(): String? = mPlaceName

    override fun getOpeningHours(): OpeningHours? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getId(): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPhotoMetadatas(): MutableList<PhotoMetadata> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getWebsiteUri(): Uri? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPhoneNumber(): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRating(): Double? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPriceLevel(): Int? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAddressComponents(): AddressComponents? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAttributions(): MutableList<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAddress(): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPlusCode(): PlusCode? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTypes(): MutableList<Type> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getViewport(): LatLngBounds? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun describeContents(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLatLng(): LatLng? = mLatLng

    companion object CREATOR : Parcelable.Creator<ExtendedPlace> {
        override fun createFromParcel(parcel: Parcel): ExtendedPlace {
            return ExtendedPlace(parcel)
        }

        override fun newArray(size: Int): Array<ExtendedPlace?> {
            return arrayOfNulls(size)
        }
    }
}