package ru.netology.nmedia.util

import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class DatePickerHelper(
    private val fragmentManager: FragmentManager,
    private val onDateSelected: (String) -> Unit,
) {

    fun showDateRangePicker() {
        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .build()

        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            val start = Date(selection.first)
            val end = Date(selection.second)
            val format = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val period = "${format.format(start)} – ${format.format(end)}"
            onDateSelected(period)
        }
        dateRangePicker.show(fragmentManager, "dateRangePicker")
    }

    fun showDateTime() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                timeInMillis = selection
            }

            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .build()

            timePicker.addOnPositiveButtonClickListener {
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
                calendar.set(Calendar.MINUTE, timePicker.minute)
                calendar.set(Calendar.SECOND, 0)

                val format = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                onDateSelected(format.format(calendar.time))
            }

            timePicker.show(fragmentManager, "materialTimePicker")
        }

        datePicker.show(fragmentManager, "materialDatePicker")
    }
}