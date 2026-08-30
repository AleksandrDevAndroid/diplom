package ru.netology.nmedia.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.adapter.JobsAdapter
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.OnJobInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.databinding.FragmentViewProfileBinding
import ru.netology.nmedia.dto.Job
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.Users
import ru.netology.nmedia.view.loadCircleCrop
import ru.netology.nmedia.viewmodel.JobViewModel
import ru.netology.nmedia.viewmodel.PostViewModel
import ru.netology.nmedia.viewmodel.RegisterViewModel
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment: Fragment() {
    @Inject
    lateinit var appAuth: AppAuth
    private val postViewModel: PostViewModel by activityViewModels()
    private val jobViewModel: JobViewModel by activityViewModels()
    private val registerViewModel: RegisterViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentViewProfileBinding.inflate(inflater, container, false)

        fun updateUserInfo(user: Users) {
            binding.apply {
                name.text = user.name
                avatar.loadCircleCrop(user.avatar)
            }
        }
        val userId = appAuth.authState.value.id
        if (userId != 0L) {
            registerViewModel.getUser(userId)
        }

        val postAdapter = PostsAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                postViewModel.likeById(post.id, post.likedByMe)
            }
        })

        val jobAdapter = JobsAdapter(object : OnJobInteractionListener {
            override fun onRemove(job: Job) {
                jobViewModel.deleteJob(job)
            }
        })

        binding.jobRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = jobAdapter
        }
        binding.postRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = postAdapter
        }

        jobViewModel.job.observe(viewLifecycleOwner) { jobs ->
            jobAdapter.submitList(jobs)
        }

        registerViewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                updateUserInfo(it)
            }
        }

        postViewModel.dataState.observe(viewLifecycleOwner) { state ->
            if (state.error) {
                Snackbar.make(
                    binding.root,
                    ru.netology.nmedia.R.string.error_loading,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(ru.netology.nmedia.R.string.retry_loading) { postViewModel.refreshPosts() }
                    .show()
            }
        }
        postViewModel.updateStatus()
        jobViewModel.loadJobs()
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
            binding.postRecyclerView.visibility = View.VISIBLE
            binding.jobRecyclerView.visibility = View.GONE
            binding.fab.hide()
            postViewModel.updateStatus()
        } else {
            binding.postRecyclerView.visibility = View.GONE
            binding.jobRecyclerView.visibility = View.VISIBLE
            binding.fab.show()
            jobViewModel.loadJobs()
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


