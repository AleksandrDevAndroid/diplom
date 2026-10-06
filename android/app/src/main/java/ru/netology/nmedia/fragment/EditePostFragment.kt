package ru.netology.nmedia.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentEditPostBinding
import ru.netology.nmedia.viewmodel.PostViewModel
import kotlin.getValue

@AndroidEntryPoint
class EditePostFragment : Fragment() {
    private val viewModel: PostViewModel by activityViewModels()
    val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data!!
                viewModel.changePhoto(fileUri, fileUri.toFile())
            } else {
                Toast.makeText(requireContext(), R.string.error_task_cancelled, Toast.LENGTH_SHORT)
                    .show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEditPostBinding.inflate(layoutInflater, container, false)

        viewModel.edited.observe(viewLifecycleOwner) { post ->
            post?.let {
                binding.edit.setText(it.content)
                val hasPhoto = it.attachment != null || viewModel.photo.value?.uri != null
                binding.removePhoto.isVisible = hasPhoto
                if (!hasPhoto) {
                    binding.photo.setImageURI(null)
                    binding.photo.setImageDrawable(null)
                }
            }
        }

        viewModel.edited.observe(viewLifecycleOwner) { post ->
            post?.let {
                binding.edit.setText(it.content)
                if (it.attachment != null) {
                    binding.photoContainer.isVisible = true
                    binding.removePhoto.isVisible = true


                    Glide.with(this)
                        .load(it.attachment.url)
                        .timeout(10_000)
                        .into(binding.photo)
                } else {
                    if (viewModel.photo.value == null) {
                        binding.removePhoto.isVisible = false
                        binding.photo.setImageURI(null)
                    }
                }
            }
        }


        binding.removePhoto.setOnClickListener {
            viewModel.removePhoto()
            binding.photo.setImageURI(null)
            binding.removePhoto.isVisible = false
        }

        binding.pickPhoto.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(2048)
                .galleryOnly()
                .galleryMimeTypes(arrayOf("image/png", "image/jpeg"))
                .createIntent { intent -> startForProfileImageResult.launch(intent) }
        }

        binding.takePhoto.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(2048)
                .cameraOnly()
                .createIntent { intent -> startForProfileImageResult.launch(intent) }
        }

        binding.pickUsers.setOnClickListener {
            findNavController().navigate(R.id.action_edite_post_to_chooseUsers)
        }

        binding.saveButton.setOnClickListener {
            val newContent = binding.edit.text.toString().trim()
            if (newContent.isNotEmpty()) {
                viewModel.edited.value?.let { post ->
                    viewModel.edit(post.copy(content = newContent))
                }
                viewModel.saveEdited()
                findNavController().navigateUp()
            }
        }
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }
}