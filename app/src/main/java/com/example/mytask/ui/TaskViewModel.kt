package com.example.mytask.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mytask.model.Task
import com.example.mytask.model.TaskRepository
import kotlinx.coroutines.launch

class TaskViewModel(
    private val repo: TaskRepository
) : ViewModel() {

    // Convert Flow<List<Task>> to LiveData for UI observation
    val allTasks: LiveData<List<Task>> = repo.allTasks.asLiveData()

    fun insert(task: Task) {
        viewModelScope.launch {
            repo.insert(task)
        }
    }

    fun update(task: Task) {
        viewModelScope.launch {
            repo.update(task)
        }
    }

    fun delete(task: Task) {
        viewModelScope.launch {
            repo.delete(task)
        }
    }
}