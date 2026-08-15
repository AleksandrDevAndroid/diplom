package ru.netology.nmedia.fagment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentRegisterBinding
import ru.netology.nmedia.viewmodel.RegisterViewModel
import java.io.File

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private val viewModelRegister: RegisterViewModel by activityViewModels()
    private var avatar: File? = null

    val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data!!
                avatar = fileUri.toFile()
            } else {
                Toast.makeText(
                    requireContext(),
                    com.github.dhaval2404.imagepicker.R.string.error_task_cancelled,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentRegisterBinding.inflate(inflater, container, false)

        binding.takePhoto.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(2048)
                .cameraOnly()
                .createIntent { intent -> startForProfileImageResult.launch(intent) }
        }


        binding.buttonEnter.setOnClickListener {
            val name = binding.enterName.text.toString().trim()
            val login = binding.enterLogin.text.toString().trim()
            val pass = binding.enterPass.text.toString().trim()
            val passAgain = binding.enterPassAgain.text.toString().trim()

            if (name.isEmpty() || login.isEmpty() || pass.isEmpty() || pass.isEmpty()) {
                Toast.makeText(context, R.string.emptyLoginOrPass, LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (!pass.equals(passAgain)) {
                Toast.makeText(context, R.string.incorrect_pass, LENGTH_LONG).show()
                return@setOnClickListener
            }

            viewModelRegister.signUp(login, pass, name, avatar)
            return@setOnClickListener
        }

        viewModelRegister.dataState.observe(viewLifecycleOwner) { state ->
            if (state.successes) {
                findNavController().navigateUp()
            }
            if (state.error) {
                Toast.makeText(context, R.string.something_wrong, LENGTH_LONG).show()
            }
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }
}