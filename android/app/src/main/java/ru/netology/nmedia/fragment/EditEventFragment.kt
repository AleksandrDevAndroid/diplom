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
import ru.netology.nmedia.databinding.FragmentEditEventBinding
import ru.netology.nmedia.viewmodel.EventViewModel
import kotlin.getValue

@AndroidEntryPoint
class EditeEventFragment : Fragment() {
    private val eventViewModel: EventViewModel by activityViewModels()
    val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data!!
                eventViewModel.changePhoto(fileUri, fileUri.toFile())
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
        val binding = FragmentEditEventBinding.inflate(layoutInflater, container, false)

        eventViewModel.edited.observe(viewLifecycleOwner) { event ->
            event?.let {
                binding.edit.setText(it.content)
                val hasPhoto = it.attachment != null || eventViewModel.photo.value?.uri != null
                binding.removePhoto.isVisible = hasPhoto
                if (!hasPhoto) {
                    binding.photo.setImageURI(null)
                    binding.photo.setImageDrawable(null)
                }
            }
        }

        eventViewModel.photo.observe(viewLifecycleOwner) { photo ->
            val uri = photo.uri
            if (uri != null) {
                binding.photoContainer.isVisible = true
                Glide.with(binding.photo)
                    .load(uri)
                    .into(binding.photo)
                binding.removePhoto.isVisible = true
            } else {
                val attachment = eventViewModel.edited.value?.attachment
                if (attachment == null) {
                    binding.photo.setImageDrawable(null)
                    binding.removePhoto.isVisible = false
                    binding.photoContainer.isVisible = false
                }
            }
        }

        binding.removePhoto.setOnClickListener {
            eventViewModel.changePhoto(null, null)
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
            findNavController().navigate(R.id.action_edit_event_to_chooseUsers)
        }

        binding.saveButton.setOnClickListener {
            val newContent = binding.edit.text.toString().trim()
            if (newContent.isNotEmpty()) {
                eventViewModel.edited.value?.let { post ->
                    eventViewModel.edit(post.copy(content = newContent))
                }
                eventViewModel.saveEdited()
                findNavController().navigateUp()
            }
        }
        return binding.root
    }
}