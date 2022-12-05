package com.vm.timemanager.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.vm.timemanager.data.Task
import com.vm.timemanager.databinding.FragmentNewTaskAddingBinding
import com.vm.timemanager.viewModel.DaysViewModel
import java.time.LocalDate
import java.util.*

/**
 * Adding a new Task
 */
class NewTaskAdding : Fragment() {

    private var _binding: FragmentNewTaskAddingBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<DaysViewModel>()
    private var newDate: Date? = null
    private val args: NewTaskAddingArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_new_task_adding, container, false)
        _binding = FragmentNewTaskAddingBinding.inflate(inflater, container, false)
        val view = binding.root

        //val viewModel = ViewModelProvider(this)[DaysViewModel::class.java]
//        val day = NewTaskAddingArgs.fromBundle(requireArguments()).day

        setFragmentResultListener(DatePickerFragment.REQUEST_KEY_DATE) { _, bundle ->
            newDate = bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE) as Date
            binding.buttonStartDate.text = newDate?.toString()
        }

        binding.apply {

            buttonStartDate.setOnClickListener {
                findNavController().navigate(
                    NewTaskAddingDirections.selectDate(Calendar.getInstance().time)
                )

            }

//            startTime.text = newDate?.toString() ?: ""

            buttonEndDate.setOnClickListener {
                findNavController().navigate(
                    NewTaskAddingDirections.selectDate(Calendar.getInstance().time)
                )
            }

//            endTime.text = newDate?.toString() ?: ""

            saveButton.setOnClickListener {
                viewModel.addTask(Task(
                    day = args.day,
                    startTime = newDate,
                    endTime = newDate,
                    taskName = taskName.text.toString(),
                    taskDescription = taskDescription.text.toString()))
            }
        }


        return view
    }
}