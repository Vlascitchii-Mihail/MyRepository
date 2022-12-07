package com.vm.timemanager.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.DatePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*

class DatePickerFragment : DialogFragment() {

    private val args: DatePickerFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?) : Dialog {

        val date =  args.taskDate
//        val date = LocalDateTime.of(2022, 1, 3, 14, 26, 45)
        val initialYear = date.year
        val initialMonth =  date.month.value
        val initialDay = date.dayOfMonth

        //get user's date
        val dateListener = DatePickerDialog.OnDateSetListener {
                _: DatePicker, year: Int, month: Int, day: Int ->

            val resultDate = LocalDateTime.of(year, month + 1, day, date.hour, date.minute)

            //send date to the NewTaskAddingFragment
            setFragmentResult(REQUEST_KEY_DATE, bundleOf(BUNDLE_KEY_DATE to resultDate))
        }

        val calendar = Calendar.getInstance()

//        calendar.time = args.taskDate


//        val initialYear = calendar.get(Calendar.YEAR)
//        val initialMonth = calendar.get(Calendar.MONTH)
//        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(
            requireContext(),

            //user's date
            dateListener,
            initialYear,

            //+ 1 automatically
            initialMonth - 1,
            initialDay
        )
    }


    companion object {
        const val REQUEST_KEY_DATE = "REQUEST_KEY_DATE"
        const val BUNDLE_KEY_DATE = "BUNDLE_KEY_DATE"
    }
}