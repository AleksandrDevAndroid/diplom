package ru.netology.nmedia.util

import androidx.core.R
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DatePickerHelper(
    private val fragmentManager: FragmentManager,
    private val onDateSelected: (String) -> Unit
) {

    fun showDateRangePicker() {
        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("")
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
}