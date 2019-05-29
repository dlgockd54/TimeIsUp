package com.example.timeisup.schedule

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.RequestManager
import com.example.timeisup.R
import java.util.*

/**
 * Created by hclee on 2019-05-17.
 */

class ScheduleListAdapter(private val mActivity: ScheduleListActivity, private var mScheduleList: LinkedList<Pair<Schedule,
        String?>>, private val mGlideRequestManager: RequestManager): RecyclerView.Adapter<ScheduleListAdapter.ScheduleViewHolder>() {
    companion object {
        const val SCHEDULE_EXTRA: String = "com.example.timeisup.schedule_extra"
    }

    private val TAG: String = ScheduleListAdapter::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        Log.d(TAG, "onCreateViewHolder()")

        val view: View = (LayoutInflater.from(parent.context).inflate(R.layout.item_schedule, parent, false))
        val viewHolder: ScheduleViewHolder = ScheduleViewHolder(view)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return mScheduleList.size
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder()")

        holder.let {
            val schedule: Schedule = mScheduleList[position].first

            if(!(it.itemView.hasOnClickListeners())) {
                it.itemView.setOnClickListener {
                    val key: String? = mScheduleList[position].second
                    val extrasArray: Array<Any?> = arrayOf(schedule.getTime(),
                        schedule.getPlaceName(),
                        schedule.getLatitude(),
                        schedule.getLongitude(),
                        schedule.getIsConfirmed(),
                        schedule.getScheduleName(),
                        key)
                    val intent: Intent = Intent().apply {
                        putExtra(SCHEDULE_EXTRA, extrasArray)
                    }

                    mActivity.startScheduleAddingActivityToEditSchedule(intent)
                }
            }

            it.itemView.setOnLongClickListener(object: View.OnLongClickListener {
                override fun onLongClick(v: View?): Boolean {
                    Log.d(TAG, "onLongClick()")

                    val key: String? = mScheduleList[position].second

                    mActivity.removeSchedule(key)

                    return true
                }
            })

            mGlideRequestManager.load(R.raw.schedule_icon)
                .into(it.mScheduleImageView)

            it.mScheduleNameTextView.text = schedule.getScheduleName()
            it.mScheduleDescriptionTextView.text = setScheduleDescriptionTextView(schedule)
        }
    }

    private fun setScheduleDescriptionTextView(schedule: Schedule): CharSequence {
        val calendar: Calendar = Calendar.getInstance().apply {
            schedule.getTime()?.let {
                timeInMillis = it
            }
        }
        val placeName: String? = schedule.getPlaceName()
        val month: Int = calendar.get(Calendar.MONTH) + 1
        val dayOfMonth: Int = calendar.get(Calendar.DAY_OF_MONTH)
        val hour: Int = calendar.get(Calendar.HOUR_OF_DAY)
        val minute: Int = calendar.get(Calendar.MINUTE)
        val ret: String = "${placeName}에서 ${month}월 ${dayOfMonth}일 ${hour}시 ${minute}분"

        return ret
    }

    fun replaceScheduleList(scheduleList: LinkedList<Pair<Schedule, String?>>) {
        mScheduleList = scheduleList
    }

    class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mScheduleImageView: ImageView = itemView.findViewById(R.id.iv_schedule)
        val mScheduleNameTextView: TextView = itemView.findViewById(R.id.tv_schedule_name)
        val mScheduleDescriptionTextView: TextView = itemView.findViewById(R.id.tv_schedule_description)
    }
}