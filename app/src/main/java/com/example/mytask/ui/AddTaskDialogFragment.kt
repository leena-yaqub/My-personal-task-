package com.example.mytask.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.example.mytask.databinding.FragmentAddTaskDialogBinding
import com.example.mytask.model.Priority
import com.example.mytask.model.Task
import java.text.SimpleDateFormat
import java.util.*

class AddTaskDialogFragment(
    private val existingTask: Task? = null,
    private val onSave: (Task) -> Unit
) : DialogFragment() {

    private var _binding: FragmentAddTaskDialogBinding? = null
    private val binding get() = _binding!!

    private val calendar = Calendar.getInstance()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentAddTaskDialogBinding.inflate(LayoutInflater.from(requireContext()))

        // Set up priority spinner
        val priorities = Priority.entries.map { it.name }
        val adapter = android.widget.ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, priorities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPriority.adapter = adapter

        // Pre-fill if editing
        existingTask?.let { task ->
            binding.editTextTitle.setText(task.title)
            binding.editTextDescription.setText(task.description)
            binding.editTextDate.setText(task.dueDate)
            binding.spinnerPriority.setSelection(task.priority.ordinal)
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        binding.editTextDate.setOnClickListener {
            val listener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
                calendar.set(year, month, day)
                binding.editTextDate.setText(dateFormat.format(calendar.time))
            }

            DatePickerDialog(
                requireContext(),
                listener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // ✅ Save button
        binding.buttonSave.setOnClickListener {
            val title = binding.editTextTitle.text.toString().trim()
            val description = binding.editTextDescription.text.toString().trim()
            val dueDate = binding.editTextDate.text.toString().trim()
            val priority = Priority.entries[binding.spinnerPriority.selectedItemPosition]

            if (title.isEmpty()) {
                binding.editTextTitle.error = "Title is required"
                return@setOnClickListener
            }

            val task = Task(
                id = existingTask?.id ?: 0,
                title = title,
                description = description,
                dueDate = dueDate,
                priority = priority,
                isCompleted = existingTask?.isCompleted ?: false
            )

            onSave(task)
            dismiss()
        }

        // ✅ Cancel button
        binding.buttonCancel.setOnClickListener {
            dismiss()
        }

        return Dialog(requireContext()).apply {
            setContentView(binding.root)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
