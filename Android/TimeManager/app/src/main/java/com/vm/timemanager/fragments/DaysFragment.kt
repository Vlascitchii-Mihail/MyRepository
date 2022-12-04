package com.vm.timemanager.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.vm.timemanager.R
import com.vm.timemanager.adapter.AdapterDays
import com.vm.timemanager.data.Task
import com.vm.timemanager.databinding.FragmentDaysBinding
import com.vm.timemanager.viewModel.DaysViewModel
import com.vm.timemanager.viewModel.DaysViewModelFactory

/**
 * A simple [Fragment] subclass.
 */
class DaysFragment : Fragment() {

    private var _binding: FragmentDaysBinding? = null
    private val binding get() = _binding!!

    //private val dayName: String = "Monday"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //val view =  inflater.inflate(R.layout.fragment_day, container, false)
        _binding = FragmentDaysBinding.inflate(inflater, container, false)

        val dayName = DaysFragmentArgs.fromBundle(requireArguments()).dayName
//        val viewModelFactory = this.context?.let { DaysViewModelFactory(dayName, it) }
        val viewModel = ViewModelProvider(this)[DaysViewModel::class.java]

//        viewModel.day = dayName
        val adapter = AdapterDays()

        binding.taskList.adapter = adapter

        viewModel.getAllTasks(dayName).observe(viewLifecycleOwner, Observer { taskList ->
            adapter.submitList(taskList)
        })

        binding.newTaskFab.setOnClickListener {
            this.findNavController().navigate(R.id.newTaskAddingFragment)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}