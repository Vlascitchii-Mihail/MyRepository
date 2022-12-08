package com.vm.timemanager.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.vm.timemanager.adapter.AdapterDays
import com.vm.timemanager.databinding.FragmentDaysBinding
import com.vm.timemanager.viewModel.DaysViewModel

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
        val view = binding.root
//        val viewModelFactory = this.context?.let { DaysViewModelFactory(dayName, it) }
        val viewModel = ViewModelProvider(this)[DaysViewModel::class.java]

//        viewModel.day = dayName
        val adapterDays = AdapterDays()

        //smart cast
//        if (view is RecyclerView) {
//            with(view) {
//                binding.taskList.adapter = AdapterDays()
//
//                //add decoration to RecyclerView
//                addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
//            }
//        }

//        binding.taskList.adapter = adapterDays
        with(binding.taskList) {
            adapter = adapterDays
            addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
        }

        viewModel.dayName = DaysFragmentArgs.fromBundle(requireArguments()).dayName

        viewModel.getAllTasks()

        viewModel.allTasks.observe(viewLifecycleOwner, Observer { taskList ->
            adapterDays.submitList(taskList)
        })


        binding.newTaskFab.setOnClickListener {
            val action = DaysFragmentDirections.dayFragmentToNewTaskFragment(viewModel.dayName)
            this.findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}