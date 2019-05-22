package com.example.timeisup.schedule

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

class ScheduleListAdapter(private var mScheduleList: LinkedList<Schedule>, private val mGlideRequestManager: RequestManager)
    : RecyclerView.Adapter<ScheduleListAdapter.ScheduleViewHolder>() {
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
            mGlideRequestManager.load(R.raw.schedule_icon)
                .into(it.mScheduleImageView)

            val calendar: Calendar = Calendar.getInstance().apply {
                mScheduleList[position].getTime()?.let {
                    timeInMillis = it
                }
            }

            Log.d(TAG, "${calendar.get(Calendar.YEAR) - 2000}")
            Log.d(TAG, "${calendar.get(Calendar.MONTH) + 1}")
            Log.d(TAG, "${calendar.get(Calendar.DAY_OF_MONTH)}")
            Log.d(TAG, "${calendar.get(Calendar.HOUR_OF_DAY)}")
            Log.d(TAG, "${calendar.get(Calendar.MINUTE)}")

            it.mScheduleTextView.text = setScheduleTextView(calendar)
        }
    }

    private fun setScheduleTextView(calendar: Calendar): CharSequence {
        val year: Int = calendar.get(Calendar.YEAR) - 2000
        val month: Int = calendar.get(Calendar.MONTH) + 1
        val dayOfMonth: Int = calendar.get(Calendar.DAY_OF_MONTH)
        val hour: Int = calendar.get(Calendar.HOUR_OF_DAY)
        val minute: Int = calendar.get(Calendar.MINUTE)
        val ret: String = "${year}년 ${month}월 ${dayOfMonth}일 ${hour}시 ${minute}분"

        return ret
    }

    fun replaceScheduleList(scheduleList: LinkedList<Schedule>) {
        mScheduleList = scheduleList
    }

    class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mScheduleImageView: ImageView = itemView.findViewById(R.id.iv_schedule)
        val mScheduleTextView: TextView = itemView.findViewById(R.id.tv_schedule)
    }
}