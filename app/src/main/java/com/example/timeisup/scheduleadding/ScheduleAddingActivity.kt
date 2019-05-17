package com.example.timeisup.scheduleadding

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.Toast.*
import com.example.timeisup.BaseActivity
import com.example.timeisup.R
import kotlinx.android.synthetic.main.activity_schedule_adding.*
import java.util.*

class ScheduleAddingActivity
    : BaseActivity(), ScheduleAddingContract.View, DatePickerDialog.OnDateSetListener
    , View.OnClickListener {
    private val TAG: String = ScheduleAddingActivity::class.java.simpleName

    override lateinit var mPresenter: ScheduleAddingContract.Presenter
    lateinit var mAddDateImageView: ImageView
    lateinit var mEditDateImageView: ImageView
    lateinit var mDatePickerDialog: DatePickerDialog
    lateinit var mAddDateTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_adding)
        setSupportActionBar(toolbar_schedule_adding as Toolbar)

        Log.d(TAG, "onCreate()")

        init()
    }

    private fun init() {
        mAddDateImageView = iv_add_date.apply {
            setOnClickListener(this@ScheduleAddingActivity)
        }
        mEditDateImageView = iv_edit_date.apply {
            setOnClickListener(this@ScheduleAddingActivity)
        }
        mAddDateTextView = tv_add_date

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

        val date: String = "picked ${dayOfMonth}/${month}/${year}"

        Toast.makeText(this, date, LENGTH_SHORT).show()

        mAddDateImageView.visibility = View.INVISIBLE
        mEditDateImageView.visibility = View.VISIBLE
        mAddDateTextView.text = resources.getString(R.string.edit_date)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.iv_add_date, R.id.iv_edit_date -> {
                mDatePickerDialog.show()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        Log.d(TAG, "onBackPressed()")
        overridePendingTransition(R.anim.animation_slide_from_left, R.anim.animation_slide_to_right)
    }
}
