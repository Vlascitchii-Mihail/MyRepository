package com.vm.timemanager.fragments

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import java.time.LocalDateTime
import java.util.*

class TimePickerFragment: DialogFragment() {

    private val args: TimePickerFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreate(savedInstanceState)
        val calendar = Calendar.getInstance()

//        calendar.time = args.taskTime
//        val initialHour = calendar.get(Calendar.HOUR)
//        val initialMinutes = calendar.get(Calendar.MINUTE)
//
//        val initialYear = calendar.get(Calendar.YEAR)
//        val initialMonth = calendar.get(Calendar.MONTH)
//        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)
//
//        val timeListener = TimePickerDialog.OnTimeSetListener {
//                _: TimePicker, hours: Int, minutes: Int ->
//            val resultTime: Date = GregorianCalendar(initialYear, initialMonth, initialDay, hours, minutes).time
//
//            //send date to the NewTaskAddingFragment
//            setFragmentResult(REQUEST_KEY_TIME, bundleOf(BUNDLE_KEY_TIME to resultTime))
//        }

        val date = args.taskTime
        val initialYear = date.year
        val initialMonth = date.month.value
        val initialDay = date.dayOfMonth
        val initialHour = date.hour
        val initialMinutes = date.minute

        val timeListener = TimePickerDialog.OnTimeSetListener {
                _: TimePicker, hours: Int, minutes: Int ->
//            val resultTime: Date = GregorianCalendar(initialYear, initialMonth, initialDay, hours, minutes).time

            val resultTime: LocalDateTime = LocalDateTime.of(initialYear, initialMonth, initialDay, hours, minutes)
            //send date to the NewTaskAddingFragment
            setFragmentResult(REQUEST_KEY_TIME, bundleOf(BUNDLE_KEY_TIME to resultTime))
        }

        return TimePickerDialog(
            requireContext(),

            //user's date
            timeListener,
            initialHour,
            initialMinutes,
            true
        )
    }


    companion object {
        const val REQUEST_KEY_TIME = "REQUEST_KEY_TIME"
        const val BUNDLE_KEY_TIME = "BUNDLE_KEY_TIME"
    }
}