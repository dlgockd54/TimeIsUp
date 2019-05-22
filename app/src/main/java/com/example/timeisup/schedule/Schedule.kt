package com.example.timeisup.schedule

import com.google.android.libraries.places.api.model.Place
import java.util.*

/**
 * Created by hclee on 2019-05-17.
 */

data class Schedule(val mCalendar: Calendar, val mPlace: Place, var isConfirmed: Boolean = false)
data class TestClass(val p0: String, val p1: String, val p2: String)