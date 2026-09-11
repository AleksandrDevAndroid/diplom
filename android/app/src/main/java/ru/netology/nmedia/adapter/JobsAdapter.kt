package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.JobCardBinding
import ru.netology.nmedia.dto.Job
import ru.netology.nmedia.extensions.formatDate

interface OnJobInteractionListener {
    fun onRemove(job: Job)
}


class JobViewHolder(
    private val binding: JobCardBinding,
    private val onInteractionListener: OnJobInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(job: Job) {
        binding.apply {
            nameOrganization.text = job.name
            position.text = job.position
            timeWorking.text = formatPeriod(job.start, job.finish).formatDate()
            webSite.text = job.link
            if (webSite.text.isNullOrEmpty()) {
                webSite.isVisible = false
            }

            binding.delete.setOnClickListener {
                onInteractionListener.onRemove(job)
            }
        }
    }
}

class JobsAdapter(
    private val onJobInteractionListener: OnJobInteractionListener
) : RecyclerView.Adapter<JobViewHolder>() {

    private var jobs = listOf<Job>()

    fun submitList(newJobs: List<Job>) {
        jobs = newJobs
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): JobViewHolder {
        val binding: JobCardBinding =
            JobCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobViewHolder(binding, onJobInteractionListener)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        return holder.bind(jobs[position])
    }

    override fun getItemCount(): Int {
        return jobs.size
    }

}

private fun formatPeriod(start: String, finish: String?): String {
    return if (finish.isNullOrEmpty()) {
        "$start – НВ"
    } else {
        "$start – $finish"
    }
}