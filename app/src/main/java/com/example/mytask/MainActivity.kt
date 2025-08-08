package com.example.mytask

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mytask.adapter.TaskAdapter
import com.example.mytask.databinding.ActivityMainBinding
import com.example.mytask.model.TaskDatabase
import com.example.mytask.model.TaskRepository
import com.example.mytask.ui.AddTaskDialogFragment
import com.example.mytask.ui.LoginActivity
import com.example.mytask.ui.TaskViewModel
import com.example.mytask.ui.TaskViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val prefs by lazy { getSharedPreferences("MyAppPrefs", MODE_PRIVATE) }

    private val viewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(TaskRepository(TaskDatabase.getDatabase(application).taskDao()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ NO LOGIN CHECK HERE! SplashActivity handles that

        // Set up layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✅ Set up the app bar
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.title = "My Tasks"

        // ✅ Set up RecyclerView
        setupRecyclerView()

        // ✅ Set up FAB
        binding.fabAddTask.setOnClickListener {
            AddTaskDialogFragment(null) { task ->
                viewModel.insert(task)
                Toast.makeText(this, "Added: ${task.title}", Toast.LENGTH_SHORT).show()
            }.show(supportFragmentManager, "AddTask")
        }
    }

    private fun setupRecyclerView() {
        val adapter = TaskAdapter(
            this,
            onEdit = { task ->
                AddTaskDialogFragment(task) { updatedTask ->
                    viewModel.update(updatedTask)
                }.show(supportFragmentManager, "Edit")
            },
            onDelete = { task ->
                AlertDialog.Builder(this)
                    .setTitle("Delete Task")
                    .setMessage("Are you sure you want to delete '${task.title}'?")
                    .setPositiveButton("Delete") { _, _ ->
                        viewModel.delete(task)
                        Toast.makeText(this, "Deleted: ${task.title}", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            },
            onToggle = { task ->
                viewModel.update(task)
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // ✅ Observe tasks
        viewModel.allTasks.observe(this) { tasks ->
            adapter.submitList(tasks)
            binding.textViewEmpty.visibility = if (tasks.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    // ✅ Create menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // ✅ Handle logout
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_logout -> {
                AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Logout") { _, _ ->
                        // Clear everything
                        FirebaseAuth.getInstance().signOut()
                        prefs.edit { clear() }

                        // Go to login
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}