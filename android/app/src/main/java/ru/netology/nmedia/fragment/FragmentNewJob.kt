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
import ru.netology.nmedia.viewmodel.JobViewModel

class FragmentNewJob : Fragment() {
    private val viewModelJob: JobViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentNewJobBinding.inflate(inflater, container, false)
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.createJob.setOnClickListener {
            val name = binding.enterName.text.toString().trim()
            val position = binding.enterPosition.text.toString().trim()
            val link = binding.enterLink.text.toString().trim()
            val date = binding.enterDate.text.toString().trim()
            if (name.isEmpty() || position.isEmpty()) {
                Toast.makeText(context, R.string.emptyPositionOrName, LENGTH_LONG).show()
                return@setOnClickListener
            }
            val job = Job(0, name, position, link, date, ownerId = 0)
            viewModelJob.saveJob(job)
            findNavController().navigateUp()
        }
        return binding.root
    }

}