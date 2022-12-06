package com.vm.timemanager.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vm.timemanager.data.DatabaseTimeManager
import com.vm.timemanager.data.RepositoryTimeManager
import com.vm.timemanager.data.Task
import kotlinx.coroutines.launch

class NewTaskAddingViewModel(application: Application): AndroidViewModel(application) {
    private val repository: RepositoryTimeManager

    var task: MutableLiveData<Task>? = MutableLiveData<Task>()
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
}