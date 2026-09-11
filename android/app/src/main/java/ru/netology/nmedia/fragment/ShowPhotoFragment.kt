package ru.netology.nmedia.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentShowPhotoBinding
import ru.netology.nmedia.viewmodel.PostViewModel
@AndroidEntryPoint
class ShowPhotoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentShowPhotoBinding.inflate(inflater, container, false)

        val url = arguments?.getString("url")
        val urlAttachment = url
        Glide.with(binding.showPhoto)
            .load(urlAttachment)
            .placeholder(R.drawable.outline_arrow_cool_down_24)
            .error(R.drawable.error)
            .timeout(6_000)
            .into(binding.showPhoto)

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }

}