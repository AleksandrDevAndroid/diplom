package ru.netology.nmedia.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.filter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.netology.nmedia.adapter.JobsAdapter
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.OnJobInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.databinding.FragmentViewProfileBinding
import ru.netology.nmedia.dto.Job
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.Users
import ru.netology.nmedia.mediaPlayer.MediaLifecycleObserver
import ru.netology.nmedia.view.loadCircleCrop
import ru.netology.nmedia.viewmodel.JobViewModel
import ru.netology.nmedia.viewmodel.PostViewModel
import ru.netology.nmedia.viewmodel.RegisterViewModel
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment: Fragment() {
    @Inject
    lateinit var appAuth: AppAuth
    private val postViewModel: PostViewModel by viewModels()
    private val jobViewModel: JobViewModel by viewModels()
    private val registerViewModel: RegisterViewModel by viewModels()
    private lateinit var mediaObserver: MediaLifecycleObserver



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

        val userId = arguments?.getLong("userId") ?: 0L
        val myId = appAuth.authState.value.id

        val targetUserId = if (userId != 0L) userId else myId
        val isMyProfile = targetUserId == myId

        if (!isMyProfile) {
            binding.fab.visibility = View.GONE
            binding.logout.visibility = View.GONE
        }

        if (targetUserId != 0L) {
            registerViewModel.getUser(targetUserId)
            jobViewModel.loadJobs(targetUserId)
        }

        mediaObserver = MediaLifecycleObserver(requireContext())
        viewLifecycleOwner.lifecycle.addObserver(mediaObserver)

        val postAdapter = PostsAdapter(mediaObserver,object : OnInteractionListener {
            override fun onLike(post: Post) {
                postViewModel.likeById(post.id, post.likedByMe)
            }

            override fun onRemove(post: Post) {
                postViewModel.removeById(post.id)
                postViewModel.refreshPosts()
            }

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }
                val shareIntent =
                    Intent.createChooser(
                        intent,
                        getString(ru.netology.nmedia.R.string.chooser_share_post)
                    )
                startActivity(shareIntent)
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

        registerViewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                updateUserInfo(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycleScope.launchWhenCreated {
                postViewModel.data.collectLatest { post ->
                    val userId = appAuth.authState.value.id
                    val filteredPagingData = post.filter { feedItem ->
                        (feedItem as? Post)?.authorId == targetUserId
                    }
                    postAdapter.submitData(filteredPagingData)
                }
            }
        }

        jobViewModel.job.observe(viewLifecycleOwner) { jobs ->
            jobAdapter.submitList(jobs)
        }


        postViewModel.updateStatus()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        binding.postRecyclerView.visibility = View.VISIBLE
                        binding.jobRecyclerView.visibility = View.GONE
                        binding.fab.hide()
                    }
                    1 -> {
                        binding.postRecyclerView.visibility = View.GONE
                        binding.jobRecyclerView.visibility = View.VISIBLE
                        if (isMyProfile) binding.fab.show() else binding.fab.hide()
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
            if (isMyProfile) binding.fab.show() else binding.fab.hide()
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


