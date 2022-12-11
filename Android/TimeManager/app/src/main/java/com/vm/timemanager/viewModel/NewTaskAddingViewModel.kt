package com.vm.timemanager.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.vm.timemanager.data.DatabaseTimeManager
import com.vm.timemanager.data.RepositoryTimeManager
import com.vm.timemanager.data.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewTaskAddingViewModel(application: Application): AndroidViewModel(application) {
    private val repository: RepositoryTimeManager

    var task: MutableLiveData<Task> = MutableLiveData<Task>(Task())
    var day: String? = null

    init {
        repository = DatabaseTimeManager.getDatabase(application)
            .getDaoTimeManager().let { dao->
                RepositoryTimeManager.getRepository(dao)
            }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            repository.addTask(task)
        }
    }

    fun getTask(taskId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                task.value = repository.getTask(taskId)
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun deleteTask() {
        viewModelScope.launch {
            task.value?.let {
                repository.deleteTask(it)
            }
        }
    }
}