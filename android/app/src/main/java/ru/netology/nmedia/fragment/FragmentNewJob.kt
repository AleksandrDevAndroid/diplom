package ru.netology.nmedia.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewJobBinding

class FragmentNewJob : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentNewJobBinding.inflate(inflater,container,false)
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }
}