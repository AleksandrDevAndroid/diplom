package ru.netology.nmedia.fragment

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentNewEventBinding
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.dto.EventType
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.DatePickerHelper
import ru.netology.nmedia.util.FormatDate
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.EventViewModel
import kotlin.getValue

@AndroidEntryPoint
class NewEventFragment : Fragment() {
    companion object {
        var Bundle.textArg: String? by StringArg
    }

    private var selectedType: String = "ONLINE"

    private fun showFormatDialog() {
        val options = arrayOf("ONLINE", "OFFLINE")
        val current = options.indexOf(selectedType).coerceAtLeast(0)

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.format)
            .setSingleChoiceItems(options, current) { dialog, which ->
                selectedType = options[which]
                eventViewModel.changeType(selectedType)
                Toast.makeText(requireContext(), "Формат: $selectedType", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private val eventViewModel: EventViewModel by activityViewModels()
    val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data!!
                eventViewModel.changePhoto(fileUri, fileUri.toFile())
            } else {
                Toast.makeText(
                    requireContext(),
                    R.string.error_task_cancelled,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewEventBinding.inflate(inflater, container, false)

        arguments?.textArg
            ?.let(binding.edit::setText)
        eventViewModel.eventCreated.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }


        eventViewModel.photo.observe(viewLifecycleOwner) {
            binding.photo.setImageURI(it.uri)
            binding.removePhoto.isVisible = it.uri != null
        }

        binding.removePhoto.setOnClickListener {
            eventViewModel.changePhoto(null, null)
        }

        binding.pickFile.setOnClickListener {
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
            findNavController().navigate(R.id.action_new_event_to_chooseUsers)
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.saveButton.setOnClickListener {
            if (eventViewModel.edited.value?.type.isNullOrEmpty()) {
                Toast.makeText(context, R.string.choose_type, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (eventViewModel.edited.value?.datetime.isNullOrEmpty()) {
                Toast.makeText(context, R.string.choose_date, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            eventViewModel.changeContent(binding.edit.text.toString())
            eventViewModel.save()
            AndroidUtils.hideKeyboard(requireView())
        }

        binding.fab.setOnClickListener {
            PopupMenu(requireContext(), binding.fab).apply {
                inflate(R.menu.menu_event)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.date -> {
                            val helper = DatePickerHelper(
                                fragmentManager = childFragmentManager,
                                onDateSelected = { display ->
                                    binding.date.text = display
                                    binding.date.isVisible = false
                                    eventViewModel.changeDatetime(
                                        FormatDate.formatDateForServer(
                                            display
                                        )
                                    )
                                }
                            )
                            helper.showDateTime()
                            true
                        }
                        R.id.format -> {
                            showFormatDialog()
                            true
                        }
                        else -> false
                    }
                }
            }.show()
        }
        return binding.root
    }
}

