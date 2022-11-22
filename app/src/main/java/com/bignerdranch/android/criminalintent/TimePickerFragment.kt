package com.bignerdranch.android.criminalintent

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.sql.Time
import java.util.*

private const val ARG_TIME = "time"

class TimePickerFragment : DialogFragment() {
    interface Callbacks {
        fun onTimeSelected(time: Date)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val calendarYear = calendar.get(Calendar.YEAR)
        val calendarMonth = calendar.get(Calendar.MONTH)
        val calendarDay = calendar.get(Calendar.DAY_OF_MONTH)
        val timeListener = TimePickerDialog.OnTimeSetListener { _: TimePicker, hour: Int, minute: Int ->

            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            val resultTime : Date = GregorianCalendar(calendarYear, calendarMonth, calendarDay, hour, minute).time
                targetFragment?.let { fragment -> (fragment as Callbacks).onTimeSelected(resultTime)}
        }
        val time = arguments?.getSerializable(ARG_TIME) as Date
        calendar.time = time
        val initialHour = calendar.get(Calendar.HOUR)
        val initialMinute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(
            requireContext(),
            timeListener,
            initialHour,
            initialMinute,
            true
        )
    }

    companion object {
        fun newInstance(time: Date): TimePickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_TIME, time)
            }
            return TimePickerFragment().apply {
                arguments = args
            }
        }
    }
}