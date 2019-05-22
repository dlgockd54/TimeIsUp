package com.example.timeisup.scheduleadding

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.Toast.*
import com.example.timeisup.BaseActivity
import com.example.timeisup.R
import com.example.timeisup.googlemap.MapsActivity
import com.google.android.libraries.places.api.model.Place
import kotlinx.android.synthetic.main.activity_schedule_adding.*
import java.util.*

class ScheduleAddingActivity
    : BaseActivity(), ScheduleAddingContract.View, DatePickerDialog.OnDateSetListener
    , View.OnClickListener {
    private val TAG: String = ScheduleAddingActivity::class.java.simpleName

    companion object {
        const val REQUEST_CODE: Int = 123
    }

    override lateinit var mPresenter: ScheduleAddingContract.Presenter
    lateinit var mAddDateImageView: ImageView
    lateinit var mEditDateImageView: ImageView
    lateinit var mDatePickerDialog: DatePickerDialog
    lateinit var mAddDateTextView: TextView
    lateinit var mDateTextView: TextView
    lateinit var mPlaceTextView: TextView
    lateinit var mAddPlaceImageView: ImageView
    lateinit var mEditPlaceImageView: ImageView
    lateinit var mAddPlaceTextView: TextView
    lateinit var mAddDateRelativeLayout: RelativeLayout
    lateinit var mAddPlaceRelativeLayout: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_adding)
        setSupportActionBar(toolbar_schedule_adding as Toolbar)

        Log.d(TAG, "onCreate()")

        init()
    }

    private fun init() {
        mAddDateImageView = iv_add_date
        mEditDateImageView = iv_edit_date
        mAddDateTextView = tv_add_date
        mDateTextView = tv_date
        mAddPlaceImageView = iv_add_place
        mEditPlaceImageView = iv_edit_place
        mPlaceTextView = tv_place
        mAddPlaceTextView = tv_add_place
        mAddDateRelativeLayout = rl_add_date.apply {
            setOnClickListener(this@ScheduleAddingActivity)
        }
        mAddPlaceRelativeLayout = rl_add_place.apply {
            setOnClickListener(this@ScheduleAddingActivity)
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mDatePickerDialog = DatePickerDialog(this
                , this@ScheduleAddingActivity
                , Calendar.getInstance().get(Calendar.YEAR)
                , Calendar.getInstance().get(Calendar.MONTH)
                , Calendar.getInstance().get(Calendar.DATE)).apply {
                datePicker.minDate = Date().time
            }
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        Log.d(TAG, "onDateSet()")

        val date: String = "${year}년 ${month + 1}월 ${dayOfMonth}일"

        Toast.makeText(this, date, LENGTH_SHORT).show()

        mAddDateImageView.visibility = View.INVISIBLE
        mEditDateImageView.visibility = View.VISIBLE
        mAddDateTextView.text = resources.getString(R.string.edit_date)
        mDateTextView.text = date
        mDateTextView.visibility = View.VISIBLE
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.rl_add_date -> {
                mDatePickerDialog.show()
            }
            R.id.rl_add_place -> {
                startActivityForResult(Intent(this, MapsActivity::class.java), REQUEST_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE) {
            Log.d(TAG, "resultCode: $resultCode")

            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place: Place? = data.getParcelableExtra("place") ?: null
                        val placeName: String? = place?.name ?: "place.name is null!!"

                        Log.d(TAG, "place name: $placeName")

                        placeName?.let {
                            mPlaceTextView.text = placeName
                            mPlaceTextView.visibility = View.VISIBLE
                            mAddPlaceImageView.visibility = View.INVISIBLE
                            mEditPlaceImageView.visibility = View.VISIBLE
                            mAddPlaceTextView.text = resources.getText(R.string.edit_place)
                        }
                    }
                }
                Activity.RESULT_CANCELED -> {

                }
                else -> {

                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        Log.d(TAG, "onBackPressed()")
        overridePendingTransition(R.anim.animation_slide_from_left, R.anim.animation_slide_to_right)
    }
}
