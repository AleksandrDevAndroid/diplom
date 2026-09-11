package ru.netology.nmedia.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.netology.nmedia.adapter.ChooseUsersAdapter
import ru.netology.nmedia.databinding.FragmentChooseUsersBinding
import ru.netology.nmedia.viewmodel.RegisterViewModel

class ChooseUsersFragment : Fragment() {
    private val registerViewModel: RegisterViewModel by activityViewModels()
    private val adapter = ChooseUsersAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentChooseUsersBinding.inflate(inflater,container,false)

        registerViewModel.getUsers()

        binding.usersRecycleView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ChooseUsersFragment.adapter
        }

        registerViewModel.users.observe(viewLifecycleOwner) { users ->
            adapter.submitList(users)
        }

        binding.saveButton.setOnClickListener {
            //TODO
            findNavController().navigateUp()
        }
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }
}