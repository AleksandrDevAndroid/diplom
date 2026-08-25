package ru.netology.nmedia.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.databinding.FragmentViewProfileBinding
import ru.netology.nmedia.viewmodel.AuthViewModel
import javax.inject.Inject

class ProfileFragment (
): Fragment() {
    @Inject
    lateinit var appAuth: AppAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentViewProfileBinding.inflate(inflater, container, false)

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        binding.fab.hide()
                    }
                    1 -> {
                        binding.fab.show()
                    }
                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabReselected(p0: TabLayout.Tab?) {
            }
        })

        if (binding.tabLayout.selectedTabPosition == 0) {
            binding.fab.hide()
        } else {
            binding.fab.show()
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(ru.netology.nmedia.R.id.action_viewProfile_to_createJob)
        }

        binding.logout.setOnClickListener {
            appAuth.removeAuth()
            findNavController().navigateUp()
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

}