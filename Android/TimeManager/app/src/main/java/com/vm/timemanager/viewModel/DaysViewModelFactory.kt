package com.vm.timemanager.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vm.timemanager.fragments.DaysFragment

class DaysViewModelFactory(private val day: String)
    : ViewModelProvider.Factory {

//        override fun <T: ViewModel> create(modelClass: Class<T>): T {
//            if (modelClass.isAssignableFrom(DaysViewModel::class.java)) {
//                return DaysViewModel(day) as T
//             }
//
//            throw  IllegalArgumentException("Unknown ViewModel")
//        }
}