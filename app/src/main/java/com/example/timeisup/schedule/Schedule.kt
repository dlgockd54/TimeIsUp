package com.example.timeisup.schedule

import com.google.android.libraries.places.api.model.Place
import java.util.*

/**
 * Created by hclee on 2019-05-17.
 */

data class Schedule(val mDate: Date, val mPlace: Place, var isConfirmed: Boolean = false)