package com.example.timeisup.scheduleadding

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import android.widget.*
import com.example.timeisup.BaseActivity
import com.example.timeisup.R
import com.example.timeisup.googlemap.MapsActivity
import com.example.timeisup.notification.ScheduleNotificationManager
import com.example.timeisup.schedule.Schedule
import com.example.timeisup.schedule.ScheduleListAdapter
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.suke.widget.SwitchButton
import kotlinx.android.synthetic.main.activity_schedule_adding.*
import java.util.*

class ScheduleAddingActivity
    : BaseActivity(), ScheduleAddingContract.View, DatePickerDialog.OnDateSetListener
    , TimePickerDialog.OnTimeSetListener, View.OnClickListener, SwitchButton.OnCheckedChangeListener {
    private val TAG: String = ScheduleAddingActivity::class.java.simpleName

    companion object {
        const val REQUEST_CODE: Int = 123
    }

    override lateinit var mPresenter: ScheduleAddingContract.Presenter

    lateinit var mScheduleNameEditText: EditText
    lateinit var mAddDateImageView: ImageView
    lateinit var mEditDateImageView: ImageView
    lateinit var mDatePickerDialog: DatePickerDialog
    lateinit var mTimePickerDialog: TimePickerDialog
    lateinit var mAddDateTextView: TextView
    lateinit var mDateTextView: TextView
    lateinit var mPlaceTextView: TextView
    lateinit var mAddPlaceImageView: ImageView
    lateinit var mEditPlaceImageView: ImageView
    lateinit var mAddPlaceTextView: TextView
    lateinit var mTimeTextView: TextView
    lateinit var mAddTimeTextView: TextView
    lateinit var mAddTimeImageView: ImageView
    lateinit var mEditTimeImageView: ImageView
    lateinit var mAddScheduleButton: Button
    lateinit var mCancelButton: Button
    lateinit var mEditScheduleButton: Button
    lateinit var mScheduleConfirmSwitchButton: SwitchButton
    lateinit var mFixScheduleTextView: TextView
    lateinit var mAddDateRelativeLayout: RelativeLayout
    lateinit var mAddTimeRelativeLayout: RelativeLayout
    lateinit var mAddPlaceRelativeLayout: RelativeLayout

    private lateinit var mScheduleCalendar: Calendar
    private var mScheduleTime: Long = 0
    private var mSchedulePlace: Place? = null
    private lateinit var mScheduleKey: String
    private var mIsScheduleNameSet: Boolean = false
    private var mIsDateSet: Boolean = false
    private var mIsTimeSet: Boolean = false
    private var mIsPlaceSet: Boolean = false
    private var mIsScheduleConfirmed: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_adding)
        setSupportActionBar(toolbar_schedule_adding as Toolbar)

        Log.d(TAG, "onCreate()")

        val intent: Intent? = intent

        init()

        intent?.let {
            if(it.hasExtra(ScheduleListAdapter.SCHEDULE_EXTRA)) {
                setAsPredefinedSchedule(it)
            }
        }
    }

    private fun init() {
        mPresenter = ScheduleAddingPresenter(this)
        mScheduleNameEditText = et_schedule_name
        mAddDateImageView = iv_add_date
        mEditDateImageView = iv_edit_date
        mAddDateTextView = tv_add_date
        mDateTextView = tv_date
        mAddPlaceImageView = iv_add_place
        mEditPlaceImageView = iv_edit_place
        mTimeTextView = tv_time
        mAddTimeTextView = tv_add_time
        mAddTimeImageView = iv_add_time
        mEditTimeImageView = iv_edit_time
        mPlaceTextView = tv_place
        mAddPlaceTextView = tv_add_place
        mFixScheduleTextView = tv_fix_schedule
        mScheduleConfirmSwitchButton = sb_confirm_schedule.apply {
            setOnCheckedChangeListener(this@ScheduleAddingActivity)
        }
        mAddScheduleButton = btn_add_schedule.apply {
            setOnClickListener(this@ScheduleAddingActivity)
        }
        mEditScheduleButton = btn_edit_schedule.apply {
            setOnClickListener(this@ScheduleAddingActivity)
        }
        mCancelButton = btn_cancel.apply {
            setOnClickListener(this@ScheduleAddingActivity)
        }
        mAddDateRelativeLayout = rl_add_date.apply {
            setOnClickListener(this@ScheduleAddingActivity)
        }
        mAddTimeRelativeLayout = rl_add_time.apply {
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

        mTimePickerDialog = TimePickerDialog(this,
            this@ScheduleAddingActivity,
            Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
            Calendar.getInstance().get(Calendar.MINUTE),
            true)

        mScheduleCalendar = Calendar.getInstance()
    }

    private fun setAsPredefinedSchedule(intent: Intent) {
        Log.d(TAG, "setAsPredefinedSchedule()")

        val extrasArray: Array<Any?> = (intent.getSerializableExtra(ScheduleListAdapter.SCHEDULE_EXTRA) as Array<Any?>)
        val time: Long = extrasArray[0] as Long
        val placeName: String = extrasArray[1] as String
        val latitude: Double = extrasArray[2] as Double
        val longitude: Double = extrasArray[3] as Double
        val isConfirmed: Boolean = extrasArray[4] as Boolean
        val scheduleName: String = extrasArray[5] as String
        val key: String = extrasArray[6] as String
        var year: Int
        var month: Int
        var day: Int
        var hour: Int
        var minute: Int
        var dateText: String = ""
        var timeText: String = ""

        mScheduleNameEditText.text = SpannableStringBuilder(scheduleName)
        mScheduleCalendar.let {
            it.timeInMillis = time
            year = it.get(Calendar.YEAR)
            month = it.get(Calendar.MONTH)
            day = it.get(Calendar.DAY_OF_MONTH)
            hour = it.get(Calendar.HOUR_OF_DAY)
            minute = it.get(Calendar.MINUTE)
            mDatePickerDialog.updateDate(year, month, day)
            mTimePickerDialog.updateTime(hour, minute)
            mScheduleTime = mScheduleCalendar.timeInMillis
            dateText = "${year}년 ${month + 1}월 ${day}일"
            timeText = "${hour}시 ${minute}분"
        }
        mAddDateImageView.visibility = View.INVISIBLE
        mEditDateImageView.visibility = View.VISIBLE
        mAddDateTextView.text = resources.getString(R.string.edit_date)
        mDateTextView.text = dateText
        mDateTextView.visibility = View.VISIBLE

        mAddTimeImageView.visibility = View.INVISIBLE
        mEditTimeImageView.visibility = View.VISIBLE
        mAddTimeTextView.text = resources.getString(R.string.edit_time)
        mTimeTextView.text = timeText
        mTimeTextView.visibility = View.VISIBLE

        mAddPlaceImageView.visibility = View.INVISIBLE
        mEditPlaceImageView.visibility = View.VISIBLE
        mAddPlaceTextView.text = resources.getString(R.string.edit_place)
        mPlaceTextView.text = placeName
        mPlaceTextView.visibility = View.VISIBLE

        mSchedulePlace = ExtendedPlace(placeName, LatLng(latitude, longitude))

        mAddScheduleButton.visibility = View.GONE
        mEditScheduleButton.visibility = View.VISIBLE

        mScheduleKey = key
        mIsScheduleNameSet = true
        mIsDateSet = true
        mIsTimeSet = true
        mIsPlaceSet = true
        mIsScheduleConfirmed = isConfirmed

        if(mIsScheduleConfirmed) {
            mScheduleConfirmSwitchButton.toggle(true)
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        Log.d(TAG, "onDateSet()")

        val date: String = "${year}년 ${month + 1}월 ${dayOfMonth}일"

        mAddDateImageView.visibility = View.INVISIBLE
        mEditDateImageView.visibility = View.VISIBLE
        mAddDateTextView.text = resources.getString(R.string.edit_date)
        mDateTextView.text = date
        mDateTextView.visibility = View.VISIBLE

        mScheduleCalendar.run {
            set(year, month, dayOfMonth)
        }
        mScheduleTime = mScheduleCalendar.timeInMillis
        mIsDateSet = true
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        Log.d(TAG, "onTimeSet()")

        val time: String = "${hourOfDay}시 ${minute}분"

        Log.d(TAG, "time: $time")

        mAddTimeImageView.visibility = View.INVISIBLE
        mEditTimeImageView.visibility = View.VISIBLE
        mAddTimeTextView.text = resources.getString(R.string.edit_time)
        mTimeTextView.text = time
        mTimeTextView.visibility = View.VISIBLE

        mScheduleCalendar.run {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
        }
        mScheduleTime = mScheduleCalendar.timeInMillis
        mIsTimeSet = true
    }

    override fun onCheckedChanged(view: SwitchButton?, isChecked: Boolean) {
        Log.d(TAG, "onCheckedChanged()")

        mIsScheduleConfirmed = isChecked

        if(mIsScheduleConfirmed) {
            mFixScheduleTextView.text = resources.getString(R.string.unconfirm_schedule)
        }
        else {
            mFixScheduleTextView.text = resources.getString(R.string.confirm_schedule)
        }

        Log.d(TAG, "mIsScheduleFix: $mIsScheduleConfirmed")
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.rl_add_date -> {
                mDatePickerDialog.show()
            }
            R.id.rl_add_time -> {
                mTimePickerDialog.show()
            }
            R.id.rl_add_place -> {
                startActivityForResult(Intent(this, MapsActivity::class.java), REQUEST_CODE)
            }
            R.id.btn_add_schedule -> {
                var schedule: Schedule = Schedule()
                val scheduleName: String = mScheduleNameEditText.text.toString()

                mIsScheduleNameSet = scheduleName.isNotEmpty()

                if(!mIsScheduleNameSet) {
                    Toast.makeText(this, "일정 이름을 설정해주세요.", Toast.LENGTH_SHORT).show()
                }
                else if(!mIsDateSet) {
                    Toast.makeText(this, "날짜를 설정해주세요.", Toast.LENGTH_SHORT).show()
                }
                else if(!mIsTimeSet) {
                    Toast.makeText(this, "시간을 설정해주세요.", Toast.LENGTH_SHORT).show()
                }
                else if(!mIsPlaceSet) {
                    Toast.makeText(this, "위치를 설정해주세요.", Toast.LENGTH_SHORT).show()
                }
                else {
                    mSchedulePlace?.let {
                        schedule = Schedule(scheduleName, mScheduleTime, it.name, it.latLng?.latitude, it.latLng?.longitude, mIsScheduleConfirmed)

                        mPresenter.addScheduleToDatabase(schedule)
                        onBackPressed()
                    }
                }
            }
            R.id.btn_edit_schedule -> {
                val scheduleName: String = mScheduleNameEditText.text.toString()

                mIsScheduleNameSet = scheduleName.isNotEmpty()

                if(mIsScheduleNameSet) {
                    val schedule: Schedule = Schedule(scheduleName, mScheduleTime, mSchedulePlace?.name,
                        mSchedulePlace?.latLng?.latitude, mSchedulePlace?.latLng?.longitude, mIsScheduleConfirmed)

                    mPresenter.reschedule(mScheduleKey, schedule)
                    onBackPressed()
                }
                else {
                    Toast.makeText(this, "일정 이름을 설정해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.btn_cancel -> {
                onBackPressed()
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

                        place?.let {
                            mSchedulePlace = it
                            mIsPlaceSet = true
                        }

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
        finish()
        overridePendingTransition(R.anim.animation_slide_from_left, R.anim.animation_slide_to_right)
    }
}
