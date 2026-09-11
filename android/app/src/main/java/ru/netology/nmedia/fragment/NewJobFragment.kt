package ru.netology.nmedia.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentNewJobBinding
import ru.netology.nmedia.dto.Job
import ru.netology.nmedia.util.DatePickerHelper
import ru.netology.nmedia.viewmodel.JobViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FragmentNewJob : Fragment() {
    private lateinit var datePickerHelper: DatePickerHelper
    private val viewModelJob: JobViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentNewJobBinding.inflate(inflater, container, false)

        datePickerHelper = DatePickerHelper(
            fragmentManager = childFragmentManager,
            onDateSelected = { period ->
                binding.enterDate.setText(period)
            }
        )


        binding.enterDate.setOnClickListener {
            datePickerHelper.showDateRangePicker()
        }

        binding.createJob.setOnClickListener {
            val name = binding.enterName.text.toString().trim()
            val position = binding.enterPosition.text.toString().trim()
            val link = binding.enterLink.text.toString().trim()
            val dateRange = binding.enterDate.text.toString().trim()
            if (name.isEmpty() || position.isEmpty()) {
                Toast.makeText(context, R.string.emptyPositionOrName, LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (dateRange.isEmpty()) {
                Toast.makeText(context, R.string.choose_period, LENGTH_LONG).show()
                return@setOnClickListener
            }

            val dates = dateRange.split(" – ")
            val startDate = dates[0]
            val finishDate = if (dates.size > 1) dates[1] else null

            val startFormatted = formatDateForServer(startDate)
            val finishFormatted = if (finishDate != null) formatDateForServer(finishDate) else null

            val job = Job(
                id = 0,
                name = name,
                position = position,
                start = startFormatted,
                finish = finishFormatted,
                link = link.ifEmpty { null },
                ownerId = 0
            )
            viewModelJob.saveJob(job)
            findNavController().navigateUp()
        }
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }

}
private fun formatDateForServer(dateString: String): String {
    val inputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    return try {
        val date = inputFormat.parse(dateString)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        dateString
    }
}
