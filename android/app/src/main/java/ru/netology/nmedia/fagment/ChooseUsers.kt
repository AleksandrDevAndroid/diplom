package ru.netology.nmedia.fagment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentChooseUsersBinding

class ChooseUsers : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binging = FragmentChooseUsersBinding.inflate(inflater,container,false)

        binging.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
        return binging.root
    }
}