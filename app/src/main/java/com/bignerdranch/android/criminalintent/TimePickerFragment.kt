package com.bignerdranch.android.criminalintent

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
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val timeListener = TimePickerDialog.OnTimeSetListener { _: TimePicker, hour: Int, minute: Int ->
            val resultTime : Time = Time(hour, minute, 0)
            targetFragment?.let { fragment -> (fragment as Callbacks).onTimeSelected(resultTime)
            }
        }
        val time = arguments?.getSerializable(ARG_TIME) as Time
        val calendar = Calendar.getInstance()
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