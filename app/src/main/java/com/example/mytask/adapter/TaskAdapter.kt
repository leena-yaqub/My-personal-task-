package com.example.mytask.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mytask.R
import com.example.mytask.databinding.ItemTaskBinding
import com.example.mytask.model.Task
import com.example.mytask.model.Priority

class TaskAdapter(
    private val context: Context,
    private val onEdit: (Task) -> Unit,
    private val onDelete: (Task) -> Unit,
    private val onToggle: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var taskList = listOf<Task>()

    inner class TaskViewHolder(val b: ItemTaskBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        val b = holder.b

        // Set task details
        b.textViewTitle.text = task.title
        b.textViewDescription.text = task.description
        b.textViewDate.text = task.dueDate
        b.textViewPriority.text = task.priority.name
            .replaceFirstChar { it.uppercase() }
        b.checkBoxCompleted.isChecked = task.isCompleted

        // Apply strikethrough if completed
        b.textViewTitle.paintFlags = if (task.isCompleted) {
            b.textViewTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            b.textViewTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        // Set badge background based on priority
        val badgeDrawable = when (task.priority) {
            Priority.HIGH -> R.drawable.badge_high
            Priority.MEDIUM -> R.drawable.badge_medium
            Priority.LOW -> R.drawable.badge_low
        }
        b.textViewPriority.setBackgroundResource(badgeDrawable)

        // Handle toggle checkbox
        b.checkBoxCompleted.setOnClickListener {
            onToggle(task.copy(isCompleted = b.checkBoxCompleted.isChecked))
        }

        // Edit and delete buttons
        b.buttonEdit.setOnClickListener { onEdit(task) }
        b.buttonDelete.setOnClickListener { onDelete(task) }
    }

    override fun getItemCount(): Int = taskList.size

    fun submitList(newList: List<Task>) {
        taskList = newList
        notifyDataSetChanged()
    }
}
